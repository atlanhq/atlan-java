/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
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
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.writer.CsvWriter
import mu.KotlinLogging
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
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

    fun import(config: RelationalAssetsBuilderCfg, outputDirectory: String = "tmp") {
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

        val assetsFileProvided = (
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
        val assetsInput = Utils.getInputFile(
            assetsFilename,
            outputDirectory,
            assetsUpload,
            Utils.getOrDefault(config.assetsPrefix, ""),
            assetsKey,
        )
        val preprocessedDetails = preprocessCSV(assetsInput, fieldSeparator)

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
        val connectionImporter = ConnectionImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
            1,
            true,
            fieldSeparator,
        )
        connectionImporter.import()

        logger.info { " --- Importing databases... ---" }
        val databaseImporter = DatabaseImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        databaseImporter.import()

        logger.info { " --- Importing schemas... ---" }
        val schemaImporter = SchemaImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        schemaImporter.import()

        logger.info { " --- Importing tables... ---" }
        val tableImporter = TableImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        tableImporter.import()

        logger.info { " --- Importing views... ---" }
        val viewImporter = ViewImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        viewImporter.import()

        logger.info { " --- Importing materialized views... ---" }
        val materializedViewImporter = MaterializedViewImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        materializedViewImporter.import()

        logger.info { " --- Importing columns... ---" }
        val columnImporter = ColumnImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        columnImporter.import()
    }

    private fun preprocessCSV(originalFile: String, fieldSeparator: Char): PreprocessedCsv {
        // Setup
        val quoteCharacter = '"'
        val inputFile = Paths.get(originalFile)
        val revisedFile = Paths.get("$originalFile.CSA_RAB.csv")

        // Open the CSV reader and writer
        val reader = CsvReader.builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter(quoteCharacter)
            .skipEmptyLines(true)
            .ignoreDifferentFieldCount(false)
        val writer = CsvWriter.builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter(quoteCharacter)
            .build(revisedFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)

        // Start processing...
        reader.ofCsvRecord(inputFile).use { tmp ->
            var hasLinks = false
            var hasTermAssignments = false
            val entityQualifiedNameToType = mutableMapOf<String, String>()
            val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
            val qualifiedNameToTableCount = mutableMapOf<String, AtomicInteger>()
            val qualifiedNameToViewCount = mutableMapOf<String, AtomicInteger>()
            var header: MutableList<String> = mutableListOf()
            var typeIdx = 0
            var lastParentQN = ""
            var columnOrder = 1
            tmp.stream().forEach { row ->
                if (row.startingLineNumber == 1L) {
                    header = row.fields.toMutableList()
                    // Inject two columns at the end that we need for column assets
                    header.add(Column.ORDER.atlanFieldName)
                    header.add(ColumnImporter.COLUMN_PARENT_QN)
                    if (header.contains(Asset.LINKS.atlanFieldName)) {
                        hasLinks = true
                    }
                    if (header.contains("assignedTerms")) {
                        hasTermAssignments = true
                    }
                    typeIdx = header.indexOf(Asset.TYPE_NAME.atlanFieldName)
                    writer.writeRecord(header)
                } else {
                    val values = row.fields.toMutableList()
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
                    writer.writeRecord(values)
                }
            }
            writer.close()
            return PreprocessedCsv(
                hasLinks,
                hasTermAssignments,
                revisedFile.toString(),
                entityQualifiedNameToType,
                qualifiedNameToChildCount,
                qualifiedNameToTableCount,
                qualifiedNameToViewCount,
            )
        }
    }

    data class PreprocessedCsv(
        val hasLinks: Boolean,
        val hasTermAssignments: Boolean,
        val preprocessedFile: String,
        val entityQualifiedNameToType: Map<String, String>,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
        val qualifiedNameToTableCount: Map<String, AtomicInteger>,
        val qualifiedNameToViewCount: Map<String, AtomicInteger>,
    )
}
