package com.atlan.pkg.aim

import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.DataDomainCache
import com.atlan.pkg.cache.DataProductCache
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.DataDomainXformer
import com.atlan.pkg.serde.cell.DataDomainXformer.DATA_PRODUCT_DELIMITER
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.ImportResults
import mu.KotlinLogging

class ProductImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val fieldSeparator: Char,
) : CSVImporter(
    filename = filename,
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    typeNameFilter = DataProduct.TYPE_NAME,
    logger = KotlinLogging.logger {},
    fieldSeparator = fieldSeparator,
) {

    private val cache = DataProductCache

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Also ignore any inbound qualifiedName
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(DataProduct.QUALIFIED_NAME.atlanFieldName)
        colsToSkip.add(DataDomain.PARENT_DOMAIN.atlanFieldName)
        colsToSkip.add(DataDomain.ASSET_ICON.atlanFieldName)
        colsToSkip.add(DataDomain.ASSET_THEME_HEX.atlanFieldName)
        return super.import(colsToSkip)
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(DataProduct.NAME.atlanFieldName) as String
        val dataDomainMinimal = deserializer.getValue(DataProduct.DATA_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        val dataDomain = if (dataDomainMinimal != null) DataDomainCache.getByGuid(dataDomainMinimal.guid) as DataDomain else null
        val dataProductAssetsDSL = deserializer.getValue(DataProduct.DATA_PRODUCT_ASSETS_DSL.atlanFieldName) as String?
        val qualifiedName = generateQualifiedName(deserializer, dataDomain)
        val candidateDP = DataProduct.creator(name, dataDomain?.qualifiedName, dataProductAssetsDSL)
        return if (qualifiedName != getCacheId(deserializer, dataDomain)) {
            // If there is an existing qualifiedName, use it, otherwise we will get a conflict exception
            candidateDP.qualifiedName(qualifiedName)
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
    private fun generateQualifiedName(deserializer: RowDeserializer, dataDomain: DataDomain?): String {
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
    private fun getCacheId(deserializer: RowDeserializer, dataDomain: DataDomain?): String {
        val productName = deserializer.getValue(DataProduct.NAME.atlanFieldName)
        return if (dataDomain != null) {
            "${productName}$DATA_PRODUCT_DELIMITER${DataDomainXformer.encode(dataDomain)}"
        } else {
            "$productName"
        }
    }
}
