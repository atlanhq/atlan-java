/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.Atlan
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
import com.atlan.pkg.objectstore.ObjectStorageSyncer
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.serde.csv.RowPreprocessor
import com.atlan.pkg.util.AssetRemover
import mu.KotlinLogging
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern
import kotlin.jvm.optionals.getOrNull
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = KotlinLogging.logger {}

    const val QN_DELIMITER = "~"
    const val PREVIOUS_FILES_PREFIX = "csa-cube-assets-builder"
    const val PREVIOUS_FILE_PROCESSED_EXT = ".processed"

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<CubeAssetsBuilderCfg>()
        import(config, outputDirectory)
    }

    /**
     * Actually import the cube assets.
     *
     * @param config the configuration for the import
     * @param outputDirectory (optional) into which to write any logs or preprocessing information
     * @return the qualifiedName of the cube that was imported, or null if no cube was loaded
     */
    fun import(
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
        val connectionImporter =
            ConnectionImporter(
                preprocessedDetails,
                assetAttrsToOverwrite,
                assetsSemantic,
                1,
                true,
                fieldSeparator,
            )
        connectionImporter.import()

        logger.info { " --- Importing cubes... ---" }
        val cubeImporter =
            CubeImporter(
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

        // Retrieve the qualifiedName of the cube that was imported
        val cubeQN =
            cubeImporterResults?.primary?.guidAssignments?.values?.first().let {
                Cube.select().where(Cube.GUID.eq(it)).pageSize(1).stream().findFirst().getOrNull()?.qualifiedName
            }

        if (Atlan.getDefaultClient().isInternal && trackBatches) {
            // Only attempt to manage a connection cache if we are running in-cluster
            Utils.updateConnectionCache(
                added = ImportResults.getAllModifiedAssets(cubeImporterResults, dimResults, hierResults, fieldResults),
            )
        }

        val runAssetRemoval = Utils.getOrDefault(config.deltaSemantic, "full") == "full"
        if (runAssetRemoval) {
            if (cubeQN == null) {
                logger.warn { "Unable to determine cube's qualifiedName, will not delete any assets." }
            } else {
                val purgeAssets = Utils.getOrDefault(config.deltaRemovalType, "archive") == "purge"
                val previousFileDirect = Utils.getOrDefault(config.previousFileDirect, "")
                val skipObjectStore = Utils.getOrDefault(config.skipObjectStore, false)
                val cubeName = preprocessedDetails.cubeName
                val previousFileLocation = "$PREVIOUS_FILES_PREFIX/$cubeQN"
                val objectStore = if (!skipObjectStore) Utils.getBackingStore() else null
                val lastCubesFile =
                    if (previousFileDirect.isNotBlank()) {
                        transformPreviousRaw(previousFileDirect, cubeName, fieldSeparator)
                    } else if (skipObjectStore) {
                        ""
                    } else {
                        objectStore!!.copyLatestFrom(previousFileLocation, PREVIOUS_FILE_PROCESSED_EXT, outputDirectory)
                    }
                if (lastCubesFile.isNotBlank()) {
                    // If there was a previous file, calculate the delta to see what we need
                    // to delete
                    val assetRemover =
                        AssetRemover(
                            ConnectionCache.getIdentityMap(),
                            AssetImporter.Companion,
                            logger,
                            listOf(CubeDimension.TYPE_NAME, CubeHierarchy.TYPE_NAME, CubeField.TYPE_NAME),
                            cubeQN,
                            purgeAssets,
                        )
                    assetRemover.calculateDeletions(preprocessedDetails.preprocessedFile, lastCubesFile)
                    if (assetRemover.hasAnythingToDelete()) {
                        assetRemover.deleteAssets()
                    }
                } else {
                    logger.info { "No previous file found for cube, treated it as an initial load." }
                }
                // Copy processed files to specified location in object storage for future comparison purposes
                if (!skipObjectStore) {
                    uploadToBackingStore(objectStore!!, preprocessedDetails.preprocessedFile, cubeQN, PREVIOUS_FILE_PROCESSED_EXT)
                }
            }
        }
        return cubeQN
    }

    /**
     * Upload a file used to load the cube to Atlan backing store.
     *
     * @param objectStore syncer providing access to the Atlan's backing object store
     * @param localFile the full path of the local file to upload
     * @param cubeQualifiedName the qualified name of the cube to which the file belongs
     * @param extension the extension to add to the file in object storage
     */
    fun uploadToBackingStore(
        objectStore: ObjectStorageSyncer,
        localFile: String,
        cubeQualifiedName: String,
        extension: String,
    ) {
        val previousFileLocation = "$PREVIOUS_FILES_PREFIX/$cubeQualifiedName"
        val sortedTime =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS")
                .withZone(ZoneId.of("UTC"))
                .format(Instant.now())
        Utils.uploadOutputFile(objectStore, localFile, previousFileLocation, "$sortedTime$extension")
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
                qualifiedNameToChildCount,
            )
        }
    }

    private fun transformPreviousRaw(
        previousRaw: String,
        cubeName: String,
        fieldSeparator: Char,
    ): String {
        logger.info { "Found previous raw file, transforming it for comparison: $previousRaw" }
        val preprocessedPrevious = Preprocessor(previousRaw, fieldSeparator).preprocess<Results>()
        val previousCubeName = preprocessedPrevious.cubeName
        return if (cubeName != previousCubeName) {
            // Ensure the cube names match, otherwise log a warning instead
            logger.warn { "Previous cube name ($previousCubeName) does not match current ($cubeName) -- will not delete any assets." }
            ""
        } else {
            preprocessedPrevious.preprocessedFile
        }
    }

    class Results(
        val cubeName: String,
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        val preprocessedFile: String,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
    ) : RowPreprocessor.Results(
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            outputFile = null,
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
