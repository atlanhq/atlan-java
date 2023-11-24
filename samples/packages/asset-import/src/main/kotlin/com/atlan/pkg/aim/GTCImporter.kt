/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.Asset
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.aim.Importer.clearField
import com.atlan.pkg.cache.AssetCache
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import mu.KLogger
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
 * @param logger through which to log any problems
 */
abstract class GTCImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    protected val cache: AssetCache,
    protected val typeNameFilter: String,
    protected val logger: KLogger,
) : AssetGenerator {
    /**
     * Actually run the import.
     */
    open fun import() {
        cache.preload()
        CSVReader(filename, updateOnly).use { csv ->
            val start = System.currentTimeMillis()
            val anyFailures = csv.streamRows(this, batchSize, logger)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
            if (anyFailures) {
                logger.error { "Some errors detected, failing the workflow." }
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
    fun cacheCreated(map: Map<String, Asset>) {
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

    /**
     * Translate a row of CSV values into a term object, overwriting any attributes that were empty
     * in the CSV with blank values, per the job configuration.
     *
     * @param row of values in the CSV
     * @param header names of columns (and their position) in the header of the CSV
     * @param typeIdx numeric index of the column containing the typeName of the asset in the row
     * @param qnIdx numeric index of the column containing the qualifiedName of the asset in the row
     * @param skipColumns columns to skip, i.e. that need to be processed in a later pass
     * @return the deserialized asset object(s)
     */
    override fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int, skipColumns: Set<String>): RowDeserialization? {
        // Deserialize the objects represented in that row (could be more than one due to flattening
        // of in particular things like READMEs and Links)
        if (includeRow(row, header, typeIdx, qnIdx)) {
            val revisedRow = generateQualifiedName(row, header, typeIdx, qnIdx)
            val assets = RowDeserializer(
                heading = header,
                row = revisedRow,
                typeIdx = typeIdx,
                qnIdx = qnIdx,
                logger = logger,
                skipColumns = skipColumns,
            ).getAssets()
            if (assets != null) {
                val builder = assets.primary
                val candidate = builder.build()
                val identity = RowDeserialization.AssetIdentity(candidate.typeName, candidate.qualifiedName)
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
     * Calculate a fallback qualifiedName, if the qualifiedName value in this row is empty.
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @param qnIdx index of the qualifiedName
     * @return the original row of data with a qualifiedName filled in, if it was blank to begin with
     */
    fun generateQualifiedName(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): List<String> {
        val revised = mutableListOf<String>()
        for (i in row.indices) {
            when {
                i == qnIdx -> {
                    revised.add(
                        row[i].ifBlank {
                            val cacheId = getCacheId(row, header)
                            cache.getByIdentity(cacheId)?.qualifiedName ?: cacheId
                        },
                    )
                }
                else -> revised.add(row[i])
            }
        }
        return revised
    }

    /**
     * Calculate the cache identity for this row of the CSV, based purely on the information in the CSV.
     *
     * @param row of values
     * @param header column names
     * @return the cache identity for the row
     */
    abstract fun getCacheId(row: List<String>, header: List<String>): String
}
