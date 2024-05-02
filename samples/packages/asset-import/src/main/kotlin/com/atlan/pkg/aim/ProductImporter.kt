package com.atlan.pkg.aim

import com.atlan.model.assets.Asset
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.RowDeserializer
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
        val dataDomain = deserializer.getValue(DataProduct.DATA_DOMAIN.atlanFieldName)?.let { it as DataDomain }
        val dataProductAssetsDSL = deserializer.getValue(DataProduct.DATA_PRODUCT_ASSETS_DSL.atlanFieldName) as String
        return DataProduct.creator(name, dataDomain?.qualifiedName, dataProductAssetsDSL)
    }
}
