/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.util

import com.atlan.cache.OffHeapAssetCache
import com.atlan.model.core.AtlanCloseable
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.objectstore.ObjectStorageSyncer
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.RowPreprocessor
import com.atlan.util.AssetBatch.AssetIdentity
import mu.KLogger
import java.io.File.separator
import java.io.IOException
import java.nio.file.Paths
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Automate the delta detection and asset removal from imports that use a full CSV file of all
 * assets each time.
 *
 * @param ctx context of the running custom package
 * @param semantic the type of calculation (only {@code full} will cause any calculation and asset removal)
 * @param qualifiedNamePrefix prefix for all assets' qualifiedNames that should be considered for the delta calculation
 * @param removalType how to remove assets ({@code purge} will hard-delete, anything else will soft-delete)
 * @param previousFilesPrefix prefix to use in the object store for all previous files, this should be a unique name for the package
 * @param resolver asset resolver to use for resolving asset identities from the CSV file
 * @param preprocessedDetails details retrieved from pre-processing the input CSV file
 * @param typesToRemove limit the asset types that will be removed to this collection of type names
 * @param logger for logging
 * @param reloadSemantic the type of reload to do for assets (default {@code all} will reload any assets that are not deleted, whether changed or not)
 * @param previousFilePreprocessor responsible for pre-processing the previous CSV file (if provided directly)
 * @param outputDirectory local directory where files can be written and compared
 * @param previousFileProcessedExtension extension to use in the object store for files that have been processed
 */
class DeltaProcessor(
    val ctx: PackageContext<*>,
    val semantic: String,
    val qualifiedNamePrefix: String?,
    val removalType: String,
    val previousFilesPrefix: String,
    val resolver: AssetResolver,
    val preprocessedDetails: Results,
    val typesToRemove: Collection<String>,
    private val logger: KLogger,
    val reloadSemantic: String = "all",
    val previousFilePreprocessor: CSVPreprocessor? = null,
    val outputDirectory: String = Paths.get(separator, "tmp").toString(),
    private val previousFileProcessedExtension: String = ".processed",
) : AtlanCloseable {
    private val objectStore = Utils.getBackingStore(outputDirectory)
    private var initialLoad: Boolean = true
    private var delta: FileBasedDelta? = null
    var deletedAssets: OffHeapAssetCache? = null
    private val reloadAll = reloadSemantic == "all"

    /**
     * Calculate any delta from the provided file context.
     */
    fun calculate() {
        if (semantic == "full") {
            if (preprocessedDetails.multipleConnections) {
                throw IllegalStateException(
                    """
                    Assets in multiple connections detected in the input file.
                    Full delta processing currently only works for a single connection per input file, exiting.
                    """.trimIndent(),
                )
            }
            if (qualifiedNamePrefix.isNullOrBlank()) {
                logger.warn { "Unable to determine qualifiedName prefix, cannot calculate any delta." }
            } else {
                val purgeAssets = removalType == "purge"
                val assetRootName = preprocessedDetails.assetRootName
                val previousFileLocation = "$previousFilesPrefix/$qualifiedNamePrefix"
                val previousFile =
                    if (previousFilePreprocessor != null && previousFilePreprocessor.filename.isNotBlank()) {
                        transformPreviousRaw(assetRootName, previousFilePreprocessor)
                    } else {
                        objectStore.copyLatestFrom(previousFileLocation, previousFileProcessedExtension, outputDirectory)
                    }
                if (previousFile.isNotBlank()) {
                    // If there was a previous file, calculate the delta (changes + deletions)
                    initialLoad = false
                    delta =
                        FileBasedDelta(
                            ctx.connectionCache.getIdentityMap(),
                            resolver,
                            logger,
                            typesToRemove.toList(),
                            qualifiedNamePrefix,
                            purgeAssets,
                            !reloadAll,
                            outputDirectory,
                        )
                    delta!!.calculateDelta(preprocessedDetails.preprocessedFile, previousFile)
                } else {
                    logger.info { "No previous file found, treated it as an initial load." }
                }
            }
        }
    }

    /**
     * Resolve the asset represented by a row of values in a CSV to an asset identity.
     *
     * @param values row of values for that asset from the CSV
     * @param header order of column names in the CSV file being processed
     * @return a unique asset identity for that row of the CSV
     */
    @Throws(IOException::class)
    fun resolveAsset(
        values: List<String>,
        header: List<String>,
    ): AssetIdentity? = delta?.resolveAsset(values, header)

    /**
     * Determine whether the provided asset identity should be processed (true) or skipped (false).
     *
     * @param identity of the asset to check whether reloading should occur
     * @return true if the asset with this identity should be reloaded, otherwise false
     */
    fun reloadAsset(identity: AssetIdentity): Boolean {
        if (!reloadAll) {
            return delta?.assetsToReload?.containsKey(identity) ?: true
        }
        return true
    }

    /**
     * Delete any assets that were detected by the delta to be deleted.
     *
     * @param client connectivity to the Atlan tenant
     */
    fun processDeletions() {
        if (!initialLoad && delta!!.hasAnythingToDelete()) {
            // Note: this will update the persistent connection cache for both adds and deletes
            deletedAssets = delta!!.deleteAssets(ctx.client)
        }
    }

    /**
     * Upload the latest processed file to the backing store, to persist the state for the next run.
     */
    fun uploadStateToBackingStore() {
        if (!qualifiedNamePrefix.isNullOrBlank()) {
            // Copy processed files to specified location in object storage for future comparison purposes
            uploadToBackingStore(objectStore, preprocessedDetails.preprocessedFile, qualifiedNamePrefix, previousFileProcessedExtension)
        }
    }

    /**
     * Update the persistent connection cache with details of any assets that were added or removed.
     *
     * @param modifiedAssets cache of assets that were created or modified (whether by initial processing or reloading)
     */
    fun updateConnectionCache(modifiedAssets: OffHeapAssetCache? = null) {
        // Update the connection cache with any changes (added and / or removed assets)
        Utils.updateConnectionCache(
            client = ctx.client,
            added = modifiedAssets,
            removed = deletedAssets,
            fallback = outputDirectory,
        )
    }

    /** {@inheritDoc} */
    override fun close() {
        AtlanCloseable.close(delta)
        AtlanCloseable.close(deletedAssets)
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
            DateTimeFormatter
                .ofPattern("yyyyMMdd-HHmmssSSS")
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
     * @param hasDomainRelationship whether there are any domain relationships in the input file
     * @param preprocessedFile full path to the preprocessed input file
     * @param multipleConnections whether multiple connections were present in the input file (true) or only a single connection (false)
     */
    open class Results(
        val assetRootName: String,
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        hasDomainRelationship: Boolean,
        val preprocessedFile: String,
        val multipleConnections: Boolean = false,
    ) : RowPreprocessor.Results(
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            hasDomainRelationship = hasDomainRelationship,
            outputFile = preprocessedFile,
        )
}
