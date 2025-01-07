/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.rab.AssetImporter.Companion.getQualifiedNameDetails
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.DeltaProcessor
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = Utils.getLogger(Importer.javaClass.name)

    const val PREVIOUS_FILES_PREFIX = "csa-relational-assets-builder"

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<RelationalAssetsBuilderCfg>().use { ctx ->
            import(ctx, outputDirectory)
        }
    }

    /**
     * Actually run the import.
     *
     * @param ctx context in which this package is running
     * @param outputDirectory in which to do any data processing
     * @return the qualifiedName of the connection that was delta-processed, or null if no delta-processing enabled
     */
    fun import(
        ctx: PackageContext<RelationalAssetsBuilderCfg>,
        outputDirectory: String = "tmp",
    ): String? {
        val assetsUpload = ctx.config.importType == "DIRECT"
        val assetsFilename = ctx.config.assetsFile
        val assetsKey = ctx.config.assetsKey
        val fieldSeparator = ctx.config.assetsFieldSeparator[0]

        val assetsFileProvided =
            (
                assetsUpload && assetsFilename.isNotBlank()
            ) ||
                (
                    !assetsUpload && assetsKey.isNotBlank()
                )
        if (!assetsFileProvided) {
            logger.error { "No input file was provided for assets." }
            exitProcess(1)
        }

        // Preprocess the CSV file in an initial pass to inject key details,
        // to allow subsequent out-of-order parallel processing
        val assetsInput =
            Utils.getInputFile(
                assetsFilename,
                outputDirectory,
                assetsUpload,
                ctx.config.assetsPrefix,
                assetsKey,
            )
        val targetHeaders = CSVXformer.getHeader(assetsInput, fieldSeparator).toMutableList()
        // Inject two columns at the end that we need for column assets
        targetHeaders.add(Column.ORDER.atlanFieldName)
        targetHeaders.add(ColumnImporter.COLUMN_PARENT_QN)
        val revisedFile = Paths.get("$assetsInput.CSA_RAB.csv")
        val preprocessedDetails =
            Preprocessor(assetsInput, fieldSeparator, ctx.config.deltaSemantic == "full")
                .preprocess<Results>(
                    outputFile = revisedFile.toString(),
                    outputHeaders = targetHeaders,
                )

        // Only cache links and terms if there are any in the CSV, otherwise this
        // will be unnecessary work
        if (preprocessedDetails.hasLinks) {
            ctx.linkCache.preload()
        }
        if (preprocessedDetails.hasTermAssignments) {
            ctx.termCache.preload()
        }
        if (preprocessedDetails.hasDomainRelationship) {
            ctx.dataDomainCache.preload()
        }

        ctx.connectionCache.preload()

        FieldSerde.FAIL_ON_ERRORS.set(ctx.config.assetsFailOnErrors)
        logger.info { "=== Importing assets... ===" }

        logger.info { " --- Importing connections... ---" }
        // Note: we force-track the batches here to ensure any created connections are cached
        // (without tracking, any connections created will NOT be cached, either, which will then cause issues
        // with the subsequent processing steps.)
        // We also need to load these connections first, irrespective of any delta calculation, so that
        // we can be certain we will be able to resolve the cube's qualifiedName (for subsequent processing)
        val connectionImporter = ConnectionImporter(ctx, preprocessedDetails, logger)
        connectionImporter.import()?.close()

        val connectionQN =
            if (ctx.config.deltaSemantic == "full") {
                val connectionIdentity = ConnectionIdentity.fromString(preprocessedDetails.assetRootName)
                try {
                    val list = Connection.findByName(ctx.client, connectionIdentity.name, AtlanConnectorType.fromValue(connectionIdentity.type))
                    list[0].qualifiedName
                } catch (e: AtlanException) {
                    logger.error(e) { "Unable to find the unique connection in Atlan from the file: $connectionIdentity" }
                    exitProcess(50)
                }
            } else {
                null
            }

        val previousFileDirect = ctx.config.previousFileDirect
        DeltaProcessor(
            ctx = ctx,
            semantic = ctx.config.deltaSemantic,
            qualifiedNamePrefix = connectionQN,
            removalType = ctx.config.deltaRemovalType,
            previousFilesPrefix = PREVIOUS_FILES_PREFIX,
            resolver = AssetImporter,
            preprocessedDetails = preprocessedDetails,
            typesToRemove = listOf(Database.TYPE_NAME, Schema.TYPE_NAME, Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME, Column.TYPE_NAME),
            logger = logger,
            reloadSemantic = ctx.config.deltaReloadCalculation,
            previousFilePreprocessor =
                Preprocessor(
                    previousFileDirect,
                    fieldSeparator,
                    true,
                    outputFile = "$previousFileDirect.transformed.csv",
                    outputHeaders = targetHeaders,
                ),
            outputDirectory = outputDirectory,
        ).use { delta ->

            delta.calculate()

            logger.info { " --- Importing databases... ---" }
            val databaseImporter = DatabaseImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val dbResults = databaseImporter.import()

            logger.info { " --- Importing schemas... ---" }
            val schemaImporter = SchemaImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val schResults = schemaImporter.import()

            logger.info { " --- Importing tables... ---" }
            val tableImporter = TableImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val tblResults = tableImporter.import()

            logger.info { " --- Importing views... ---" }
            val viewImporter = ViewImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val viewResults = viewImporter.import()

            logger.info { " --- Importing materialized views... ---" }
            val materializedViewImporter = MaterializedViewImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val mviewResults = materializedViewImporter.import()

            logger.info { " --- Importing columns... ---" }
            val columnImporter = ColumnImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val colResults = columnImporter.import()

            delta.processDeletions()

            ImportResults.getAllModifiedAssets(ctx.client, true, dbResults, schResults, tblResults, viewResults, mviewResults, colResults).use { modifiedAssets ->
                delta.updateConnectionCache(modifiedAssets)
            }
        }
        return connectionQN
    }

    private class Preprocessor(
        originalFile: String,
        fieldSeparator: Char,
        val deltaProcessing: Boolean,
        outputFile: String? = null,
        outputHeaders: List<String>? = null,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
            producesFile = outputFile,
            usingHeaders = outputHeaders,
        ) {
        val entityQualifiedNameToType = mutableMapOf<String, String>()
        val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToTableCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToViewCount = mutableMapOf<String, AtomicInteger>()

        var connectionIdentity = ""
        var lastParentQN = ""
        var columnOrder = 1

        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            if (deltaProcessing) {
                val connectionNameOnRow = row.getOrNull(header.indexOf(Asset.CONNECTION_NAME.atlanFieldName)) ?: ""
                val connectionTypeOnRow = row.getOrNull(header.indexOf("connectorType")) ?: ""
                val connectionIdentityOnRow = ConnectionIdentity(connectionTypeOnRow, connectionNameOnRow).toString()
                if (connectionIdentity.isBlank()) {
                    connectionIdentity = connectionIdentityOnRow
                }
                if (connectionIdentity != connectionIdentityOnRow) {
                    logger.error { "Connection changed mid-file: $connectionIdentityOnRow -> $connectionIdentityOnRow" }
                    logger.error { "This package is designed to only process a single connection per input file when doing delta processing, exiting." }
                    exitProcess(101)
                }
            }

            val values = row.toMutableList()
            val typeName = CSVXformer.trimWhitespace(values.getOrElse(typeIdx) { "" })
            val qnDetails = getQualifiedNameDetails(values, header, typeName)
            if (typeName !in setOf(Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME)) {
                if (!qualifiedNameToChildCount.containsKey(qnDetails.parentUniqueQN)) {
                    qualifiedNameToChildCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                }
                qualifiedNameToChildCount[qnDetails.parentUniqueQN]?.incrementAndGet()
            }
            when (typeName) {
                Connection.TYPE_NAME, Database.TYPE_NAME, Schema.TYPE_NAME -> {
                    values.add("")
                    values.add("")
                }
                Table.TYPE_NAME -> {
                    if (!qualifiedNameToTableCount.containsKey(qnDetails.parentUniqueQN)) {
                        qualifiedNameToTableCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                    }
                    qualifiedNameToTableCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                    entityQualifiedNameToType[qnDetails.uniqueQN] = typeName
                    values.add("")
                    values.add("")
                }
                View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                    if (!qualifiedNameToViewCount.containsKey(qnDetails.parentUniqueQN)) {
                        qualifiedNameToViewCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                    }
                    qualifiedNameToViewCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                    entityQualifiedNameToType[qnDetails.uniqueQN] = typeName
                    values.add("")
                    values.add("")
                }
                Column.TYPE_NAME -> {
                    // If it is a column, calculate the order and parent qualifiedName and inject them
                    if (qnDetails.parentUniqueQN == lastParentQN) {
                        columnOrder += 1
                    } else {
                        lastParentQN = qnDetails.parentUniqueQN
                        columnOrder = 1
                    }
                    values.add("$columnOrder")
                    values.add(qnDetails.parentPartialQN)
                }
            }
            return values
        }

        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): Results {
            val results = super.finalize(header, outputFile)
            return Results(
                connectionIdentity,
                results.hasLinks,
                results.hasTermAssignments,
                results.hasDomainRelationship,
                results.outputFile!!,
                entityQualifiedNameToType,
                qualifiedNameToChildCount,
                qualifiedNameToTableCount,
                qualifiedNameToViewCount,
            )
        }
    }

    class Results(
        assetRootName: String,
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        hasDomainRelationship: Boolean,
        preprocessedFile: String,
        val entityQualifiedNameToType: Map<String, String>,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
        val qualifiedNameToTableCount: Map<String, AtomicInteger>,
        val qualifiedNameToViewCount: Map<String, AtomicInteger>,
    ) : DeltaProcessor.Results(
            assetRootName = assetRootName,
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            hasDomainRelationship = hasDomainRelationship,
            preprocessedFile = preprocessedFile,
        )

    data class ConnectionIdentity(
        val type: String,
        val name: String,
    ) {
        override fun toString(): String = "$type$DELIMITER$name"

        companion object {
            const val DELIMITER = "::"

            fun fromString(identity: String): ConnectionIdentity {
                val tokens = identity.split(DELIMITER)
                return ConnectionIdentity(tokens[0], tokens[1])
            }
        }
    }
}
