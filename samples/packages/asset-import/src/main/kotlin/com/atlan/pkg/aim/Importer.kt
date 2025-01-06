/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.ImportResults
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = Utils.getLogger(this.javaClass.name)

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<AssetImportCfg>().use { ctx ->
            import(ctx, outputDirectory)?.close()
        }
    }

    fun import(
        ctx: PackageContext<AssetImportCfg>,
        outputDirectory: String = "tmp",
    ): ImportResults? {
        val assetsFileProvided = Utils.isFileProvided(ctx.config.importType, ctx.config.assetsFile, ctx.config.assetsKey)
        val glossariesFileProvided = Utils.isFileProvided(ctx.config.importType, ctx.config.glossariesFile, ctx.config.glossariesKey)
        val dataProductsFileProvided = Utils.isFileProvided(ctx.config.importType, ctx.config.dataProductsFile, ctx.config.dataProductsKey)
        if (!assetsFileProvided && !glossariesFileProvided && !dataProductsFileProvided) {
            logger.error { "No input file was provided for either data products, glossaries or assets." }
            exitProcess(1)
        }

        // Glossaries...
        val resultsGTC =
            if (glossariesFileProvided) {
                val glossariesInput =
                    Utils.getInputFile(
                        ctx.config.glossariesFile,
                        outputDirectory,
                        ctx.config.importType == "DIRECT",
                        ctx.config.glossariesPrefix,
                        ctx.config.glossariesKey,
                    )
                FieldSerde.FAIL_ON_ERRORS.set(ctx.config.glossariesFailOnErrors)
                logger.info { "=== Importing glossaries... ===" }
                val glossaryImporter = GlossaryImporter(ctx, glossariesInput, logger)
                val includes = glossaryImporter.preprocess()
                if (includes.hasLinks) {
                    ctx.linkCache.preload()
                }
                val resultsGlossary = glossaryImporter.import()
                logger.info { "=== Importing categories... ===" }
                val categoryImporter = CategoryImporter(ctx, glossariesInput, logger)
                val resultsCategory = categoryImporter.import()
                logger.info { "=== Importing terms... ===" }
                val termImporter = TermImporter(ctx, glossariesInput, logger)
                val resultsTerm = termImporter.import()
                ImportResults.combineAll(ctx.client, true, resultsGlossary, resultsCategory, resultsTerm)
            } else {
                null
            }

        val resultsAssets =
            if (assetsFileProvided) {
                val assetsInput =
                    Utils.getInputFile(
                        ctx.config.assetsFile,
                        outputDirectory,
                        ctx.config.importType == "DIRECT",
                        ctx.config.assetsPrefix,
                        ctx.config.assetsKey,
                    )
                FieldSerde.FAIL_ON_ERRORS.set(ctx.config.assetsFailOnErrors)
                logger.info { "=== Importing assets... ===" }
                val assetImporter = AssetImporter(ctx, assetsInput, logger)
                val includes = assetImporter.preprocess()
                if (includes.hasLinks) {
                    ctx.linkCache.preload()
                }
                if (includes.hasDomainRelationship) {
                    ctx.dataDomainCache.preload()
                }
                assetImporter.import()
            } else {
                null
            }

        // Data products...
        val resultsDDP =
            if (dataProductsFileProvided) {
                val dataProductsInput =
                    Utils.getInputFile(
                        ctx.config.dataProductsFile,
                        outputDirectory,
                        ctx.config.importType == "DIRECT",
                        ctx.config.dataProductsPrefix,
                        ctx.config.dataProductsKey,
                    )
                FieldSerde.FAIL_ON_ERRORS.set(ctx.config.dataProductsFailOnErrors)
                logger.info { "=== Importing domains... ===" }
                val domainImporter = DomainImporter(ctx, dataProductsInput, logger)
                if (domainImporter.preprocess().hasLinks) {
                    ctx.linkCache.preload()
                }
                val resultsDomain = domainImporter.import()
                logger.info { "=== Importing products... ===" }
                val productImporter = ProductImporter(ctx, dataProductsInput, logger)
                if (productImporter.preprocess().hasLinks) {
                    ctx.linkCache.preload()
                }
                val resultsProduct = productImporter.import()
                ImportResults.combineAll(ctx.client, true, resultsDomain, resultsProduct)
            } else {
                null
            }

        ImportResults.getAllModifiedAssets(ctx.client, false, resultsAssets).use { allModified ->
            Utils.updateConnectionCache(
                client = ctx.client,
                added = allModified,
                fallback = outputDirectory,
            )
        }
        return ImportResults.combineAll(ctx.client, true, resultsGTC, resultsDDP, resultsAssets)
    }
}
