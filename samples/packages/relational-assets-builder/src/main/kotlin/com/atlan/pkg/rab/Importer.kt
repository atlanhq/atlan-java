/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.Atlan
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.cache.TermCache
import com.atlan.pkg.rab.AssetImporter.Companion.getQualifiedNameDetails
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.serde.csv.RowPreprocessor
import mu.KotlinLogging
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<RelationalAssetsBuilderCfg>()
        import(config, outputDirectory)
    }

    fun import(
        config: RelationalAssetsBuilderCfg,
        outputDirectory: String = "tmp",
    ) {
        val batchSize = Utils.getOrDefault(config.assetsBatchSize, 20).toInt()
        val fieldSeparator = Utils.getOrDefault(config.assetsFieldSeparator, ",")[0]
        val assetsUpload = Utils.getOrDefault(config.importType, "DIRECT") == "DIRECT"
        val assetsFilename = Utils.getOrDefault(config.assetsFile, "")
        val assetsKey = Utils.getOrDefault(config.assetsKey, "")
        val assetAttrsToOverwrite =
            CSVImporter.attributesToClear(Utils.getOrDefault(config.assetsAttrToOverwrite, listOf()).toMutableList(), "assets", logger)
        val assetsFailOnErrors = Utils.getOrDefault(config.assetsFailOnErrors, true)
        val assetsSemantic = Utils.getCreationHandling(config.assetsUpsertSemantic, AssetCreationHandling.FULL)
        val trackBatches = Utils.getOrDefault(config.trackBatches, true)

        val assetsFileProvided =
            (
                assetsUpload && assetsFilename.isNotBlank()
            ) || (
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
                Utils.getOrDefault(config.assetsPrefix, ""),
                assetsKey,
            )
        val targetHeaders = CSVXformer.getHeader(assetsInput, fieldSeparator).toMutableList()
        // Inject two columns at the end that we need for column assets
        targetHeaders.add(Column.ORDER.atlanFieldName)
        targetHeaders.add(ColumnImporter.COLUMN_PARENT_QN)
        val revisedFile = Paths.get("$assetsInput.CSA_RAB.csv")
        val preprocessedDetails =
            Preprocessor(assetsInput, fieldSeparator)
                .preprocess<Results>(
                    outputFile = revisedFile.toString(),
                    outputHeaders = targetHeaders,
                )

        // Only cache links and terms if there are any in the CSV, otherwise this
        // will be unnecessary work
        if (preprocessedDetails.hasLinks) {
            LinkCache.preload()
        }
        if (preprocessedDetails.hasTermAssignments) {
            TermCache.preload()
        }

        ConnectionCache.preload()

        FieldSerde.FAIL_ON_ERRORS.set(assetsFailOnErrors)
        logger.info { "=== Importing assets... ===" }

        logger.info { " --- Importing connections... ---" }
        // Note: we force-track the batches here to ensure any created connections are cached
        // (without tracking, any connections created will NOT be cached, either, which will then cause issues
        // with the subsequent processing steps.)
        val connectionImporter =
            ConnectionImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                1,
                true,
                fieldSeparator,
            )
        connectionImporter.import()

        logger.info { " --- Importing databases... ---" }
        val databaseImporter =
            DatabaseImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                batchSize,
                connectionImporter,
                trackBatches,
                fieldSeparator,
            )
        val dbResults = databaseImporter.import()

        logger.info { " --- Importing schemas... ---" }
        val schemaImporter =
            SchemaImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                batchSize,
                connectionImporter,
                trackBatches,
                fieldSeparator,
            )
        val schResults = schemaImporter.import()

        logger.info { " --- Importing tables... ---" }
        val tableImporter =
            TableImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                batchSize,
                connectionImporter,
                trackBatches,
                fieldSeparator,
            )
        val tblResults = tableImporter.import()

        logger.info { " --- Importing views... ---" }
        val viewImporter =
            ViewImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                batchSize,
                connectionImporter,
                trackBatches,
                fieldSeparator,
            )
        val viewResults = viewImporter.import()

        logger.info { " --- Importing materialized views... ---" }
        val materializedViewImporter =
            MaterializedViewImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                batchSize,
                connectionImporter,
                trackBatches,
                fieldSeparator,
            )
        val mviewResults = materializedViewImporter.import()

        logger.info { " --- Importing columns... ---" }
        val columnImporter =
            ColumnImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                batchSize,
                connectionImporter,
                trackBatches,
                fieldSeparator,
            )
        val colResults = columnImporter.import()

        if (Atlan.getDefaultClient().isInternal && trackBatches) {
            // Only attempt to manage a connection cache if we are running in-cluster
            Utils.updateConnectionCache(
                added = ImportResults.getAllModifiedAssets(dbResults, schResults, tblResults, viewResults, mviewResults, colResults),
            )
        }
    }

    private class Preprocessor(
        originalFile: String,
        fieldSeparator: Char,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
        ) {
        val entityQualifiedNameToType = mutableMapOf<String, String>()
        val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToTableCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToViewCount = mutableMapOf<String, AtomicInteger>()

        var lastParentQN = ""
        var columnOrder = 1

        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            val values = row.toMutableList()
            val typeName = values[typeIdx]
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
                results.hasLinks,
                results.hasTermAssignments,
                results.outputFile!!,
                entityQualifiedNameToType,
                qualifiedNameToChildCount,
                qualifiedNameToTableCount,
                qualifiedNameToViewCount,
            )
        }
    }

    class Results(
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        val preprocessedFile: String,
        val entityQualifiedNameToType: Map<String, String>,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
        val qualifiedNameToTableCount: Map<String, AtomicInteger>,
        val qualifiedNameToViewCount: Map<String, AtomicInteger>,
    ) : RowPreprocessor.Results(
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            outputFile = preprocessedFile,
        )
}
