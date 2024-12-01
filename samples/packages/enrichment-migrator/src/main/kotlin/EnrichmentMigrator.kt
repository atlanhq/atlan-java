/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import EnrichmentMigratorXform.getSourceDatabaseNames
import EnrichmentMigratorXform.getTargetDatabaseName
import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.exception.ErrorCode
import com.atlan.exception.InvalidRequestException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.RowSerde
import mu.KotlinLogging
import sun.tools.jconsole.ProxyClient.getConnectionName
import java.io.File
import kotlin.jvm.optionals.getOrElse

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
        Utils.initializeContext(config).use { ctx ->

            val sourceConnectionQN = ctx.config.sourceConnection!![0]
            val sourcePrefix = ctx.config.sourceQnPrefix!!
            val sourceDatabaseName = getSourceDatabaseNames(ctx.config.targetDatabasePattern!!, sourceConnectionQN, sourcePrefix)
            val sourceQN =
                if (sourcePrefix.isBlank()) {
                    sourceConnectionQN
                } else {
                    "$sourceConnectionQN/$sourcePrefix"
                }

            // 1. Extract the enriched metadata
            val extractConfig =
                AssetExportBasicCfg(
                    exportScope = "ENRICHED_ONLY",
                    qnPrefix = sourceQN,
                    includeGlossaries = false,
                    includeArchived = ctx.config.includeArchived!!,
                )
            Utils.initializeContext(extractConfig, ctx.client).use { eCtx ->
                Exporter.export(eCtx, outputDirectory)
            }
            val extractFile = "$outputDirectory${File.separator}asset-export.csv"

            // 2. Transform to the target metadata assets (limiting attributes as requested)
            val attributeLimits = Utils.getOrDefault(config.attributesList, listOf())
            val cmLimits = Utils.getOrDefault(config.customMetadata, "").split("|")
            val includeOOTB = Utils.getOrDefault(config.limitType, "EXCLUDE") == "INCLUDE"
            val includeCM = Utils.getOrDefault(config.cmLimitType, "EXCLUDE") == "INCLUDE"
            val start = mutableListOf(Asset.QUALIFIED_NAME.atlanFieldName, Asset.TYPE_NAME.atlanFieldName)
            val defaultAttrsToExtract = AssetExporter.getAttributesToExtract(true, Exporter.getAllCustomMetadataFields(ctx.client))
            if (ctx.config.includeArchived!!) {
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
            val assetsFailOnErrors = Utils.getOrDefault(config.failOnErrors, true)
            ctx.config.targetConnection!!.forEach { targetConnectionQN ->
                val targetDatabaseNames = getTargetDatabaseName(targetConnectionQN, ctx.config.targetDatabasePattern!!)
                targetDatabaseNames.forEach { targetDatabaseName ->
                    val mCtx =
                        MigratorContext(
                            sourceConnectionQN = sourceConnectionQN,
                            targetConnectionQN = targetConnectionQN,
                            targetConnectionName = getConnectionName(targetConnectionQN),
                            includeArchived = ctx.config.includeArchived!!,
                            sourceDatabaseName = sourceDatabaseName,
                            targetDatabaseName = targetDatabaseName,
                        )
                    val targetConnectionFilename =
                        if (targetDatabaseName.isNotBlank()) {
                            "${targetConnectionQN}_$targetDatabaseName".replace("/", "_")
                        } else {
                            targetConnectionQN.replace("/", "_")
                        }
                    val transformedFile =
                        "$outputDirectory${File.separator}CSA_EM_transformed_$targetConnectionFilename.csv"
                    val transformer =
                        Transformer(
                            mCtx,
                            extractFile,
                            header.toList(),
                            logger,
                            ctx.config.fieldSeparator!![0],
                        )
                    transformer.transform(transformedFile)

                    // 3. Import the transformed file
                    val importConfig =
                        AssetImportCfg(
                            assetsFile = transformedFile,
                            assetsUpsertSemantic = "update",
                            assetsFailOnErrors = assetsFailOnErrors,
                            assetsBatchSize = ctx.config.batchSize!!,
                            assetsFieldSeparator = ctx.config.fieldSeparator!!,
                            assetsCaseSensitive = ctx.config.caseSensitive!!,
                            assetsTableViewAgnostic = ctx.config.tableViewAgnostic!!,
                        )
                    Utils.initializeContext(importConfig, ctx.client).use { iCtx ->
                        Importer.import(iCtx, outputDirectory)?.close()
                    }
                }
            }
        }
    }

    @JvmStatic
    fun getDatabaseNames(
        connectionQN: String,
        sourcePrefix: String,
    ): List<String> {
        val databaseNames =
            Database.select()
                .where(Asset.QUALIFIED_NAME.startsWith(connectionQN))
                .where(Asset.NAME.regex(sourcePrefix))
                .sort(Asset.NAME.order(SortOrder.Asc))
                .stream()
                .map { it.name }
                .toList()
        return databaseNames
    }

    @JvmStatic
    fun getConnectionName(connectionQN: String): String {
        val connection =
            Connection.select()
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
        targetConnectionQN: String,
        targetDatabasePattern: String,
    ): List<String> {
        if (targetDatabasePattern.isBlank()) {
            return listOf("")
        }
        val databaseNames = getDatabaseNames(targetConnectionQN, targetDatabasePattern)
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
        targetDatabasePattern: String,
        sourceConnectionQN: String,
        sourcePrefix: String,
    ): String {
        if (targetDatabasePattern.isBlank()) {
            return ""
        }
        val sourceDatabaseNames = getDatabaseNames(sourceConnectionQN, sourcePrefix.split("/")[0])
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
