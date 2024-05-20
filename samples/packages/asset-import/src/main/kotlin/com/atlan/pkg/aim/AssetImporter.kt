/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.Asset
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVImporter
import mu.KotlinLogging

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
 * @param creationHandling if assets are to be created, how they should be created (as full assets or only partial assets)
 * @param tableViewAgnostic if true, tables and views will be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa)
 * @param failOnErrors if true, fail if errors are encountered, otherwise continue processing
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class AssetImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val caseSensitive: Boolean = true,
    private val creationHandling: AssetCreationHandling = AssetCreationHandling.NONE,
    private val tableViewAgnostic: Boolean = false,
    private val failOnErrors: Boolean = true,
    private val trackBatches: Boolean = true,
    private val fieldSeparator: Char = ',',
) : CSVImporter(
    filename,
    logger = KotlinLogging.logger {},
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    caseSensitive = caseSensitive,
    creationHandling = creationHandling,
    tableViewAgnostic = tableViewAgnostic,
    failOnErrors = failOnErrors,
    trackBatches = trackBatches,
    fieldSeparator = fieldSeparator,
) {
    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val typeName = deserializer.typeName
        return FieldSerde.getBuilderForType(typeName).qualifiedName(deserializer.qualifiedName)
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean {
        return row.size >= typeIdx && row[typeIdx].isNotBlank()
    }
}
