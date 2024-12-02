/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.DataTypeXformer
import com.atlan.pkg.util.DeltaProcessor
import mu.KLogger

/**
 * Import columns into Atlan from a provided CSV file.
 *
 * Only the columns and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context through which this package is running
 * @param delta the processor containing any details about file deltas
 * @param preprocessed details of the preprocessed CSV file
 * @param connectionImporter that was used to import connections
 * @param logger through which to record logging
 */
class ColumnImporter(
    ctx: PackageContext<RelationalAssetsBuilderCfg>,
    private val delta: DeltaProcessor,
    private val preprocessed: Importer.Results,
    private val connectionImporter: ConnectionImporter,
    logger: KLogger,
) : AssetImporter(
        ctx = ctx,
        delta = delta,
        filename = preprocessed.preprocessedFile,
        typeNameFilter = Column.TYPE_NAME,
        logger = logger,
    ) {
    companion object {
        const val COLUMN_PARENT_QN = "columnParentQualifiedName"
        const val COLUMN_NAME = "columnName"
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(COLUMN_NAME)?.let { it as String } ?: ""
        val order = deserializer.getValue(Column.ORDER.atlanFieldName)?.let { it as Int } ?: 0
        val qnDetails = getQualifiedNameDetails(deserializer.row, deserializer.heading, typeNameFilter)
        val connectionQN = connectionImporter.getBuilder(deserializer).build().qualifiedName
        val parentQN = "$connectionQN/${qnDetails.parentPartialQN}"
        val parentType = preprocessed.entityQualifiedNameToType[qnDetails.parentUniqueQN] ?: throw IllegalStateException("Could not find any table/view at: ${qnDetails.parentUniqueQN}")
        val builder = Column.creator(name, parentType, parentQN, order)
        val rawDataType = deserializer.getRawValue(Column.DATA_TYPE.atlanFieldName)
        if (rawDataType.isNotBlank()) {
            builder.rawDataTypeDefinition(rawDataType)
            if (!rawDataType.contains("<") && !rawDataType.contains(">")) {
                // Only attempt to parse things like precision, scale and max-length if this is not a complex type
                DataTypeXformer.getPrecision(rawDataType)?.let { builder.precision(it) }
                DataTypeXformer.getScale(rawDataType)?.let { builder.numericScale(it) }
                DataTypeXformer.getMaxLength(rawDataType)?.let { builder.maxLength(it) }
            }
        }
        return builder
    }
}
