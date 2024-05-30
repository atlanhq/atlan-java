/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.Atlan
import com.atlan.exception.ErrorCode
import com.atlan.exception.InvalidRequestException
import com.atlan.model.assets.Asset
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.RowSerde
import mu.KotlinLogging
import java.io.File

/**
 * Actually run the migrator, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object EnrichmentMigrator {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<EnrichmentMigratorCfg>()
        val batchSize = Utils.getOrDefault(config.batchSize, 20)
        val fieldSeparator = Utils.getOrDefault(config.fieldSeparator, ",")[0]
        val sourceConnectionQN = Utils.getOrDefault(config.sourceConnection, listOf(""))[0]
        val targetConnectionQNs = Utils.getOrDefault(config.targetConnection, listOf(""))
        val sourcePrefix = Utils.getOrDefault(config.sourceQnPrefix, "")
        val includeArchived = Utils.getOrDefault(config.includeArchived, false)
        val isPrefixDatabasePattern = Utils.getOrDefault(config.isPrefixDatabasePattern, false)
        val sourceDatabaseName = getSourceDatabaseNames(isPrefixDatabasePattern, sourceConnectionQN, sourcePrefix)
        val sourceQN = if (sourcePrefix.isBlank()) {
            sourceConnectionQN
        } else if (isPrefixDatabasePattern) {
            "$sourceConnectionQN/$sourceDatabaseName"
        } else {
            "$sourceConnectionQN/$sourcePrefix"
        }

        if (isPrefixDatabasePattern) {
            val sourceDatabaseNames = getDatabaseNames(sourceConnectionQN, sourcePrefix)
            if (sourceDatabaseNames.size > 1) {
                throw InvalidRequestException(ErrorCode.UNEXPECTED_NUMBER_OF_DATABASE_FOUND, sourcePrefix)
            }
        }

        // 1. Extract the enriched metadata
        val extractConfig = AssetExportBasicCfg(
            exportScope = "ENRICHED_ONLY",
            qnPrefix = sourceQN,
            includeGlossaries = false,
            includeArchived = includeArchived,
        )
        Exporter.export(extractConfig, outputDirectory)
        val extractFile = "$outputDirectory${File.separator}asset-export.csv"

        // 2. Transform to the target metadata assets (limiting attributes as requested)
        val attributeLimits = Utils.getOrDefault(config.attributesList, listOf())
        val cmLimits = Utils.getOrDefault(config.customMetadata, "").split("|")
        val includeOOTB = Utils.getOrDefault(config.limitType, "EXCLUDE") == "INCLUDE"
        val includeCM = Utils.getOrDefault(config.cmLimitType, "EXCLUDE") == "INCLUDE"
        val start = mutableListOf(Asset.QUALIFIED_NAME.atlanFieldName, Asset.TYPE_NAME.atlanFieldName)
        val defaultAttrsToExtract = AssetExporter.getAttributesToExtract(true, Exporter.getAllCustomMetadataFields())
        if (includeArchived) {
            start.add(Asset.STATUS.atlanFieldName)
        }
        if (includeOOTB) {
            start.add(Asset.NAME.atlanFieldName)
            attributeLimits.forEach {
                start.add(it)
            }
        } else {
            defaultAttrsToExtract.forEach {
                if (it !is CustomMetadataField) {
                    val fieldName = RowSerde.getHeaderForField(it)
                    start.add(fieldName)
                }
            }
            attributeLimits.forEach {
                start.remove(it)
            }
        }
        val header = if (includeCM) {
            cmLimits.forEach {
                start.add(it)
            }
            start.toList()
        } else {
            defaultAttrsToExtract.forEach {
                if (it is CustomMetadataField) {
                    val fieldName = RowSerde.getHeaderForField(it)
                    start.add(fieldName)
                }
            }
            cmLimits.forEach {
                start.remove(it)
            }
            start.toList()
        }
        targetConnectionQNs.forEachIndexed { _, targetConnectionQN ->
            val targetDatabaseNames = getTargetDatabaseName(isPrefixDatabasePattern, targetConnectionQN, sourcePrefix)
            targetDatabaseNames.forEach { targetDatabaseName ->
                val ctx = MigratorContext(
                    sourceConnectionQN = sourceConnectionQN,
                    targetConnectionQN = targetConnectionQN,
                    includeArchived = includeArchived,
                    sourceDatabaseName = sourceDatabaseName,
                    targetDatabaseName = targetDatabaseName,
                )
                val targetConnectionFilename = if (targetDatabaseName.isNotBlank()) {
                    "${targetConnectionQN}_$targetDatabaseName".replace("/", "_")
                } else {
                    targetConnectionQN.replace("/", "_")
                }
                val transformedFile = "$outputDirectory${File.separator}CSA_EM_transformed_$targetConnectionFilename.csv"
                val transformer = Transformer(
                    ctx,
                    extractFile,
                    header.toList(),
                    logger,
                    fieldSeparator,
                )
                transformer.transform(transformedFile)

                // 3. Import the transformed file
                val importConfig = AssetImportCfg(
                    assetsFile = transformedFile,
                    assetsUpsertSemantic = "update",
                    assetsFailOnErrors = Utils.getOrDefault(config.failOnErrors, true),
                    assetsBatchSize = batchSize,
                    assetsFieldSeparator = fieldSeparator.toString(),
                )
                Importer.import(importConfig, outputDirectory)
            }
        }
    }

    @JvmStatic
    fun getDatabaseNames(connectionQN: String, sourcePrefix: String): MutableList<String> {
        val databaseNames = Atlan.getDefaultClient().assets.select()
            .where(Asset.QUALIFIED_NAME.startsWith(connectionQN))
            .where(Asset.NAME.regex(sourcePrefix))
            .sort(Asset.NAME.order(SortOrder.Asc))
            .stream()
            .map { it.name }
            .toList()
        return databaseNames
    }

    @JvmStatic
    fun getTargetDatabaseName(
        isPrefixDatabasePattern: Boolean,
        targetConnectionQN: String,
        sourcePrefix: String,
    ): List<String> {
        if (isPrefixDatabasePattern) {
            val databaseNames = getDatabaseNames(targetConnectionQN, sourcePrefix)
            if (databaseNames.size < 1) {
                throw InvalidRequestException(
                    ErrorCode.UNEXPECTED_NUMBER_OF_DATABASE_FOUND,
                    "at least one",
                    sourcePrefix,
                    databaseNames.size.toString(),
                )
            }
            return databaseNames
        }
        return listOf("")
    }

    @JvmStatic
    fun getSourceDatabaseNames(
        isPrefixDatabasePattern: Boolean,
        sourceConnectionQN: String,
        sourcePrefix: String,
    ) = if (isPrefixDatabasePattern) {
        val sourceDatabaseNames = getDatabaseNames(sourceConnectionQN, sourcePrefix)
        if (sourceDatabaseNames.size != 1) {
            throw InvalidRequestException(
                ErrorCode.UNEXPECTED_NUMBER_OF_DATABASE_FOUND,
                "only one",
                sourcePrefix,
                sourceDatabaseNames.size.toString(),
            )
        } else {
            sourceDatabaseNames[0]
        }
    } else {
        ""
    }

    data class MigratorContext(
        val sourceConnectionQN: String,
        val targetConnectionQN: String,
        val includeArchived: Boolean,
        val sourceDatabaseName: String,
        val targetDatabaseName: String,
    )
}
