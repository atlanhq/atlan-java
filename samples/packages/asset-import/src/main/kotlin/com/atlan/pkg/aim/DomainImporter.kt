/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.IDataMesh
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.DataDomainCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.DataDomainXformer.DATA_DOMAIN_DELIMITER
import com.atlan.pkg.serde.csv.ImportResults
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max


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
) : DDPImporter(
    filename = filename,
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    cache = DataDomainCache,
    typeNameFilter = DataDomain.TYPE_NAME,
    logger = KotlinLogging.logger {},
    fieldSeparator = fieldSeparator,
) {
    private var levelToProcess = 0

    // Maximum depth of any domain in the CSV -- will be updated on first pass through the CSV
    // file by includeRow() method
    private val maxCategoryDepth = AtomicInteger(1)

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        cache.preload()
        // Also ignore any inbound qualifiedName
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(DataDomain.QUALIFIED_NAME.atlanFieldName)

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
        val slug = IDataMesh.generateSlugForName(name)
        val parentDomain = deserializer.getValue(DataDomain.PARENT_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        val parentQualifiedName = if (parentDomain != null ) {
            cache.getByGuid(parentDomain.guid)?.qualifiedName
        } else {
            null
        }
        return DataDomain.creator(name, parentQualifiedName)
    }

    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String {
        val domainName = deserializer.getValue(DataDomain.NAME.atlanFieldName)
        val parentDomain = deserializer.getValue(DataDomain.PARENT_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        return if (parentDomain != null ) {
            val parentIdx = deserializer.heading.indexOf(DataDomain.PARENT_DOMAIN.atlanFieldName)
            val parentPath = deserializer.row[parentIdx]
            "${parentPath}$DATA_DOMAIN_DELIMITER$domainName"
        } else {
            "$domainName"
        }
    }
}
