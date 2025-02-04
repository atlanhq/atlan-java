/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVWriter
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.DeltaProcessor
import java.io.File
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = Utils.getLogger(this.javaClass.name)

    const val PREVIOUS_FILES_PREFIX = "csa-asset-import"

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

        // 1. Glossaries -- everything else can be linked to terms
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

        // 2. Data products -- since all other assets can now be direct-linked to domains (and products are only a DSL)
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

        // 3. Assets (last) -- since these may be related to the other objects loaded above
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
                val previousFileDirect = ctx.config.assetsPreviousFileDirect
                val preprocessedDetails =
                    AssetImporter
                        .Preprocessor(assetsInput, ctx.config.assetsFieldSeparator[0], logger)
                        .preprocess<AssetImporter.Results>()
                if (preprocessedDetails.hasLinks) {
                    ctx.linkCache.preload()
                }
                DeltaProcessor(
                    ctx = ctx,
                    semantic = ctx.config.assetsDeltaSemantic,
                    qualifiedNamePrefix = preprocessedDetails.assetRootName,
                    removalType = ctx.config.assetsDeltaRemovalType,
                    previousFilesPrefix = ctx.config.assetsPreviousFilePrefix.ifBlank { PREVIOUS_FILES_PREFIX },
                    resolver = AssetImporter,
                    preprocessedDetails = preprocessedDetails,
                    typesToRemove = emptyList(),
                    logger = logger,
                    reloadSemantic = ctx.config.assetsDeltaReloadCalculation,
                    previousFilePreprocessor =
                        AssetImporter.Preprocessor(
                            previousFileDirect,
                            ctx.config.assetsFieldSeparator[0],
                            logger,
                        ),
                    outputDirectory = outputDirectory,
                ).use { delta ->

                    delta.calculate()

                    logger.info { "=== Importing assets... ===" }
                    val assetImporter = AssetImporter(ctx, delta, assetsInput, logger)
                    assetImporter.preprocess() // Note: we still do this to detect any cyclical relationships
                    val importedAssets = assetImporter.import()

                    delta.processDeletions()

                    // Note: we won't close the original set of changes here, as we'll combine it later for a full set of changes
                    // (at which point, it will be closed)
                    ImportResults.getAllModifiedAssets(ctx.client, false, importedAssets).use { modifiedAssets ->
                        delta.updateConnectionCache(modifiedAssets)
                    }
                    importedAssets
                }
            } else {
                null
            }
        val results = ImportResults.combineAll(ctx.client, true, resultsGTC, resultsDDP, resultsAssets)
        CSVWriter("$outputDirectory${File.separator}overall-stats.csv").use { csv ->
            csv.writeHeader(
                listOf(
                    "Assets",
                    "Count",
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Created",
                    "Count" to (results?.primary?.numCreated ?: 0),
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Updated",
                    "Count" to (results?.primary?.numUpdated ?: 0),
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Restored",
                    "Count" to (results?.primary?.numRestored ?: 0),
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Skipped",
                    "Count" to (results?.primary?.skipped?.size ?: 0),
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Created (related)",
                    "Count" to (results?.related?.numCreated ?: 0),
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Updated (related)",
                    "Count" to (results?.related?.numUpdated ?: 0),
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Restored (related)",
                    "Count" to (results?.related?.numRestored ?: 0),
                ),
            )
            csv.writeRecord(
                mapOf(
                    "Assets" to "Skipped (related)",
                    "Count" to (results?.related?.skipped?.size ?: 0),
                ),
            )
        }
        CSVWriter("$outputDirectory${File.separator}failed-assets.csv").use { csv ->
            csv.writeHeader(
                listOf(
                    "Batch ID",
                    "Asset type",
                    "Qualified name",
                    "Loaded as",
                    "Failure reason",
                ),
            )
            results?.primary?.failed?.entrySet()?.forEach { entry ->
                val batchId = entry.key
                val failedBatch = entry.value
                failedBatch.failedAssets.forEach { asset ->
                    csv.writeRecord(
                        mapOf(
                            "Batch ID" to batchId,
                            "Asset type" to asset.typeName,
                            "Qualified name" to asset.qualifiedName,
                            "Loaded as" to "primary",
                            "Failure reason" to failedBatch.failureReason.toString(),
                        ),
                    )
                }
            }
            results?.related?.failed?.entrySet()?.forEach { entry ->
                val batchId = entry.key
                val failedBatch = entry.value
                failedBatch.failedAssets.forEach { asset ->
                    csv.writeRecord(
                        mapOf(
                            "Batch ID" to batchId,
                            "Asset type" to asset.typeName,
                            "Qualified name" to asset.qualifiedName,
                            "Loaded as" to "related",
                            "Failure reason" to failedBatch.failureReason.toString(),
                        ),
                    )
                }
            }
        }
        CSVWriter("$outputDirectory${File.separator}skipped-assets.csv").use { csv ->
            csv.writeHeader(
                listOf(
                    "Asset type",
                    "Qualified name",
                    "Loaded as",
                ),
            )
            results?.primary?.skipped?.entrySet()?.forEach { entry ->
                val asset = entry.value
                csv.writeRecord(
                    mapOf(
                        "Asset type" to asset.typeName,
                        "Qualified name" to asset.qualifiedName,
                        "Loaded as" to "primary",
                    ),
                )
            }
            results?.related?.skipped?.entrySet()?.forEach { entry ->
                val asset = entry.value
                csv.writeRecord(
                    mapOf(
                        "Asset type" to asset.typeName,
                        "Qualified name" to asset.qualifiedName,
                        "Loaded as" to "related",
                    ),
                )
            }
        }
        return results
    }
}
