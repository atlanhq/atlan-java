/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.AtlanClient
import com.atlan.model.assets.Cube
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeHierarchy
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.pkg.Utils
import com.atlan.pkg.cab.AssetImporter.Companion.getQualifiedNameDetails
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.cache.TermCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.AssetResolver
import com.atlan.pkg.util.DeltaProcessor
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = KotlinLogging.logger {}

    const val QN_DELIMITER = "~"
    const val PREVIOUS_FILES_PREFIX = "csa-cube-assets-builder"

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<CubeAssetsBuilderCfg>()
        Utils.initializeContext(config).use { client ->
            import(client, config, outputDirectory)
        }
    }

    /**
     * Actually import the cube assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param config the configuration for the import
     * @param outputDirectory (optional) into which to write any logs or preprocessing information
     * @return the qualifiedName of the cube that was imported, or null if no cube was loaded
     */
    fun import(
        client: AtlanClient,
        config: CubeAssetsBuilderCfg,
        outputDirectory: String = "tmp",
    ): String? {
        val batchSize = Utils.getOrDefault(config.assetsBatchSize, 20).toInt()
        val fieldSeparator = Utils.getOrDefault(config.assetsFieldSeparator, ",")[0]
        val assetsUpload = Utils.getOrDefault(config.assetsImportType, "DIRECT") == "DIRECT"
        val assetsKey = Utils.getOrDefault(config.assetsKey, "")
        val assetsFilename = Utils.getOrDefault(config.assetsFile, "")
        val assetAttrsToOverwrite =
            CSVImporter.attributesToClear(Utils.getOrDefault(config.assetsAttrToOverwrite, listOf()).toMutableList(), "assets", logger)
        val assetsFailOnErrors = Utils.getOrDefault(config.assetsFailOnErrors, true)
        val assetsSemantic = Utils.getCreationHandling(config.assetsUpsertSemantic, AssetCreationHandling.FULL)
        val trackBatches = Utils.getOrDefault(config.trackBatches, true)

        val assetsFileProvided = (assetsUpload && assetsFilename.isNotBlank()) || (!assetsUpload && assetsKey.isNotBlank())
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
        val preprocessedDetails = Preprocessor(assetsInput, fieldSeparator).preprocess<Results>()

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
        // We also need to load these connections first, irrespective of any delta calculation, so that
        // we can be certain we will be able to resolve the cube's qualifiedName (for subsequent processing)
        val connectionImporter =
            ConnectionImporter(
                client,
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                1,
                true,
                fieldSeparator,
            )
        connectionImporter.import()?.close()

        val connectionQN = ConnectionCache.getIdentityMap().getOrDefault(preprocessedDetails.connectionIdentity, null)
        if (connectionQN == null) {
            logger.error { "Unable to determine the qualifiedName of the connection that is being loaded -- exiting." }
            exitProcess(105)
        }
        val cubeQN = "$connectionQN/${preprocessedDetails.assetRootName}"

        DeltaProcessor(
            semantic = Utils.getOrDefault(config.deltaSemantic, "full"),
            qualifiedNamePrefix = cubeQN,
            removalType = Utils.getOrDefault(config.deltaRemovalType, "archive"),
            previousFilesPrefix = PREVIOUS_FILES_PREFIX,
            resolver = AssetImporter,
            preprocessedDetails = preprocessedDetails,
            typesToRemove = listOf(CubeDimension.TYPE_NAME, CubeHierarchy.TYPE_NAME, CubeField.TYPE_NAME),
            logger = logger,
            reloadSemantic = Utils.getOrDefault(config.deltaReloadCalculation, "all"),
            previousFilePreprocessor = Preprocessor(Utils.getOrDefault(config.previousFileDirect, ""), fieldSeparator),
            outputDirectory = outputDirectory,
        ).use { delta ->

            delta.calculate()

            logger.info { " --- Importing cubes... ---" }
            val cubeImporter =
                CubeImporter(
                    client,
                    delta,
                    preprocessedDetails,
                    assetAttrsToOverwrite,
                    assetsSemantic,
                    batchSize,
                    connectionImporter,
                    true,
                    fieldSeparator,
                )
            val cubeImporterResults = cubeImporter.import()

            logger.info { " --- Importing dimensions... ---" }
            val dimensionImporter =
                DimensionImporter(
                    client,
                    delta,
                    preprocessedDetails,
                    assetAttrsToOverwrite,
                    assetsSemantic,
                    batchSize,
                    connectionImporter,
                    trackBatches,
                    fieldSeparator,
                )
            val dimResults = dimensionImporter.import()

            logger.info { " --- Importing hierarchies... ---" }
            val hierarchyImporter =
                HierarchyImporter(
                    client,
                    delta,
                    preprocessedDetails,
                    assetAttrsToOverwrite,
                    assetsSemantic,
                    batchSize,
                    connectionImporter,
                    trackBatches,
                    fieldSeparator,
                )
            val hierResults = hierarchyImporter.import()

            logger.info { " --- Importing fields... ---" }
            val fieldImporter =
                FieldImporter(
                    client,
                    delta,
                    preprocessedDetails,
                    assetAttrsToOverwrite,
                    assetsSemantic,
                    batchSize,
                    connectionImporter,
                    trackBatches,
                    fieldSeparator,
                )
            fieldImporter.preprocess()
            val fieldResults = fieldImporter.import()

            delta.processDeletions(client)

            ImportResults.getAllModifiedAssets(client, true, cubeImporterResults, dimResults, hierResults, fieldResults).use { modifiedAssets ->
                delta.updateConnectionCache(client, modifiedAssets)
            }
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
                val type = row.getOrNull(header.indexOf("connectorType"))
                if (name != null && type != null) {
                    connectionIdentity = AssetResolver.ConnectionIdentity(name, type)
                }
            }
            val values = row.toMutableList()
            val typeName = values[typeIdx]
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
        preprocessedFile: String,
        val connectionIdentity: AssetResolver.ConnectionIdentity?,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
    ) : DeltaProcessor.Results(
            assetRootName = assetRootName,
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
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
