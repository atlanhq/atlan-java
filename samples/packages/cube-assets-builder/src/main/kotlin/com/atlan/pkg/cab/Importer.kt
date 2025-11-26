/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Cube
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeHierarchy
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.cab.AssetImporter.Companion.getQualifiedNameDetails
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.AssetResolver
import com.atlan.pkg.util.DeltaProcessor
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern
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
        val preprocessedDetails = Preprocessor(assetsInput, fieldSeparator).preprocess<Results>()

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
            previousFilePreprocessor = Preprocessor(ctx.config.previousFileDirect, fieldSeparator),
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

    private class Preprocessor(
        originalFile: String,
        fieldSeparator: Char,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
            validator = AssetImporter::validateHeader,
        ) {
        val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
        var cubeName: String? = null
        var connectionIdentity: AssetResolver.ConnectionIdentity? = null

        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            val cubeNameOnRow = row.getOrNull(header.indexOf(Cube.CUBE_NAME.atlanFieldName)) ?: ""
            if (cubeName.isNullOrBlank()) {
                cubeName = cubeNameOnRow
            }
            if (cubeName != cubeNameOnRow) {
                logger.error { "Cube name changed mid-file: $cubeName -> $cubeNameOnRow" }
                logger.error { "This package is designed to only process a single cube per input file, exiting." }
                exitProcess(101)
            }
            if (connectionIdentity == null) {
                val name = row.getOrNull(header.indexOf("connectionName"))
                val type = row.getOrNull(header.indexOf("connectorType"))?.lowercase()
                if (name != null && type != null) {
                    connectionIdentity = AssetResolver.ConnectionIdentity(name, type)
                }
            }
            val values = row.toMutableList()
            val typeName = CSVXformer.trimWhitespace(values.getOrElse(typeIdx) { "" })
            val qnDetails = getQualifiedNameDetails(values, header, typeName)
            if (qnDetails.parentUniqueQN.isNotBlank()) {
                if (!qualifiedNameToChildCount.containsKey(qnDetails.parentUniqueQN)) {
                    qualifiedNameToChildCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                }
                qualifiedNameToChildCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                if (typeName == CubeField.TYPE_NAME) {
                    val hierarchyQN = getHierarchyQualifiedName(qnDetails.parentUniqueQN)
                    if (hierarchyQN != qnDetails.parentUniqueQN) {
                        // Only further increment the field count of the hierarchy for nested
                        // fields (top-level fields are already counted by the logic above)
                        if (!qualifiedNameToChildCount.containsKey(hierarchyQN)) {
                            qualifiedNameToChildCount[hierarchyQN] = AtomicInteger(0)
                        }
                        qualifiedNameToChildCount[hierarchyQN]?.incrementAndGet()
                    }
                }
            }
            return row
        }

        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): Results {
            val results = super.finalize(header, outputFile)
            return Results(
                cubeName!!,
                results.hasLinks,
                results.hasTermAssignments,
                results.hasDomainRelationship,
                results.hasProductRelationship,
                filename,
                connectionIdentity,
                qualifiedNameToChildCount,
            )
        }
    }

    class Results(
        assetRootName: String,
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        hasDomainRelationship: Boolean,
        hasProductRelationship: Boolean,
        preprocessedFile: String,
        val connectionIdentity: AssetResolver.ConnectionIdentity?,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
    ) : DeltaProcessor.Results(
            assetRootName = assetRootName,
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            hasDomainRelationship = hasDomainRelationship,
            hasProductRelationship = hasProductRelationship,
            preprocessedFile = preprocessedFile,
        )

    private val hierarchyQNPrefix: Pattern = Pattern.compile("([^/]*/[a-z0-9-]+/[^/]*(/[^/]*){2}).*")

    /**
     * Extracts the unique name of the hierarchy from the qualified name of the CubeField's parent.
     *
     * @param parentQualifiedName unique name of the hierarchy or parent field in which this CubeField exists
     * @return the unique name of the CubeHierarchy in which the field exists
     */
    private fun getHierarchyQualifiedName(parentQualifiedName: String): String {
        val m = hierarchyQNPrefix.matcher(parentQualifiedName)
        return if (m.find() && m.groupCount() > 0) m.group(1) else ""
    }
}
