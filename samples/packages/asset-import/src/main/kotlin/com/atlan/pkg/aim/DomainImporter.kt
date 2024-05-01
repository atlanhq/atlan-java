/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.DataDomainCache
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.DataDomainXformer.DATA_DOMAIN_DELIMITER
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.ImportResults
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.text.Regex

/**
 * Import data domains (only) into Atlan from a provided CSV file.
 *
 * Only the data domains and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class DomainImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val fieldSeparator: Char,
) : CSVImporter(
    filename,
    logger = KotlinLogging.logger {},
    typeNameFilter = DataDomain.TYPE_NAME,
    attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    trackBatches = true, // Always track batches for GTC importers, to ensure cache is managed
    fieldSeparator = fieldSeparator,
) {
    private var levelToProcess = 0

    // Maximum depth of any domain in the CSV -- will be updated on first pass through the CSV
    // file by includeRow() method
    private val maxCategoryDepth = AtomicInteger(1)

    private val cache = DataDomainCache

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
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        cache.preload()
        // Also ignore any inbound qualifiedName
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(DataDomain.QUALIFIED_NAME.atlanFieldName)
        colsToSkip.add(DataProduct.DATA_DOMAIN.atlanFieldName)

        logger.info { "Loading domains in multiple passes, by level..." }
        var combinedResults: ImportResults? = null
        while (levelToProcess < maxCategoryDepth.get()) {
            levelToProcess += 1
            logger.info { "--- Loading level $levelToProcess domains... ---" }
            val results = super.import(colsToSkip)
            if (combinedResults == null) {
                combinedResults = results
            } else if (results != null) {
                combinedResults = combinedResults.combinedWith(results)
            }
        }
        return combinedResults
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean {
        val nameIdx = header.indexOf(DataDomain.NAME.atlanFieldName)
        val parentIdx = header.indexOf(DataDomain.PARENT_DOMAIN.atlanFieldName)

        val maxBound = max(typeIdx, max(nameIdx, parentIdx))
        if (maxBound > row.size || row[typeIdx] != typeNameFilter) {
            // If any of the columns are beyond the size of the row, or the row
            // represents something other than a domain, short-circuit
            return false
        }
        val domainLevel = if (row[parentIdx].isBlank()) {
            1
        } else {
            row[parentIdx].split(DATA_DOMAIN_DELIMITER).size + 1
        }
        // Consider whether we need to update the maximum depth of categories we need to load
        val currentMax = maxCategoryDepth.get()
        val maxDepth = max(domainLevel, currentMax)
        if (maxDepth > currentMax) {
            maxCategoryDepth.set(maxDepth)
        }
        if (domainLevel != levelToProcess) {
            // If this category is a different level than we are currently processing,
            // short-circuit
            return false
        }
        return row[typeIdx] == typeNameFilter
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(DataDomain.NAME.atlanFieldName) as String
        val parentDomain = deserializer.getValue(DataDomain.PARENT_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        val parentQualifiedName = parentDomain?.qualifiedName
        var builder = DataDomain.creator(name, parentQualifiedName)
        if (parentQualifiedName != null) {
            val regex = Regex("^[^/]+[/][^/]+[/][^/]+")
            val match = regex.find(parentQualifiedName)
            val superDomainQualifiedName = match?.groups?.first()?.value
            if (superDomainQualifiedName != null) {
                builder = builder.superDomainQualifiedName(superDomainQualifiedName)
            }
        }
        return builder
    }

    /**
     * Calculate the cache identity for this row of the CSV, based purely on the information in the CSV.
     *
     * @param deserializer a row of deserialized values
     * @return the cache identity for the row
     */
    fun getCacheId(deserializer: RowDeserializer): String {
        val domainName = deserializer.getValue(DataDomain.NAME.atlanFieldName)
        val parentDomain = deserializer.getValue(DataDomain.PARENT_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        return if (parentDomain != null) {
            val parentIdx = deserializer.heading.indexOf(DataDomain.PARENT_DOMAIN.atlanFieldName)
            val parentPath = deserializer.row[parentIdx]
            "${parentPath}$DATA_DOMAIN_DELIMITER$domainName"
        } else {
            "$domainName"
        }
    }
}
