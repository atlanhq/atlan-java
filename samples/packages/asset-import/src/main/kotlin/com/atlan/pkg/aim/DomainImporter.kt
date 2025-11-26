/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.DataDomainXformer.DATA_DOMAIN_DELIMITER
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import mu.KLogger
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Stream
import kotlin.math.max

/**
 * Import data domains (only) into Atlan from a provided CSV file.
 *
 * Only the data domains and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param logger through which to write log entries
 */
class DomainImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    logger: KLogger,
) : MeshImporter(
        ctx = ctx,
        filename = filename,
        cache = ctx.dataDomainCache,
        logger = logger,
        typeNameFilter = DataDomain.TYPE_NAME,
        trackBatches = true,
    ) {
    // Note: Always track batches (above) for domain importer, to ensure cache is managed

    // Maximum depth of any domain in the CSV -- will be updated on first pass through the CSV
    // file by includeRow() method
    private val maxDomainDepth = AtomicInteger(1)

    private val secondPassRemain =
        setOf(
            DataDomain.NAME.atlanFieldName,
            DataDomain.PARENT_DOMAIN.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun cacheCreated(list: Stream<Asset>) {
        // Cache any assets that were created by processing
        list.forEach { asset ->
            // We must look up the asset and then cache to ensure we have the necessary identity
            // characteristics and status
            cache.cacheById(asset.guid)
        }
    }

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Also ignore any inbound qualifiedName
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(DataDomain.QUALIFIED_NAME.atlanFieldName)
        colsToSkip.add(DataProduct.DATA_DOMAIN.atlanFieldName)
        val includes = preprocess()
        if (includes.hasLinks) {
            ctx.linkCache.preload()
        }
        if (includes.hasTermAssignments) {
            ctx.termCache.preload()
        }
        return super.importHierarchy(cache, typeNameFilter, colsToSkip, secondPassRemain, maxDomainDepth)
    }

    /** {@inheritDoc} */
    override fun includeRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): Boolean {
        val nameIdx = header.indexOf(DataDomain.NAME.atlanFieldName)
        val parentIdx = header.indexOf(DataDomain.PARENT_DOMAIN.atlanFieldName)

        val maxBound = max(typeIdx, max(nameIdx, parentIdx))
        if (maxBound > row.size || CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) != typeNameFilter) {
            // If any of the columns are beyond the size of the row, or the row
            // represents something other than a domain, short-circuit
            return false
        }
        val domainLevel =
            if (parentIdx < 0 || row[parentIdx].isBlank()) {
                1
            } else {
                row[parentIdx].split(DATA_DOMAIN_DELIMITER).size + 1
            }
        // Consider whether we need to update the maximum depth of categories we need to load
        val currentMax = maxDomainDepth.get()
        val maxDepth = max(domainLevel, currentMax)
        if (maxDepth > currentMax) {
            maxDomainDepth.set(maxDepth)
        }
        if (domainLevel != levelToProcess) {
            // If this data domain is a different level than we are currently processing,
            // short-circuit
            return false
        }
        return CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) == typeNameFilter
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(DataDomain.NAME.atlanFieldName) as String
        val parentDomainMinimal = deserializer.getValue(DataDomain.PARENT_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        val parentQualifiedName = if (parentDomainMinimal != null) ctx.dataDomainCache.getByGuid(parentDomainMinimal.guid)?.qualifiedName else null
        val qualifiedName = generateQualifiedName(deserializer)
        val candidateDD = DataDomain.creator(name, parentQualifiedName)
        return if (qualifiedName != getCacheId(deserializer)) {
            return candidateDD.qualifiedName(qualifiedName)
        } else {
            candidateDD
        }
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
    private fun getCacheId(deserializer: RowDeserializer): String {
        val domainName = deserializer.getValue(DataDomain.NAME.atlanFieldName)
        val parentDomain = deserializer.getValue(DataDomain.PARENT_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        return if (parentDomain != null) {
            val parentIdx = deserializer.heading.indexOf(DataDomain.PARENT_DOMAIN.atlanFieldName)
            val parentPath = CSVXformer.trimWhitespace(deserializer.row[parentIdx])
            "${parentPath}$DATA_DOMAIN_DELIMITER$domainName"
        } else {
            "$domainName"
        }
    }
}
