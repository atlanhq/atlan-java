/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.DataDomainXformer
import com.atlan.pkg.serde.cell.DataDomainXformer.DATA_PRODUCT_DELIMITER
import com.atlan.pkg.serde.csv.ImportResults
import mu.KLogger

/**
 * Import data products (only) into Atlan from a provided CSV file.
 *
 * Only the data products and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param logger through which to write log entries
 */
class ProductImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    logger: KLogger,
) : MeshImporter(
        ctx = ctx,
        filename = filename,
        cache = ctx.dataProductCache,
        logger = logger,
        typeNameFilter = DataProduct.TYPE_NAME,
    ) {
    private val secondPassRemain =
        setOf(
            Asset.NAME.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Also ignore any inbound qualifiedName
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(DataProduct.QUALIFIED_NAME.atlanFieldName)
        colsToSkip.add(DataDomain.PARENT_DOMAIN.atlanFieldName)
        colsToSkip.add(DataDomain.ASSET_ICON.atlanFieldName)
        colsToSkip.add(DataDomain.ASSET_THEME_HEX.atlanFieldName)
        val includes = preprocess()
        if (includes.hasLinks) {
            ctx.linkCache.preload()
        }
        if (includes.hasTermAssignments) {
            ctx.termCache.preload()
        }
        return super.import(typeNameFilter, colsToSkip, secondPassRemain, cache)
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(DataProduct.NAME.atlanFieldName) as String
        val dataDomainMinimal = deserializer.getValue(DataProduct.DATA_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        if (dataDomainMinimal == null) {
            throw NoSuchElementException("No dataDomain provided for the data product, cannot be processed.")
        }
        val dataDomain = ctx.dataDomainCache.getByGuid(dataDomainMinimal.guid) ?: throw NoSuchElementException("dataDomain not found for the data product, cannot be processed: ${deserializer.getRawValue(DataProduct.DATA_DOMAIN.atlanFieldName)}")
        val dataProductAssetsDSL = deserializer.getValue(DataProduct.DATA_PRODUCT_ASSETS_DSL.atlanFieldName) as String?
        val qualifiedName = generateQualifiedName(deserializer, dataDomain)
        val candidateDP = DataProduct.creator(name, dataDomain.qualifiedName, dataProductAssetsDSL)
        return if (qualifiedName != getCacheId(deserializer, dataDomain)) {
            // If there is an existing qualifiedName, use it, otherwise we will get a conflict exception
            // AND also clear out the default "ACTIVE" status that was set by the creator (so we don't
            // clobber whatever the existing status is)
            candidateDP
                .qualifiedName(qualifiedName)
                .daapStatus(null)
                .dataProductStatus(null)
        } else {
            // If there is no existing qualifiedName, we MUST use the generated qualifiedName,
            // otherwise we will get a permission exception
            candidateDP
        }
    }

    /**
     * Determine the qualifiedName for the data product, irrespective of whether it is
     * present in the input file or not. Since these qualifiedNames are generated, and the object may
     * have been created in a previous pass (and cached), we can resolve to its known qualifiedName
     * here based on the information in the row of the input file.
     *
     * @param deserializer a row of deserialized values
     * @param dataDomain domain in which this data product should be contained
     * @return the qualifiedName, calculated from the deserialized values
     */
    private fun generateQualifiedName(
        deserializer: RowDeserializer,
        dataDomain: DataDomain?,
    ): String {
        val cacheId = getCacheId(deserializer, dataDomain)
        return cache.getByIdentity(cacheId)?.qualifiedName ?: cacheId
    }

    /**
     * Calculate the cache identity for this row of the CSV, based purely on the information in the CSV.
     *
     * @param deserializer a row of deserialized values
     * @param dataDomain domain in which this data product should be contained
     * @return the cache identity for the row
     */
    private fun getCacheId(
        deserializer: RowDeserializer,
        dataDomain: DataDomain?,
    ): String {
        val productName = deserializer.getValue(DataProduct.NAME.atlanFieldName)
        return if (dataDomain != null) {
            "${productName}$DATA_PRODUCT_DELIMITER${DataDomainXformer.encode(ctx, dataDomain)}"
        } else {
            "$productName"
        }
    }
}
