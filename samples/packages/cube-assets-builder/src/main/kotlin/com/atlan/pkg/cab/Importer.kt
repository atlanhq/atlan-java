/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Cube
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeField.getHierarchyQualifiedName
import com.atlan.model.assets.CubeHierarchy
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.pkg.Utils
import com.atlan.pkg.cab.AssetImporter.Companion.getQualifiedNameDetails
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.cache.TermCache
import com.atlan.pkg.objectstore.S3Sync
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.util.AssetRemover
import de.siegmar.fastcsv.reader.CsvReader
import mu.KotlinLogging
import java.nio.file.Paths
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
    fun import(config: CubeAssetsBuilderCfg, outputDirectory: String = "tmp"): String? {
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
        val assetsSemantic = Utils.getCreationHandling(config.assetsUpsertSemantic, AssetCreationHandling.FULL)
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
            outputDirectory,
            s3Region = assetsS3Region,
            s3Bucket = assetsS3Bucket,
            s3ObjectKey = assetsS3ObjectKey,
            preferUpload = assetsUpload,
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
            assetsSemantic,
            1,
            true,
            fieldSeparator,
        )
        connectionImporter.import()

        logger.info { " --- Importing cubes... ---" }
        val cubeImporter = CubeImporter(
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
        val dimensionImporter = DimensionImporter(
            preprocessedDetails,
            assetAttrsToOverwrite,
            assetsSemantic,
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
            assetsSemantic,
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
            assetsSemantic,
            batchSize,
            connectionImporter,
            trackBatches,
            fieldSeparator,
        )
        fieldImporter.import()

        // Retrieve the qualifiedName of the cube that was imported
        val cubeQN = cubeImporterResults?.primary?.guidAssignments?.values?.first().let {
            Cube.select().where(Cube.GUID.eq(it)).pageSize(1).stream().findFirst().getOrNull()?.qualifiedName
        }

        val runAssetRemoval = Utils.getOrDefault(config.deltaSemantic, "full") == "full"
        if (runAssetRemoval) {
            if (cubeQN == null) {
                logger.warn { "Unable to determine cube's qualifiedName, will not delete any assets." }
            } else {
                val purgeAssets = Utils.getOrDefault(config.deltaRemovalType, "archive") == "purge"
                val previousFileDirect = Utils.getOrDefault(config.previousFileDirect, "")
                val skipS3 = Utils.getOrDefault(config.skipS3, false)
                val cubeName = preprocessedDetails.cubeName
                val previousFileLocation = "$PREVIOUS_FILES_PREFIX/$cubeQN"
                val s3 = if (!skipS3) S3Sync(defaultBucket, defaultRegion, logger) else null
                val lastCubesFile = if (previousFileDirect.isNotBlank()) {
                    transformPreviousRaw(previousFileDirect, cubeName, fieldSeparator)
                } else if (skipS3) {
                    ""
                } else {
                    s3!!.copyLatestFrom(previousFileLocation, PREVIOUS_FILE_PROCESSED_EXT, outputDirectory)
                }
                if (lastCubesFile.isNotBlank()) {
                    // If there was a previous file, calculate the delta to see what we need
                    // to delete
                    val assetRemover = AssetRemover(
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
                // Copy processed files to specified location in S3 for future comparison purposes
                if (!skipS3) {
                    uploadToS3(s3!!, preprocessedDetails.preprocessedFile, cubeQN, PREVIOUS_FILE_PROCESSED_EXT)
                }
            }
        }
        return cubeQN
    }

    /**
     * Upload a file used to load the cube to S3.
     *
     * @param s3 connectivity to S3 bucket into which to upload the file
     * @param localFile the full path of the local file to upload
     * @param cubeQualifiedName the qualified name of the cube to which the file belongs
     * @param s3Extension the extension to add to the file in S3
     */
    fun uploadToS3(s3: S3Sync, localFile: String, cubeQualifiedName: String, s3Extension: String) {
        val previousFileLocation = "$PREVIOUS_FILES_PREFIX/$cubeQualifiedName"
        val sortedTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS")
            .withZone(ZoneId.of("UTC"))
            .format(Instant.now())
        s3.uploadTo(
            localFile,
            "$previousFileLocation/$sortedTime$s3Extension",
        )
    }

    private fun preprocessCSV(originalFile: String, fieldSeparator: Char): PreprocessedCsv {
        // Setup
        val quoteCharacter = '"'
        val inputFile = Paths.get(originalFile)

        // Open the CSV reader and writer
        val reader = CsvReader.builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter(quoteCharacter)
            .skipEmptyLines(true)
            .ignoreDifferentFieldCount(false)

        // Start processing...
        reader.ofCsvRecord(inputFile).use { tmp ->
            var hasLinks = false
            var hasTermAssignments = false
            val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
            var header: MutableList<String> = mutableListOf()
            var typeIdx = 0
            var cubeName: String? = null
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
                } else {
                    val cubeNameOnRow = row.fields[header.indexOf(Cube.CUBE_NAME.atlanFieldName)] ?: ""
                    if (cubeName.isNullOrBlank()) {
                        cubeName = cubeNameOnRow
                    }
                    if (cubeName != cubeNameOnRow) {
                        logger.error { "Cube name changed mid-file: $cubeName -> $cubeNameOnRow" }
                        logger.error { "This package is designed to only process a single cube per input file, exiting." }
                        exitProcess(101)
                    }
                    val values = row.fields.toMutableList()
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
                }
            }
            return PreprocessedCsv(
                cubeName!!,
                hasLinks,
                hasTermAssignments,
                originalFile,
                qualifiedNameToChildCount,
            )
        }
    }

    private fun transformPreviousRaw(previousRaw: String, cubeName: String, fieldSeparator: Char): String {
        logger.info { "Found previous raw file, transforming it for comparison: $previousRaw" }
        val preprocessedPrevious = preprocessCSV(previousRaw, fieldSeparator)
        val previousCubeName = preprocessedPrevious.cubeName
        return if (cubeName != previousCubeName) {
            // Ensure the cube names match, otherwise log a warning instead
            logger.warn { "Previous cube name ($previousCubeName) does not match current ($cubeName) -- will not delete any assets." }
            ""
        } else {
            preprocessedPrevious.preprocessedFile
        }
    }

    data class PreprocessedCsv(
        val cubeName: String,
        val hasLinks: Boolean,
        val hasTermAssignments: Boolean,
        val preprocessedFile: String,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
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
