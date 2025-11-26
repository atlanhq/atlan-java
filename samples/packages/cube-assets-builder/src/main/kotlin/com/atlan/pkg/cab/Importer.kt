/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeHierarchy
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.DeltaProcessor
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = Utils.getLogger(this.javaClass.name)

    const val QN_DELIMITER = "~"
    const val PREVIOUS_FILES_PREFIX = "csa-cube-assets-builder"

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<CubeAssetsBuilderCfg>().use { ctx ->
            import(ctx, outputDirectory)
        }
    }

    /**
     * Actually import the cube assets.
     *
     * @param ctx context in which this package is running
     * @param outputDirectory (optional) into which to write any logs or preprocessing information
     * @return the qualifiedName of the cube that was imported, or null if no cube was loaded
     */
    fun import(
        ctx: PackageContext<CubeAssetsBuilderCfg>,
        outputDirectory: String = "tmp",
    ): String {
        if (ctx.config.assetsFieldSeparator.length > 1) {
            logger.error { "Field separator must be only a single character. The provided value is too long: ${ctx.config.assetsFieldSeparator}" }
            exitProcess(2)
        }
        val fieldSeparator = ctx.config.assetsFieldSeparator[0]

        val assetsFileProvided = Utils.isFileProvided(ctx.config.assetsImportType, ctx.config.assetsFile, ctx.config.assetsKey)
        if (!assetsFileProvided) {
            logger.error { "No input file was provided for assets." }
            exitProcess(1)
        }

        // Preprocess the CSV file in an initial pass to inject key details,
        // to allow subsequent out-of-order parallel processing
        val assetsInput =
            Utils.getInputFile(
                ctx.config.assetsFile,
                outputDirectory,
                ctx.config.assetsImportType == "DIRECT",
                ctx.config.assetsPrefix,
                ctx.config.assetsKey,
            )
        val preprocessedDetails = FieldImporter.Preprocessor(assetsInput, fieldSeparator, logger).preprocess<FieldImporter.Results>()

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
        val connectionResults = connectionImporter.import()
        if (connectionResults?.anyFailures == true && ctx.config.assetsFailOnErrors) {
            logger.error { "Some errors detected while loading connections, failing the workflow." }
            connectionResults.close()
            exitProcess(1)
        }
        connectionResults?.close()

        val connectionQN = ctx.connectionCache.getIdentityMap().getOrDefault(preprocessedDetails.connectionIdentity, null)
        if (connectionQN == null) {
            logger.error { "Unable to determine the qualifiedName of the connection that is being loaded -- exiting." }
            exitProcess(105)
        }
        val cubeQN = "$connectionQN/${preprocessedDetails.assetRootName}"

        DeltaProcessor(
            ctx = ctx,
            semantic = ctx.config.deltaSemantic,
            qualifiedNamePrefix = cubeQN,
            removalType =
                ctx.config.getEffectiveValue(
                    CubeAssetsBuilderCfg::deltaRemovalType,
                    CubeAssetsBuilderCfg::deltaSemantic,
                    "full",
                ),
            previousFilesPrefix = PREVIOUS_FILES_PREFIX,
            resolver = AssetImporter,
            preprocessedDetails = preprocessedDetails,
            typesToRemove = listOf(CubeDimension.TYPE_NAME, CubeHierarchy.TYPE_NAME, CubeField.TYPE_NAME),
            logger = logger,
            reloadSemantic =
                ctx.config.getEffectiveValue(
                    CubeAssetsBuilderCfg::deltaReloadCalculation,
                    CubeAssetsBuilderCfg::deltaSemantic,
                    "full",
                ),
            previousFilePreprocessor = if (ctx.config.previousFileDirect.isNotBlank()) FieldImporter.Preprocessor(ctx.config.previousFileDirect, fieldSeparator, logger) else null,
            outputDirectory = outputDirectory,
        ).use { delta ->

            delta.calculate()

            logger.info { " --- Importing cubes... ---" }
            val cubeImporter = CubeImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val cubeImporterResults = cubeImporter.import()
            if (cubeImporterResults?.anyFailures == true && ctx.config.assetsFailOnErrors) {
                logger.error { "Some errors detected while loading cubes, failing the workflow." }
                exitProcess(2)
            }

            logger.info { " --- Importing dimensions... ---" }
            val dimensionImporter = DimensionImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val dimResults = dimensionImporter.import()
            if (dimResults?.anyFailures == true && ctx.config.assetsFailOnErrors) {
                logger.error { "Some errors detected while loading dimensions, failing the workflow." }
                exitProcess(3)
            }

            logger.info { " --- Importing hierarchies... ---" }
            val hierarchyImporter = HierarchyImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            val hierResults = hierarchyImporter.import()
            if (hierResults?.anyFailures == true && ctx.config.assetsFailOnErrors) {
                logger.error { "Some errors detected while loading hierarchies, failing the workflow." }
                exitProcess(4)
            }

            logger.info { " --- Importing fields... ---" }
            val fieldImporter = FieldImporter(ctx, delta, preprocessedDetails, connectionImporter, logger)
            fieldImporter.preprocess()
            val fieldResults = fieldImporter.import()
            if (fieldResults?.anyFailures == true && ctx.config.assetsFailOnErrors) {
                logger.error { "Some errors detected while loading fields, failing the workflow." }
                exitProcess(5)
            }

            delta.processDeletions()

            ImportResults.getAllModifiedAssets(ctx.client, true, cubeImporterResults, dimResults, hierResults, fieldResults).use { modifiedAssets ->
                delta.updateConnectionCache(modifiedAssets)
            }
            delta.uploadStateToBackingStore()
        }
        return cubeQN
    }
}
