/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.AtlanClient
import com.atlan.exception.ErrorCode
import com.atlan.exception.InvalidRequestException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.validatePathIsSafe
import com.atlan.pkg.serde.RowSerde
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import java.io.File
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.jvm.optionals.getOrElse
import kotlin.system.exitProcess

/**
 * Actually run the migrator, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object EnrichmentMigrator {
    private val logger = Utils.getLogger(EnrichmentMigrator.javaClass.name)

    @JvmStatic
    fun main(args: Array<String>) {
        val od = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<EnrichmentMigratorCfg>().use { ctx ->

            val fieldSeparator = ctx.config.getEffectiveValue(EnrichmentMigratorCfg::fieldSeparator, EnrichmentMigratorCfg::configType)

            if (fieldSeparator.length > 1) {
                logger.error { "Field separator must be only a single character. The provided value is too long: $fieldSeparator" }
                exitProcess(2)
            }
            val sourceConnectionQN = ctx.config.sourceConnection[0]
            val sourcePrefix = ctx.config.sourceQnPrefix
            val sourceDatabaseName = getSourceDatabaseNames(ctx.client, ctx.config.targetDatabasePattern, sourceConnectionQN, sourcePrefix)
            val sourceQN =
                if (sourcePrefix.isBlank()) {
                    sourceConnectionQN
                } else {
                    "$sourceConnectionQN/$sourcePrefix"
                }

            val outputDirectory = validatePathIsSafe(od)
            outputDirectory.toFile().mkdirs()

            // 1. Extract the enriched metadata
            val extractConfig =
                AssetExportBasicCfg(
                    exportScope = "ENRICHED_ONLY",
                    qnPrefix = sourceQN,
                    includeGlossaries = false,
                    includeArchived = ctx.config.includeArchived,
                )
            Utils.initializeContext(extractConfig, ctx).use { eCtx ->
                Exporter.export(eCtx, outputDirectory.toString())
            }
            val extractFile = validatePathIsSafe(outputDirectory, "asset-export.csv")

            // 2. Transform to the target metadata assets (limiting attributes as requested)
            val attributeLimits = ctx.config.attributesList
            val cmLimits = ctx.config.customMetadata.split("|")
            val includeOOTB = ctx.config.limitType == "INCLUDE"
            val includeCM = ctx.config.cmLimitType == "INCLUDE"
            val start = mutableListOf(Asset.QUALIFIED_NAME.atlanFieldName, Asset.TYPE_NAME.atlanFieldName)
            val defaultAttrsToExtract = AssetExporter.getAttributesToExtract(true, Exporter.getAllCustomMetadataFields(ctx.client))
            if (ctx.config.includeArchived) {
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
            val header =
                if (includeCM) {
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

            val transformedFile = "$outputDirectory${File.separator}transformed-file.csv"
            CsvWriter
                .builder()
                .fieldSeparator(fieldSeparator[0])
                .quoteCharacter('"')
                .quoteStrategy(QuoteStrategies.NON_EMPTY)
                .lineDelimiter(LineDelimiter.PLATFORM)
                .build(
                    Paths.get(transformedFile),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE,
                ).use { writer ->
                    writer.writeRecord(header.toList())

                    ctx.config.targetConnection.forEach { targetConnectionQN ->
                        val targetDatabaseNames = getTargetDatabaseName(ctx.client, targetConnectionQN, ctx.config.targetDatabasePattern)
                        targetDatabaseNames.forEach { targetDatabaseName ->
                            val mCtx =
                                MigratorContext(
                                    sourceConnectionQN = sourceConnectionQN,
                                    targetConnectionQN = targetConnectionQN,
                                    targetConnectionName = getConnectionName(ctx.client, targetConnectionQN),
                                    includeArchived = ctx.config.includeArchived,
                                    sourceDatabaseName = sourceDatabaseName,
                                    targetDatabaseName = targetDatabaseName,
                                )
                            val transformer =
                                Transformer(
                                    mCtx,
                                    extractFile.toString(),
                                    header.toList(),
                                    logger,
                                    fieldSeparator[0],
                                )
                            transformer.transform(writer)
                        }
                    }
                }
        }
    }

    @JvmStatic
    fun getDatabaseNames(
        client: AtlanClient,
        connectionQN: String,
        sourcePrefix: String,
    ): List<String> {
        val databaseNames =
            Database
                .select(client)
                .where(Asset.QUALIFIED_NAME.startsWith(connectionQN))
                .where(Asset.NAME.regex(sourcePrefix))
                .sort(Asset.NAME.order(SortOrder.Asc))
                .stream()
                .map { it.name }
                .toList()
        return databaseNames
    }

    @JvmStatic
    fun getConnectionName(
        client: AtlanClient,
        connectionQN: String,
    ): String {
        val connection =
            Connection
                .select(client)
                .where(Asset.QUALIFIED_NAME.eq(connectionQN))
                .stream()
                .findFirst()
                .getOrElse {
                    throw NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, connectionQN, "Connection")
                }
        return connection.name
    }

    @JvmStatic
    fun getTargetDatabaseName(
        client: AtlanClient,
        targetConnectionQN: String,
        targetDatabasePattern: String,
    ): List<String> {
        if (targetDatabasePattern.isBlank()) {
            return listOf("")
        }
        val databaseNames = getDatabaseNames(client, targetConnectionQN, targetDatabasePattern)
        if (databaseNames.isEmpty()) {
            throw InvalidRequestException(
                ErrorCode.UNEXPECTED_NUMBER_OF_DATABASES_FOUND,
                "at least one",
                targetDatabasePattern,
                "0",
            )
        }
        return databaseNames
    }

    @JvmStatic
    fun getSourceDatabaseNames(
        client: AtlanClient,
        targetDatabasePattern: String,
        sourceConnectionQN: String,
        sourcePrefix: String,
    ): String {
        if (targetDatabasePattern.isBlank()) {
            return ""
        }
        val sourceDatabaseNames = getDatabaseNames(client, sourceConnectionQN, sourcePrefix.split("/")[0])
        if (sourceDatabaseNames.size != 1) {
            throw InvalidRequestException(
                ErrorCode.UNEXPECTED_NUMBER_OF_DATABASES_FOUND,
                "only one",
                sourcePrefix,
                sourceDatabaseNames.size.toString(),
            )
        }
        return sourceDatabaseNames[0]
    }

    data class MigratorContext(
        val sourceConnectionQN: String,
        val targetConnectionQN: String,
        val targetConnectionName: String,
        val includeArchived: Boolean,
        val sourceDatabaseName: String,
        val targetDatabaseName: String,
    )
}
