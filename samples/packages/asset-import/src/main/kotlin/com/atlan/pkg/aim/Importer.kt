/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVImporter.Companion.attributesToClear
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.util.AssetBatch.AssetCreationHandling
import mu.KotlinLogging
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
        val config = Utils.setPackageOps<AssetImportCfg>()
        import(config, outputDirectory)
    }

    fun import(config: AssetImportCfg, outputDirectory: String = "tmp"): ImportResults? {
        val batchSize = 20
        val defaultRegion = Utils.getEnvVar("AWS_S3_REGION")
        val defaultBucket = Utils.getEnvVar("AWS_S3_BUCKET_NAME")
        val assetsUpload = Utils.getOrDefault(config.assetsImportType, "UPLOAD") == "UPLOAD"
        val glossariesUpload = Utils.getOrDefault(config.glossariesImportType, "UPLOAD") == "UPLOAD"
        val assetsFilename = Utils.getOrDefault(config.assetsFile, "")
        val glossariesFilename = Utils.getOrDefault(config.glossariesFile, "")
        val assetsS3Region = Utils.getOrDefault(config.assetsS3Region, defaultRegion)
        val assetsS3Bucket = Utils.getOrDefault(config.assetsS3Bucket, defaultBucket)
        val assetsS3ObjectKey = Utils.getOrDefault(config.assetsS3ObjectKey, "")
        val glossariesS3Region = Utils.getOrDefault(config.glossariesS3Region, defaultRegion)
        val glossariesS3Bucket = Utils.getOrDefault(config.glossariesS3Bucket, defaultBucket)
        val glossariesS3ObjectKey = Utils.getOrDefault(config.glossariesS3ObjectKey, "")
        val assetAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.assetsAttrToOverwrite, listOf()).toMutableList(), "assets", logger)
        val assetsFailOnErrors = Utils.getOrDefault(config.assetsFailOnErrors, true)
        val glossaryAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.glossariesAttrToOverwrite, listOf()).toMutableList(), "glossaries", logger)
        val assetsUpdateSemantic = Utils.getOrDefault(config.assetsUpsertSemantic, "update")
        val assetsCaseSensitive = Utils.getOrDefault(config.assetsCaseSensitive, true)
        val assetsTableViewAgnostic = Utils.getOrDefault(config.assetsTableViewAgnostic, false)
        val glossariesUpdateOnly = Utils.getOrDefault(config.glossariesUpsertSemantic, "update") == "update"
        val glossariesFailOnErrors = Utils.getOrDefault(config.glossariesFailOnErrors, true)
        val trackBatches = Utils.getOrDefault(config.trackBatches, true)

        val assetsFileProvided = (assetsUpload && assetsFilename.isNotBlank()) || (!assetsUpload && assetsS3ObjectKey.isNotBlank())
        val glossariesFileProvided = (glossariesUpload && glossariesFilename.isNotBlank()) || (!glossariesUpload && glossariesS3ObjectKey.isNotBlank())
        if (!assetsFileProvided && !glossariesFileProvided) {
            logger.error { "No input file was provided for either glossaries or assets." }
            exitProcess(1)
        }

        LinkCache.preload()

        // Glossaries...
        val glossariesInput = Utils.getInputFile(
            glossariesFilename,
            glossariesS3Region,
            glossariesS3Bucket,
            glossariesS3ObjectKey,
            outputDirectory,
            glossariesUpload,
        )
        val resultsGTC = if (glossariesInput.isNotBlank()) {
            FieldSerde.FAIL_ON_ERRORS.set(glossariesFailOnErrors)
            logger.info { "=== Importing glossaries... ===" }
            val glossaryImporter =
                GlossaryImporter(glossariesInput, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize, trackBatches)
            val resultsGlossary = glossaryImporter.import()
            logger.info { "=== Importing categories... ===" }
            val categoryImporter =
                CategoryImporter(glossariesInput, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize, trackBatches)
            val resultsCategory = categoryImporter.import()
            logger.info { "=== Importing terms... ===" }
            val termImporter =
                TermImporter(glossariesInput, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize, trackBatches)
            val resultsTerm = termImporter.import()
            resultsGlossary?.combinedWith(resultsCategory)?.combinedWith(resultsTerm)
        } else {
            null
        }

        val assetsInput = Utils.getInputFile(
            assetsFilename,
            assetsS3Region,
            assetsS3Bucket,
            assetsS3ObjectKey,
            outputDirectory,
            assetsUpload,
        )
        val resultsAssets = if (assetsInput.isNotBlank()) {
            FieldSerde.FAIL_ON_ERRORS.set(assetsFailOnErrors)
            logger.info { "=== Importing assets... ===" }
            val creationHandling = when (assetsUpdateSemantic) {
                "upsert" -> AssetCreationHandling.FULL
                "partial" -> AssetCreationHandling.PARTIAL
                else -> AssetCreationHandling.NONE
            }
            val assetImporter = AssetImporter(
                assetsInput,
                assetAttrsToOverwrite,
                creationHandling == AssetCreationHandling.NONE,
                batchSize,
                assetsCaseSensitive,
                creationHandling,
                assetsTableViewAgnostic,
                assetsFailOnErrors,
                trackBatches,
            )
            assetImporter.import()
        } else {
            null
        }

        return resultsGTC?.combinedWith(resultsAssets) ?: resultsAssets
    }
}
