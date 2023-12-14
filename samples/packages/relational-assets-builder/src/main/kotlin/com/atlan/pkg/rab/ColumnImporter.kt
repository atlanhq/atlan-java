/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.RowDeserializer
import mu.KotlinLogging

/**
 * Import columns into Atlan from a provided CSV file.
 *
 * Only the columns and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param preprocessed details of the preprocessed CSV file
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param connectionImporter that was used to import connections
 */
class ColumnImporter(
    private val preprocessed: Importer.PreprocessedCsv,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val connectionImporter: ConnectionImporter,
) : AssetImporter(
    preprocessed.preprocessedFile,
    attrsToOverwrite,
    updateOnly,
    batchSize,
    Column.TYPE_NAME,
    KotlinLogging.logger {},
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
        return Column.creator(name, parentType, parentQN, order)
    }
}
