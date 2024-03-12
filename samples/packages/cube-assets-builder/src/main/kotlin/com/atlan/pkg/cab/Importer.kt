/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.pkg.Utils
import com.atlan.pkg.cab.AssetImporter.Companion.getQualifiedNameDetails
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.cache.TermCache
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
        val config = Utils.setPackageOps<CubeAssetsBuilderCfg>()
        import(config, outputDirectory)
    }

    fun import(config: CubeAssetsBuilderCfg, outputDirectory: String = "tmp") {
        val batchSize = Utils.getOrDefault(config.assetsBatchSize, 20).toInt()
        val fieldSeparator = Utils.getOrDefault(config.assetsFieldSeparator, ",")[0]
        val defaultRegion = Utils.getEnvVar("AWS_S3_REGION")
        val defaultBucket = Utils.getEnvVar("AWS_S3_BUCKET_NAME")
        val assetsUpload = Utils.getOrDefault(config.assetsImportType, "UPLOAD") == "UPLOAD"
        val assetsFilename = Utils.getOrDefault(config.assetsFile, "")
        val assetsS3Region = Utils.getOrDefault(config.assetsS3Region, defaultRegion)
        val assetsS3Bucket = Utils.getOrDefault(config.assetsS3Bucket, defaultBucket)
        val assetsS3ObjectKey = Utils.getOrDefault(config.assetsS3ObjectKey, "")
        val assetAttrsToOverwrite =
            CSVImporter.attributesToClear(Utils.getOrDefault(config.assetsAttrToOverwrite, listOf()).toMutableList(), "assets", logger)
        val assetsFailOnErrors = Utils.getOrDefault(config.assetsFailOnErrors, true)
        val assetsUpdateOnly = Utils.getOrDefault(config.assetsUpsertSemantic, "update") == "update"
        val trackBatches = Utils.getOrDefault(config.trackBatches, true)

        val assetsFileProvided = (assetsUpload && assetsFilename.isNotBlank()) || (!assetsUpload && assetsS3ObjectKey.isNotBlank())
        if (!assetsFileProvided) {
            logger.error { "No input file was provided for assets." }
            exitProcess(1)
        }

        // Preprocess the CSV file in an initial pass to inject key details,
        // to allow subsequent out-of-order parallel processing
        val assetsInput = Utils.getInputFile(
            assetsFilename,
            assetsS3Region,
            assetsS3Bucket,
            assetsS3ObjectKey,
            outputDirectory,
            assetsUpload,
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
            assetsUpdateOnly,
            1,
            true,
            fieldSeparator,
        )
        connectionImporter.import()

        logger.info { " --- Importing cubes... ---" }
        val cubeImporter = CubeImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsUpdateOnly,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        cubeImporter.import()

        logger.info { " --- Importing dimensions... ---" }
        val dimensionImporter = DimensionImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsUpdateOnly,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        dimensionImporter.import()

        logger.info { " --- Importing hierarchies... ---" }
        val hierarchyImporter = HierarchyImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsUpdateOnly,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        hierarchyImporter.import()

        logger.info { " --- Importing fields... ---" }
        val fieldImporter = FieldImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsUpdateOnly,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        fieldImporter.import()
    }

    private fun preprocessCSV(originalFile: String, fieldSeparator: Char): PreprocessedCsv {
        // Setup
        val quoteCharacter = '"'
        val inputFile = Paths.get(originalFile)
        val revisedFile = Paths.get("$originalFile.CSA_CAB.csv")

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
            val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
            var header: MutableList<String> = mutableListOf()
            var typeIdx = 0
            tmp.stream().forEach { row ->
                if (row.startingLineNumber == 1L) {
                    header = row.fields.toMutableList()
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
                    if (!qualifiedNameToChildCount.containsKey(qnDetails.parentUniqueQN)) {
                        qualifiedNameToChildCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                    }
                    qualifiedNameToChildCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                    writer.writeRecord(values)
                }
            }
            writer.close()
            return PreprocessedCsv(
                hasLinks,
                hasTermAssignments,
                revisedFile.toString(),
                qualifiedNameToChildCount,
            )
        }
    }

    data class PreprocessedCsv(
        val hasLinks: Boolean,
        val hasTermAssignments: Boolean,
        val preprocessedFile: String,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
    )
}
