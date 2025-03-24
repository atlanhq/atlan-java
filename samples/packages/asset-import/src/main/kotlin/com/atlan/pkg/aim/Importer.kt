/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.cache.OffHeapAssetCache
import com.atlan.cache.OffHeapFailureCache
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
        val tagFileProvided = Utils.isFileProvided(ctx.config.importType, ctx.config.tagsFile, ctx.config.tagsKey)
        if (!assetsFileProvided && !glossariesFileProvided && !dataProductsFileProvided && !tagFileProvided) {
            logger.error { "No input file was provided for either data products, glossaries, assets or tags." }
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
                if (ctx.config.glossariesFieldSeparator.length > 1) {
                    logger.error { "Field separator must be only a single character. The provided value is too long: ${ctx.config.glossariesFieldSeparator}" }
                    exitProcess(2)
                }
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
        if (resultsGTC?.anyFailures == true && ctx.config.glossariesFailOnErrors) {
            logger.error { "Some errors detected while loading glossaries, failing the workflow." }
            createResultsFile(outputDirectory, resultsGTC)
            resultsGTC.close()
            exitProcess(1)
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
                if (ctx.config.dataProductsFieldSeparator.length > 1) {
                    logger.error { "Field separator must be only a single character. The provided value is too long: ${ctx.config.dataProductsFieldSeparator}" }
                    exitProcess(2)
                }
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
        if (resultsDDP?.anyFailures == true && ctx.config.glossariesFailOnErrors) {
            logger.error { "Some errors detected while loading data products, failing the workflow." }
            ImportResults.combineAll(ctx.client, true, resultsGTC, resultsDDP).use { combined ->
                createResultsFile(outputDirectory, combined)
            }
            exitProcess(2)
        }

        val assetsInput =
            if (assetsFileProvided) {
                if (ctx.config.assetsFieldSeparator.length > 1) {
                    logger.error { "Field separator must be only a single character. The provided value is too long: ${ctx.config.assetsFieldSeparator}" }
                    exitProcess(2)
                }
                Utils.getInputFile(
                    ctx.config.assetsFile,
                    outputDirectory,
                    ctx.config.importType == "DIRECT",
                    ctx.config.assetsPrefix,
                    ctx.config.assetsKey,
                )
            } else {
                null
            }

        // 3. Connections (since they must first exist for source-synced tags, but could themselves have terms and / or domains assigned)
        val resultsConnections =
            if (assetsInput != null) {
                FieldSerde.FAIL_ON_ERRORS.set(ctx.config.assetsFailOnErrors)
                logger.info { " === Importing connections... ===" }
                // Note: we force-track the batches here to ensure any created connections are cached
                // (without tracking, any connections created will NOT be cached, either, which will then cause issues
                // with the subsequent processing steps.)
                // We also need to load these connections first, irrespective of any delta calculation, so that
                // we can be certain we will be able to resolve the assets' qualifiedNames (for subsequent processing)
                val connectionImporter = ConnectionImporter(ctx, assetsInput, logger)
                connectionImporter.import()
            } else {
                null
            }

        // 4. Tag definitions
        if (tagFileProvided) {
            val tagsInput =
                Utils.getInputFile(
                    ctx.config.tagsFile,
                    outputDirectory,
                    ctx.config.importType == "DIRECT",
                    ctx.config.tagsPrefix,
                    ctx.config.tagsKey,
                )
            FieldSerde.FAIL_ON_ERRORS.set(ctx.config.tagsFailOnErrors)
            if (ctx.config.tagsFieldSeparator.length > 1) {
                logger.error { "Field separator must be only a single character. The provided value is too long: ${ctx.config.tagsFieldSeparator}" }
                exitProcess(2)
            }
            logger.info { "=== Importing tag definitions... ===" }
            val tagImporter = AtlanTagImporter(ctx, tagsInput, logger)
            tagImporter.import()
        }

        // 5. Assets (last) -- since these may be related to the other objects loaded above
        val deletedAssets = OffHeapAssetCache(ctx.client, "deleted")
        val resultsAssets =
            if (assetsInput != null) {
                FieldSerde.FAIL_ON_ERRORS.set(ctx.config.assetsFailOnErrors)
                val previousFileDirect = ctx.config.assetsPreviousFileDirect
                val preprocessedDetails =
                    AssetImporter
                        .Preprocessor(assetsInput, ctx.config.assetsFieldSeparator[0], logger)
                        .preprocess<AssetImporter.Results>()
                if (preprocessedDetails.hasLinks) {
                    ctx.linkCache.preload()
                }
                if (preprocessedDetails.hasDomainRelationship) {
                    ctx.dataDomainCache.preload()
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
                    deletedAssets.putAll(delta.deletedAssets)

                    // Note: we won't close the original set of changes here, as we'll combine it later for a full set of changes
                    // (at which point, it will be closed)
                    ImportResults.getAllModifiedAssets(ctx.client, false, importedAssets).use { modifiedAssets ->
                        delta.updateConnectionCache(modifiedAssets)
                    }
                    delta.uploadStateToBackingStore()
                    importedAssets
                }
            } else {
                null
            }
        val results = ImportResults.combineAll(ctx.client, true, resultsGTC, resultsDDP, resultsConnections, resultsAssets)
        createResultsFile(outputDirectory, results, deletedAssets)
        deletedAssets.close()
        if (results?.anyFailures == true && ctx.config.assetsFailOnErrors) {
            logger.error { "Some errors detected while loading assets, failing the workflow." }
            results.close()
            exitProcess(3)
        }
        return results
    }

    private fun createResultsFile(
        outputDirectory: String,
        results: ImportResults?,
        deletedAssets: OffHeapAssetCache? = null,
    ) {
        CSVWriter("$outputDirectory${File.separator}results.csv").use { csv ->
            csv.writeHeader(
                listOf(
                    "Action",
                    "Asset type",
                    "Qualified name",
                    "Asset name",
                    "Loaded as",
                    "Failure reason",
                    "Batch ID",
                    "Asset GUID",
                ),
            )
            addFailures(csv, results?.primary?.failed, "primary")
            addFailures(csv, results?.related?.failed, "related")
            addResults(csv, results?.primary?.skipped, "skipped", "primary")
            addResults(csv, results?.related?.skipped, "skipped", "related")
            addResults(csv, results?.primary?.created, "created", "primary")
            addResults(csv, results?.related?.created, "created", "related")
            addResults(csv, results?.primary?.updated, "updated", "primary")
            addResults(csv, results?.related?.updated, "updated", "related")
            addResults(csv, results?.primary?.restored, "restored", "primary")
            addResults(csv, results?.related?.restored, "restored", "related")
            addResults(csv, deletedAssets, "deleted", "")
        }
    }

    private fun addResults(
        csv: CSVWriter,
        cache: OffHeapAssetCache?,
        action: String,
        loadedAs: String,
    ) {
        cache?.entrySet()?.forEach { entry ->
            val asset = entry.value
            csv.writeRecord(
                mapOf(
                    "Action" to action,
                    "Asset type" to asset.typeName,
                    "Qualified name" to asset.qualifiedName,
                    "Loaded as" to loadedAs,
                    "Asset GUID" to asset.guid,
                    "Asset name" to (asset.name ?: ""),
                ),
            )
        }
    }

    private fun addFailures(
        csv: CSVWriter,
        cache: OffHeapFailureCache?,
        loadedAs: String,
    ) {
        cache?.entrySet()?.forEach { entry ->
            val batchId = entry.key
            val failedBatch = entry.value
            failedBatch.failedAssets.forEach { asset ->
                csv.writeRecord(
                    mapOf(
                        "Action" to "failed",
                        "Batch ID" to batchId,
                        "Asset type" to asset.typeName,
                        "Qualified name" to asset.qualifiedName,
                        "Loaded as" to loadedAs,
                        "Failure reason" to failedBatch.failureReason.toString(),
                        "Asset GUID" to asset.guid,
                        "Asset name" to (asset.name ?: ""),
                    ),
                )
            }
        }
    }
}
