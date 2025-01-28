/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.cell.DataTypeXformer
import mu.KLogger

class ColumnXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : AssetXformer(
    ctx = ctx,
    typeNameFilter = Column.TYPE_NAME,
    preprocessedDetails = preprocessedDetails,
    logger = logger,
) {
    companion object {
        const val COLUMN_PARENT_QN = "columnParentQualifiedName"
        const val COLUMN_NAME = "columnName"
    }
    override fun mapAsset(
        inputRow: Map<String, String>,
    ): Map<String, String> {
        val connectionQN = getConnectionQN(ctx, inputRow)
        val details = getSQLHierarchyDetails(inputRow, typeNameFilter)
        val assetQN = "$connectionQN/${details.partialQN}"
        val parentQN = "$connectionQN/${details.parentPartialQN}"
        val rawDataType = trimWhitespace(inputRow.getOrElse(Column.DATA_TYPE.atlanFieldName) { "" })
        var precision: Int? = null
        var scale: Double? = null
        var maxLength: Long? = null
        if (rawDataType.isNotBlank()) {
            if (!rawDataType.contains("<") && !rawDataType.contains(">")) {
                // Only attempt to parse things like precision, scale and max-length if this is not a complex type
                precision = DataTypeXformer.getPrecision(rawDataType)
                scale = DataTypeXformer.getScale(rawDataType)
                maxLength = DataTypeXformer.getMaxLength(rawDataType)
            }
        }
        return if (assetQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to assetQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to typeNameFilter,
                RowSerde.getHeaderForField(Asset.NAME) to details.name,
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE) to getConnectorType(inputRow),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME) to connectionQN,
                RowSerde.getHeaderForField(Column.TABLE_NAME) to if (details.parentTypeName == Table.TYPE_NAME) details.parentName else "",
                RowSerde.getHeaderForField(Column.TABLE_QUALIFIED_NAME) to if (details.parentTypeName == Table.TYPE_NAME) parentQN else "",
                RowSerde.getHeaderForField(Column.TABLE) to if (details.parentTypeName == Table.TYPE_NAME) "${details.parentTypeName}@$parentQN" else "",
                RowSerde.getHeaderForField(Column.VIEW_NAME) to if (details.parentTypeName == View.TYPE_NAME || details.parentTypeName == MaterializedView.TYPE_NAME) details.parentName else "",
                RowSerde.getHeaderForField(Column.VIEW_QUALIFIED_NAME) to if (details.parentTypeName == View.TYPE_NAME || details.parentTypeName == MaterializedView.TYPE_NAME) parentQN else "",
                RowSerde.getHeaderForField(Column.VIEW) to if (details.parentTypeName == View.TYPE_NAME) "${details.parentTypeName}@$parentQN" else "",
                RowSerde.getHeaderForField(Column.MATERIALIZED_VIEW) to if (details.parentTypeName == MaterializedView.TYPE_NAME) "${details.parentTypeName}@$parentQN" else "",
                RowSerde.getHeaderForField(Column.ORDER) to inputRow.getOrElse(Column.ORDER.atlanFieldName) { "" },
                RowSerde.getHeaderForField(Column.RAW_DATA_TYPE_DEFINITION) to rawDataType,
                RowSerde.getHeaderForField(Column.PRECISION) to (precision?.toString() ?: ""),
                RowSerde.getHeaderForField(Column.NUMERIC_SCALE) to (scale?.toString() ?: ""),
                RowSerde.getHeaderForField(Column.MAX_LENGTH) to (maxLength?.toString() ?: ""),
            )
        } else {
            mapOf()
        }
    }
}
