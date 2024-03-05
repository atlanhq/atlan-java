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
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
abstract class GTCImporter(
    filename: String,
    attrsToOverwrite: List<AtlanField>,
    updateOnly: Boolean,
    batchSize: Int,
    protected val cache: AssetCache,
    typeNameFilter: String,
    logger: KLogger,
    fieldSeparator: Char,
) : CSVImporter(
    filename,
    logger,
    typeNameFilter,
    attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    trackBatches = true, // Always track batches for GTC importers, to ensure cache is managed
    fieldSeparator = fieldSeparator,
) {
    /** {@inheritDoc} */
    override fun cacheCreated(list: List<Asset>) {
        // Cache any assets that were created by processing
        list.forEach { asset ->
            // We must look up the asset and then cache to ensure we have the necessary identity
            // characteristics and status
            val result = cache.lookupAssetByGuid(asset.guid, maxRetries = 5)
            result?.let {
                cache.addByGuid(asset.guid, result)
            } ?: throw IllegalStateException("Result of searching by GUID for ${asset.guid} was null.")
        }
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val qualifiedName = generateQualifiedName(deserializer)
        return FieldSerde.getBuilderForType(typeNameFilter)
            .qualifiedName(qualifiedName)
    }

    /**
     * Determine the qualifiedName for the glossary, term or category, irrespective of whether it is
     * present in the input file or not. Since these qualifiedNames are generated, and the object may
     * have been created in a previous pass (and cached), we can resolve to its known qualifiedName
     * here based on the information in the row of the input file.
     *
     * @param deserializer a row of deserialized values
     * @return the qualifiedName, calculated from the deserialized values
     */
    private fun generateQualifiedName(deserializer: RowDeserializer): String {
        val cacheId = getCacheId(deserializer)
        return cache.getByIdentity(cacheId)?.qualifiedName ?: cacheId
    }

    /**
     * Calculate the cache identity for this row of the CSV, based purely on the information in the CSV.
     *
     * @param deserializer a row of deserialized values
     * @return the cache identity for the row
     */
    abstract fun getCacheId(deserializer: RowDeserializer): String
}
