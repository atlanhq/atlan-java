/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.Atlan
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVImporter.Companion.attributesToClear
import com.atlan.pkg.serde.csv.ImportResults
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

    fun import(
        config: AssetImportCfg,
        outputDirectory: String = "tmp",
    ): ImportResults? {
        val assetsBatchSize = Utils.getOrDefault(config.assetsBatchSize, 20).toInt()
        val glossariesBatchSize = Utils.getOrDefault(config.glossariesBatchSize, 20).toInt()
        val dataProductsBatchSize = Utils.getOrDefault(config.dataProductsBatchSize, 20).toInt()
        val assetsFieldSeparator = Utils.getOrDefault(config.assetsFieldSeparator, ",")[0]
        val glossariesFieldSeparator = Utils.getOrDefault(config.glossariesFieldSeparator, ",")[0]
        val dataProductsFieldSeparator = Utils.getOrDefault(config.dataProductsFieldSeparator, ",")[0]
        val directUpload = Utils.getOrDefault(config.importType, "DIRECT") == "DIRECT"
        val assetsFilename = Utils.getOrDefault(config.assetsFile, "")
        val glossariesFilename = Utils.getOrDefault(config.glossariesFile, "")
        val dataProductsFilename = Utils.getOrDefault(config.dataProductsFile, "")
        val assetsKey = Utils.getOrDefault(config.assetsKey, "")
        val glossariesKey = Utils.getOrDefault(config.glossariesKey, "")
        val dataProductsKey = Utils.getOrDefault(config.dataProductsKey, "")
        val assetAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.assetsAttrToOverwrite, listOf()).toMutableList(), "assets", logger)
        val assetsFailOnErrors = Utils.getOrDefault(config.assetsFailOnErrors, true)
        val glossaryAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.glossariesAttrToOverwrite, listOf()).toMutableList(), "glossaries", logger)
        val dataProductAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.dataProductsAttrToOverwrite, listOf()).toMutableList(), "dataProducts", logger)
        val assetsUpdateSemantic = Utils.getCreationHandling(config.assetsUpsertSemantic, AssetCreationHandling.NONE)
        val assetsCaseSensitive = Utils.getOrDefault(config.assetsCaseSensitive, true)
        val assetsTableViewAgnostic = Utils.getOrDefault(config.assetsTableViewAgnostic, false)
        val glossariesUpdateOnly = Utils.getOrDefault(config.glossariesUpsertSemantic, "update") == "update"
        val glossariesFailOnErrors = Utils.getOrDefault(config.glossariesFailOnErrors, true)
        val dataProductsUpdateOnly = Utils.getOrDefault(config.dataProductsUpsertSemantic, "update") == "update"
        val dataProductsFailOnErrors = Utils.getOrDefault(config.dataProductsFailOnErrors, true)
        val trackBatches = Utils.getOrDefault(config.trackBatches, true)

        val assetsFileProvided = (directUpload && assetsFilename.isNotBlank()) || (!directUpload && assetsKey.isNotBlank())
        val glossariesFileProvided = (directUpload && glossariesFilename.isNotBlank()) || (!directUpload && glossariesKey.isNotBlank())
        val dataProductsFileProvided = (directUpload && dataProductsFilename.isNotBlank()) || (!directUpload && dataProductsKey.isNotBlank())
        if (!assetsFileProvided && !glossariesFileProvided && !dataProductsFileProvided) {
            logger.error { "No input file was provided for either data products, glossaries or assets." }
            exitProcess(1)
        }

        // Glossaries...
        val resultsGTC =
            if (glossariesFileProvided) {
                val glossariesInput =
                    Utils.getInputFile(
                        glossariesFilename,
                        outputDirectory,
                        directUpload,
                        Utils.getOrDefault(config.glossariesPrefix, ""),
                        glossariesKey,
                    )
                FieldSerde.FAIL_ON_ERRORS.set(glossariesFailOnErrors)
                logger.info { "=== Importing glossaries... ===" }
                val glossaryImporter =
                    GlossaryImporter(
                        glossariesInput,
                        glossaryAttrsToOverwrite,
                        glossariesUpdateOnly,
                        glossariesBatchSize,
                        glossariesFailOnErrors,
                        glossariesFieldSeparator,
                    )
                val includes = glossaryImporter.preprocess()
                if (includes.hasLinks) {
                    LinkCache.preload()
                }
                val resultsGlossary = glossaryImporter.import()
                logger.info { "=== Importing categories... ===" }
                val categoryImporter =
                    CategoryImporter(
                        glossariesInput,
                        glossaryAttrsToOverwrite,
                        glossariesUpdateOnly,
                        glossariesBatchSize,
                        glossariesFailOnErrors,
                        glossariesFieldSeparator,
                    )
                val resultsCategory = categoryImporter.import()
                logger.info { "=== Importing terms... ===" }
                val termImporter =
                    TermImporter(
                        glossariesInput,
                        glossaryAttrsToOverwrite,
                        glossariesUpdateOnly,
                        glossariesBatchSize,
                        glossariesFailOnErrors,
                        glossariesFieldSeparator,
                    )
                val resultsTerm = termImporter.import()
                resultsGlossary?.combinedWith(resultsCategory)?.combinedWith(resultsTerm)
            } else {
                null
            }

        val resultsAssets =
            if (assetsFileProvided) {
                val assetsInput =
                    Utils.getInputFile(
                        assetsFilename,
                        outputDirectory,
                        directUpload,
                        Utils.getOrDefault(config.assetsPrefix, ""),
                        assetsKey,
                    )
                FieldSerde.FAIL_ON_ERRORS.set(assetsFailOnErrors)
                logger.info { "=== Importing assets... ===" }
                val assetImporter =
                    AssetImporter(
                        assetsInput,
                        assetAttrsToOverwrite,
                        assetsUpdateSemantic == AssetCreationHandling.NONE,
                        assetsBatchSize,
                        assetsCaseSensitive,
                        assetsUpdateSemantic,
                        assetsTableViewAgnostic,
                        assetsFailOnErrors,
                        trackBatches,
                        assetsFieldSeparator,
                    )
                assetImporter.import()
            } else {
                null
            }

        // Data products...
        val resultsDDP =
            if (dataProductsFileProvided) {
                val dataProductsInput =
                    Utils.getInputFile(
                        dataProductsFilename,
                        outputDirectory,
                        directUpload,
                        Utils.getOrDefault(config.dataProductsPrefix, ""),
                        dataProductsKey,
                    )
                FieldSerde.FAIL_ON_ERRORS.set(dataProductsFailOnErrors)
                logger.info { "=== Importing domains... ===" }
                val domainImporter =
                    DomainImporter(
                        dataProductsInput,
                        dataProductAttrsToOverwrite,
                        dataProductsUpdateOnly,
                        dataProductsBatchSize,
                        dataProductsFailOnErrors,
                        dataProductsFieldSeparator,
                    )
                val resultsDomain = domainImporter.import()
                logger.info { "=== Importing products... ===" }
                val productImporter =
                    ProductImporter(
                        dataProductsInput,
                        dataProductAttrsToOverwrite,
                        dataProductsUpdateOnly,
                        dataProductsBatchSize,
                        dataProductsFailOnErrors,
                        dataProductsFieldSeparator,
                    )
                val resultsProduct = productImporter.import()
                resultsDomain?.combinedWith(resultsProduct)
            } else {
                null
            }

        if (Atlan.getDefaultClient().isInternal && trackBatches) {
            // Only attempt to manage a connection cache if we are running in-cluster
            Utils.updateConnectionCache(
                added = ImportResults.getAllModifiedAssets(resultsAssets),
            )
        }

        val resultsAssetsGTC = resultsGTC?.combinedWith(resultsAssets) ?: resultsAssets
        return resultsDDP?.combinedWith(resultsAssetsGTC) ?: resultsAssetsGTC
    }
}
