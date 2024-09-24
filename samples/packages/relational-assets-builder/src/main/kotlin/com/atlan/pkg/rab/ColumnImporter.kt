/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.enums.AssetCreationHandling
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
 * @param creationHandling what to do with assets that do not exist (create full, partial, or ignore)
 * @param batchSize maximum number of records to save per API request
 * @param connectionImporter that was used to import connections
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class ColumnImporter(
    private val preprocessed: Importer.Results,
    private val attrsToOverwrite: List<AtlanField>,
    private val creationHandling: AssetCreationHandling,
    private val batchSize: Int,
    private val connectionImporter: ConnectionImporter,
    trackBatches: Boolean,
    fieldSeparator: Char,
) : AssetImporter(
        preprocessed.preprocessedFile,
        attrsToOverwrite,
        creationHandling,
        batchSize,
        Column.TYPE_NAME,
        KotlinLogging.logger {},
        trackBatches,
        fieldSeparator,
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
