/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.util

import com.atlan.pkg.Utils
import com.atlan.pkg.cache.ConnectionCache
import com.atlan.pkg.objectstore.ObjectStorageSyncer
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.RowPreprocessor
import mu.KLogger
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Automate the delta detection and asset removal from imports that use a full CSV file of all
 * assets each time.
 *
 * @param semantic the type of calculation (only {@code full} will cause any calculation and asset removal)
 * @param qualifiedNamePrefix prefix for all assets' qualifiedNames that should be considered for the delta calculation
 * @param removalType how to remove assets ({@code purge} will hard-delete, anything else will soft-delete)
 * @param previousFilesPrefix prefix to use in the object store for all previous files, this should be a unique name for the package
 * @param resolver asset resolver to use for resolving asset identities from the CSV file
 * @param preprocessedDetails details retrieved from pre-processing the input CSV file
 * @param typesToRemove limit the asset types that will be removed to this collection of type names
 * @param logger for logging
 * @param previousFilePreprocessor responsible for pre-processing the previous CSV file (if provided directly)
 * @param outputDirectory local directory where files can be written and compared
 * @param skipObjectStore whether to skip any processing via the object store (true) or not (false)
 * @param previousFileProcessedExtension extension to use in the object store for files that have been processed
 */
class DeltaProcessor(
    val semantic: String,
    val qualifiedNamePrefix: String?,
    val removalType: String,
    val previousFilesPrefix: String,
    val resolver: AssetResolver,
    val preprocessedDetails: Results,
    val typesToRemove: Collection<String>,
    private val logger: KLogger,
    val previousFilePreprocessor: CSVPreprocessor? = null,
    val outputDirectory: String = "tmp",
    val skipObjectStore: Boolean = false,
    private val previousFileProcessedExtension: String = ".processed",
) {
    fun run() {
        if (semantic == "full") {
            if (qualifiedNamePrefix.isNullOrBlank()) {
                logger.warn { "Unable to determine qualifiedName prefix, will not delete any assets." }
            } else {
                val purgeAssets = removalType == "purge"
                val assetRootName = preprocessedDetails.assetRootName
                val previousFileLocation = "$previousFilesPrefix/$qualifiedNamePrefix"
                val objectStore = if (!skipObjectStore) Utils.getBackingStore() else null
                val previousFile =
                    if (previousFilePreprocessor != null && previousFilePreprocessor.filename.isNotBlank()) {
                        transformPreviousRaw(assetRootName, previousFilePreprocessor)
                    } else if (skipObjectStore) {
                        ""
                    } else {
                        objectStore!!.copyLatestFrom(previousFileLocation, previousFileProcessedExtension, outputDirectory)
                    }
                if (previousFile.isNotBlank()) {
                    // If there was a previous file, calculate the delta to see what we need
                    // to delete
                    val assetRemover =
                        AssetRemover(
                            ConnectionCache.getIdentityMap(),
                            resolver,
                            logger,
                            typesToRemove.toList(),
                            qualifiedNamePrefix,
                            purgeAssets,
                        )
                    assetRemover.calculateDeletions(preprocessedDetails.preprocessedFile, previousFile)
                    if (assetRemover.hasAnythingToDelete()) {
                        assetRemover.deleteAssets()
                    }
                } else {
                    logger.info { "No previous file found for cube, treated it as an initial load." }
                }
                // Copy processed files to specified location in object storage for future comparison purposes
                if (!skipObjectStore) {
                    uploadToBackingStore(objectStore!!, preprocessedDetails.preprocessedFile, qualifiedNamePrefix, previousFileProcessedExtension)
                }
            }
        }
    }

    /**
     * Upload the file used to load the assets to Atlan backing store.
     *
     * @param objectStore syncer providing access to the Atlan's backing object store
     * @param localFile the full path of the local file to upload
     * @param qualifiedNamePrefix the qualified name that prefixes all assets to which the file belongs
     * @param extension the extension to add to the file in object storage
     */
    private fun uploadToBackingStore(
        objectStore: ObjectStorageSyncer,
        localFile: String,
        qualifiedNamePrefix: String,
        extension: String,
    ) {
        val previousFileLocation = "$previousFilesPrefix/$qualifiedNamePrefix"
        val sortedTime =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS")
                .withZone(ZoneId.of("UTC"))
                .format(Instant.now())
        Utils.uploadOutputFile(objectStore, localFile, previousFileLocation, "$sortedTime$extension")
    }

    /**
     * Transform a previous (raw) file, to use for comparison purposes in calculating the delta.
     *
     * @param assetRootName the unique name of the root-level of all assets (e.g. a connection)
     * @param preprocessor responsible for pre-processing the CSV file
     */
    private fun transformPreviousRaw(
        assetRootName: String,
        preprocessor: CSVPreprocessor,
    ): String {
        logger.info { "Found previous raw file, transforming it for comparison: ${preprocessor.filename}" }
        val preprocessedPrevious = preprocessor.preprocess<Results>()
        val previousRoot = preprocessedPrevious.assetRootName
        return if (assetRootName != previousRoot) {
            // Ensure the prefixes match, otherwise log a warning instead
            logger.warn { "Previous asset root ($previousRoot) does not match current ($assetRootName) -- will not delete any assets." }
            ""
        } else {
            preprocessedPrevious.preprocessedFile
        }
    }

    /**
     * Minimal set of information that must be tracked by a pre-processor for the delta processing
     * to be automated.
     *
     * @param assetRootName the unique name of the root-level of all assets (e.g. a connection)
     * @param hasLinks whether there are any links in the input file
     * @param hasTermAssignments whether there are any term assignments in the input file
     * @param preprocessedFile full path to the preprocessed input file
     */
    open class Results(
        val assetRootName: String,
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        val preprocessedFile: String,
    ) : RowPreprocessor.Results(
        hasLinks = hasLinks,
        hasTermAssignments = hasTermAssignments,
        outputFile = preprocessedFile,
    )
}
