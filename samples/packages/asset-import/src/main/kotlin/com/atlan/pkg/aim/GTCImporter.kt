/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.AssetCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVImporter
import mu.KLogger

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
 * @param logger through which to log any problems
 */
abstract class GTCImporter(
    filename: String,
    attrsToOverwrite: List<AtlanField>,
    updateOnly: Boolean,
    batchSize: Int,
    protected val cache: AssetCache,
    typeNameFilter: String,
    logger: KLogger,
) : CSVImporter(
    filename,
    logger,
    typeNameFilter,
    attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
) {
    /**
     * Cache any created assets.
     *
     * @param map from GUID to asset that was created
     */
    override fun cacheCreated(map: Map<String, Asset>) {
        // Cache any assets that were created by processing
        map.keys.forEach { k ->
            // We must look up the asset and then cache to ensure we have the necessary identity
            // characteristics and status
            val result = cache.lookupAssetByGuid(k, maxRetries = 5)
            result?.let {
                cache.addByGuid(k, result)
            } ?: throw IllegalStateException("Result of searching by GUID for $k was null.")
        }
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val qualifiedName = generateQualifiedName(deserializer)
        return FieldSerde.getBuilderForType(typeNameFilter)
            .qualifiedName(qualifiedName)
    }

    /**
     * Calculate a fallback qualifiedName, if the qualifiedName value in this row is empty.
     *
     * @param deserializer a row of deserialized values
     * @return the qualifiedName, calculated from the deserialized values
     */
    private fun generateQualifiedName(deserializer: RowDeserializer): String {
        val qn = deserializer.getValue(Asset.QUALIFIED_NAME.atlanFieldName)?.let { it as String } ?: ""
        return qn.ifBlank {
            val cacheId = getCacheId(deserializer)
            cache.getByIdentity(cacheId)?.qualifiedName ?: cacheId
        }
    }

    /**
     * Calculate the cache identity for this row of the CSV, based purely on the information in the CSV.
     *
     * @param deserializer a row of deserialized values
     * @return the cache identity for the row
     */
    abstract fun getCacheId(deserializer: RowDeserializer): String
}
