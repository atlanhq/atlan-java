/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import Importer.clearField
import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.AssetCache
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.AssetRefXformer
import mu.KotlinLogging
import kotlin.system.exitProcess

/**
 * Import glossaries, terms and categories (only) into Atlan from a provided CSV file.
 *
 * Only the terms and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param cache of existing glossaries, terms or categories (will be preloaded by import)
 * @param typeNameFilter name of the specific type that should be handled by this importer
 */
abstract class GTCImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    protected val cache: AssetCache,
    protected val typeNameFilter: String,
) : AssetGenerator {
    private val logger = KotlinLogging.logger {}

    /**
     * Actually run the import.
     */
    open fun import() {
        cache.preload()
        CSVReader(filename, updateOnly).use { csv ->
            val start = System.currentTimeMillis()
            val anyFailures = csv.streamRows(this, batchSize, logger)
            logger.info("Total time taken: {} ms", System.currentTimeMillis() - start)
            if (anyFailures) {
                logger.error("Some errors detected, failing the workflow.")
                exitProcess(1)
            }
            cacheCreated(csv.created)
        }
    }

    /**
     * Cache any created assets.
     *
     * @param map from GUID to asset that was created
     */
    open fun cacheCreated(map: Map<String, Asset>) {
        // Cache any assets that were created by processing
        map.forEach { (k, v) ->
            cache.addByGuid(k, v)
        }
    }

    /**
     * For terms and categories, we must look up the asset and then cache it to ensure that
     * we have the necessary identity characteristics (since these are not inherent in the normal
     * attributes or qualifiedName).
     *
     * @param map from GUID to asset that was created
     */
    protected fun lookupAndCache(map: Map<String, Asset>) {
        map.forEach { (k, v) ->
            val result = cache.lookupAssetByGuid(k, maxRetries = 5)
            result?.let {
                cache.addByGuid(k, result)
            }
        }
    }

    /**
     * Translate a row of CSV values into a term object, overwriting any attributes that were empty
     * in the CSV with blank values, per the job configuration.
     *
     * @param row of values in the CSV
     * @param header names of columns (and their position) in the header of the CSV
     * @param typeIdx numeric index of the column containing the typeName of the asset in the row
     * @param qnIdx numeric index of the column containing the qualifiedName of the asset in the row
     * @return the deserialized asset object(s)
     */
    override fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): RowDeserialization? {
        // Deserialize the objects represented in that row (could be more than one due to flattening
        // of in particular things like READMEs and Links)
        if (includeRow(row, header, typeIdx, qnIdx)) {
            val fallbackQN = getFallbackQualifiedName(row, header, typeIdx, qnIdx)
            val assets = RowDeserializer(header, row, typeIdx, qnIdx, fallbackQN).getAssets()
            if (assets != null) {
                val builder = assets.primary
                val candidate = builder.build()
                val cacheId = cache.getIdentityForAsset(candidate)
                val qualifiedName = cache.getByIdentity(cacheId)?.qualifiedName ?: cacheId
                if (qualifiedName == cacheId) {
                    // Inject the qualifiedName if there was none in the CSV
                    builder.qualifiedName(cacheId)
                }
                val identity = RowDeserialization.AssetIdentity(candidate.typeName, qualifiedName)
                // Then apply any field clearances based on attributes configured in the job
                for (field in attrsToOverwrite) {
                    clearField(field, candidate, builder)
                    // If there are no related assets
                    if (!assets.related.containsKey(field.atlanFieldName)) {
                        assets.delete.add(field)
                    }
                }
                return RowDeserialization(identity, builder, assets.related, assets.delete)
            }
        }
        return null
    }

    /**
     * Build a complete related asset object from the provided asset and (partial) related asset details.
     *
     * @param asset the asset to which another asset is to be related (should have at least its GUID and name)
     * @param related the (partial) asset that should be related to the asset, which needs to be completed
     * @return a completed related asset that can be idempotently saved
     */
    override fun buildRelated(asset: Asset, related: Asset): Asset {
        return AssetRefXformer.getRelated(asset, related)
    }

    /**
     * Check whether to include this row as part of the processing (true) or not (false).
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @param qnIdx index of the qualifiedName
     * @return true if the row should be included in the import, or false if not
     */
    abstract fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean

    /**
     * Calculate a fallback qualifiedName, if the qualifiedName value in this row is empty.
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @param qnIdx index of the qualifiedName
     * @return a qualifiedName to use as a placeholder for when none is specified
     */
    abstract fun getFallbackQualifiedName(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): String
}
