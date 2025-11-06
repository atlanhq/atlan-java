/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DatabricksUnityCatalogTag
import com.atlan.model.assets.DbtTag
import com.atlan.model.assets.SnowflakeTag
import com.atlan.model.assets.SourceTag
import com.atlan.model.enums.AtlanEnum
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.model.typedefs.AtlanTagOptions
import com.atlan.model.typedefs.TypeDef
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.CellXformer
import com.atlan.pkg.serde.cell.ConnectionXformer
import com.atlan.pkg.serde.cell.EnumXformer
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.util.ParallelBatch
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import mu.KLogger
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicLong
import kotlin.streams.asSequence

/**
 * Import tags into Atlan from a provided CSV file.
 *
 * Only the Atlan tags attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param logger through which to write log entries
 * @param fieldSeparator character used to separate each field in the input CSV
 * @param batchSize maximum number of tags to create per underlying API call
 */
class AtlanTagImporter(
    private val ctx: PackageContext<AssetImportCfg>,
    private val filename: String,
    private val logger: KLogger,
    private val fieldSeparator: Char = ',',
    private val batchSize: Int = 20,
) {
    private val reader: CsvReader<CsvRecord>
    private val counter: CsvReader<CsvRecord>
    private val header: List<String> = CSVXformer.getHeader(filename, fieldSeparator)
    private val tagIdx: Int = header.indexOf(TAG_NAME)

    init {
        if (tagIdx < 0) {
            throw IOException(
                "Unable to find the column '$TAG_NAME'. This is a mandatory column in the input CSV.",
            )
        }
        val inputFile = Paths.get(filename)
        val builder =
            CsvReader
                .builder()
                .fieldSeparator(fieldSeparator)
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .allowExtraFields(false)
                .allowMissingFields(false)
        reader = builder.ofCsvRecord(inputFile)
        counter = builder.ofCsvRecord(inputFile)
    }

    /**
     * Actually import the tags.
     */
    fun import(): ImportResults? {
        ctx.client.atlanTagCache.refreshIfNeeded()
        val filteredRowCount = AtomicLong(0)
        counter.stream().skip(1).forEach { row ->
            val tagName = getTagName(row.fields, header)
            if (tagName.isNotBlank()) {
                filteredRowCount.incrementAndGet()
            }
        }
        counter.close()
        val totalRowCount = filteredRowCount.get()
        val count = AtomicLong(0)
        ParallelBatch(ctx.client, batchSize + 1).use { assets ->
            reader.stream().skip(1).asSequence().chunked(batchSize).forEach { chunk: List<CsvRecord> ->
                val tagDefsToCreate = mutableListOf<AtlanTagDef>()
                val tagDefsToUpdate = mutableListOf<AtlanTagDef>()
                chunk.forEach { r ->
                    val row = CSVXformer.getRowByHeader(header, r.fields, true)
                    val tagName = row.getOrElse(TAG_NAME) { "" }
                    if (tagName.isNotBlank()) {
                        val details =
                            TagDetails(
                                ctx,
                                logger,
                                tagName,
                                row.getOrElse(TAG_COLOR) { "" },
                                row.getOrElse(TAG_ICON) { "" },
                                row.getOrElse(TAG_CONNECTION) { "" },
                                row.getOrElse(TAG_CONNECTOR) { "" }.lowercase(),
                                row.getOrElse(TAG_SRC_ID) { "" },
                                CellXformer.parseDelimitedList(row.getOrElse(ALLOWED_VALUES) { "" }),
                                row.getOrElse(DBT_ACCOUNT_ID) { "" },
                                row.getOrElse(DBT_PROJECT_ID) { "" },
                                row.getOrElse(SNOWFLAKE_PATH) { "" },
                                row.getOrElse("Description") { row.getOrElse("description") { "" } },
                            )

                        // Manage the TypeDefinition for the tag
                        val tag = idempotentTagDef(ctx, details)
                        when (tag.op) {
                            TagOp.CREATE -> tagDefsToCreate.add(tag.def)
                            TagOp.UPDATE -> tagDefsToUpdate.add(tag.def)
                        }
                        count.getAndIncrement()

                        // Manage the asset, for any source-synced tags
                        val asset = idempotentTagAsset(details)
                        if (asset != null) {
                            assets.add(asset)
                        }
                    }
                }
                ctx.client.typeDefs.create(tagDefsToCreate as List<TypeDef>?)
                ctx.client.typeDefs.update(tagDefsToUpdate as List<TypeDef>?)
                assets.flush()
                Utils.logProgress(count, totalRowCount, logger, batchSize)
            }
            reader.close()
        }

        return null
    }

    private fun idempotentTagDef(
        ctx: PackageContext<AssetImportCfg>,
        tag: TagDetails,
    ): TagDefAndOp {
        try {
            val existing = ctx.client.atlanTagCache.getByName(tag.name, false)
            val builder = existing.toBuilder().description(tag.description)
            if (tag.sourceSynced) {
                if (existing.attributeDefs.size < 1 || existing.attributeDefs.first().displayName != "sourceTagAttachment") {
                    AtlanTagDef.setupSourceSynced(builder, true)
                }
                val optionsBuilder =
                    if (tag.imageUrl) {
                        AtlanTagOptions.withImage(ctx.client, tag.icon, true).toBuilder()
                    } else if (tag.color.isNotBlank()) {
                        val colorEnum = getEnumValue<AtlanTagColor>(tag.color, TAG_COLOR)
                        if (tag.icon.isBlank()) {
                            AtlanTagOptions.of(colorEnum, true).toBuilder()
                        } else {
                            val iconEnum = getEnumValue<AtlanIcon>(tag.icon, TAG_ICON)
                            AtlanTagOptions.withIcon(iconEnum, colorEnum, true).toBuilder()
                        }
                    } else {
                        AtlanTagOptions.of(AtlanTagColor.GRAY, true).toBuilder()
                    }
                builder.options(optionsBuilder.hasTag(true).build())
            }
            return TagDefAndOp(builder.build(), TagOp.UPDATE)
        } catch (e: NotFoundException) {
            val tagDefBuilder =
                if (tag.imageUrl) {
                    AtlanTagDef.creator(ctx.client, tag.name, tag.icon, tag.sourceSynced)
                } else if (tag.color.isNotBlank()) {
                    val colorEnum = getEnumValue<AtlanTagColor>(tag.color, TAG_COLOR)
                    if (tag.icon.isBlank()) {
                        AtlanTagDef.creator(tag.name, colorEnum, tag.sourceSynced)
                    } else {
                        val iconEnum = getEnumValue<AtlanIcon>(tag.icon, TAG_ICON)
                        AtlanTagDef.creator(tag.name, iconEnum, colorEnum, tag.sourceSynced)
                    }
                } else {
                    AtlanTagDef.creator(tag.name, tag.sourceSynced)
                }
            val tagDef = tagDefBuilder.description(tag.description).build()
            return TagDefAndOp(tagDef, TagOp.CREATE)
        }
    }

    private fun idempotentTagAsset(tag: TagDetails): Asset? =
        if (tag.sourceSynced) {
            val assetBuilder =
                when (tag.connectorType) {
                    "snowflake" -> {
                        SnowflakeTag.creator(
                            tag.name,
                            "${tag.connectionQualifiedName}/${tag.snowflakeSchemaPath}",
                            tag.name,
                            tag.tagIdInSource,
                            tag.allowedValues,
                        )
                    }
                    "databricks" -> {
                        DatabricksUnityCatalogTag.creator(
                            tag.name,
                            tag.connectionQualifiedName,
                            tag.name,
                            tag.tagIdInSource,
                            tag.allowedValues,
                        )
                    }
                    "dbt" -> {
                        DbtTag.creator(
                            tag.name,
                            tag.connectionQualifiedName,
                            tag.name,
                            tag.dbtAccountId,
                            tag.dbtProjectId,
                            tag.tagIdInSource,
                            tag.allowedValues,
                        )
                    }
                    "bigquery" -> {
                        throw IllegalArgumentException("Creation and management of tags for BigQuery is not currently supported.")
                    }
                    else -> {
                        SourceTag.creator(
                            tag.name,
                            tag.connectionQualifiedName,
                            tag.name,
                            tag.tagIdInSource,
                            tag.allowedValues,
                        )
                    }
                }
            assetBuilder.build()
        } else {
            null
        }

    companion object {
        const val TAG_NAME = "Atlan tag name"
        const val TAG_COLOR = "Color"
        const val TAG_ICON = "Icon"
        const val TAG_CONNECTION = "Synced connection name"
        const val TAG_CONNECTOR = "Synced connector type"
        const val TAG_SRC_ID = "Tag ID in source"
        const val ALLOWED_VALUES = "Allowed values for tag"
        const val DBT_ACCOUNT_ID = "Account ID (dbt)"
        const val DBT_PROJECT_ID = "Project ID (dbt)"
        const val SNOWFLAKE_PATH = "Schema path (Snowflake)"

        fun getTagName(
            row: List<String>,
            header: List<String>,
        ): String = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(TAG_NAME)) { "" })

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T : AtlanEnum> getEnumValue(
            value: String,
            fieldName: String,
        ): T = EnumXformer.decode(value, T::class.java as Class<AtlanEnum>, fieldName) as T
    }

    enum class TagOp {
        CREATE,
        UPDATE,
    }

    data class TagDefAndOp(
        val def: AtlanTagDef,
        val op: TagOp,
    )

    data class TagDetails(
        val ctx: PackageContext<AssetImportCfg>,
        val logger: KLogger,
        val name: String,
        val color: String,
        val icon: String,
        val connectionName: String,
        val connectorType: String,
        val tagIdInSource: String,
        val allowedValues: List<String>,
        val dbtAccountId: String,
        val dbtProjectId: String,
        val snowflakeSchemaPath: String,
        val description: String,
    ) {
        val connectionQualifiedName: String
        val sourceSynced: Boolean
        val imageUrl: Boolean

        init {
            val connectionIdentity =
                if (connectionName.isNotBlank() && connectorType.isNotBlank()) {
                    "$connectionName${ConnectionXformer.CONNECTION_DELIMITER}$connectorType"
                } else {
                    ""
                }
            connectionQualifiedName =
                if (connectionIdentity.isNotBlank()) {
                    try {
                        val connection = ctx.connectionCache.getByIdentity(connectionIdentity)
                        connection?.qualifiedName ?: ""
                    } catch (e: NotFoundException) {
                        logger.warn { "Unable to find connection with the provided identity -- not configuring source-synced tag for $name: $connectionIdentity" }
                        logger.debug(e) { "Details:" }
                        ""
                    }
                } else {
                    ""
                }
            sourceSynced = connectionQualifiedName.isNotBlank()
            imageUrl = icon.startsWith("http")
        }
    }
}
