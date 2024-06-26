/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import com.atlan.model.assets.Asset
import com.atlan.model.assets.CubeField
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.ImportResults
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max

/**
 * Import cube fields into Atlan from a provided CSV file.
 *
 * Only the cube fields and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular cube field's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that cube field's field to getAttributesToOverwrite.
 *
 * @param preprocessed details of the preprocessed CSV file
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param creationHandling what to do with assets that do not exist (create full, partial, or ignore)
 * @param batchSize maximum number of records to save per API request
 * @param connectionImporter that was used to import connections
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class FieldImporter(
    private val preprocessed: Importer.PreprocessedCsv,
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
    CubeField.TYPE_NAME,
    KotlinLogging.logger {},
    trackBatches,
    fieldSeparator,
) {
    private var levelToProcess = 0L

    // Maximum depth of any field in the CSV -- will be updated on first pass through the CSV
    // file by includeRow() method
    private val maxFieldLevel = AtomicLong(1)

    companion object {
        const val PARENT_FIELD_QN = "parentFieldQualifiedName"
        const val FIELD_NAME = "fieldName"
    }

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Import fields by level, top-to-bottom, and stop when we hit a level with no fields
        logger.info { "Loading fields in multiple passes, by level..." }
        var combinedResults: ImportResults? = null
        while (levelToProcess < maxFieldLevel.get()) {
            levelToProcess += 1
            logger.info { "--- Loading level $levelToProcess fields... ---" }
            val results = super.import(columnsToSkip)
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
        val nameIdx = header.indexOf(FIELD_NAME)
        val parentIdx = header.indexOf(PARENT_FIELD_QN)

        val maxBound = max(typeIdx, max(nameIdx, parentIdx))
        if (maxBound > row.size || row[typeIdx] != typeNameFilter) {
            // If any of the columns are beyond the size of the row, or the row
            // represents something other than a field, short-circuit
            return false
        }
        val fieldLevel = if (row[parentIdx].isBlank()) {
            1L
        } else {
            val parentPath = row[parentIdx].split(Importer.QN_DELIMITER)
            (parentPath.size + 1).toLong()
        }
        // Consider whether we need to update the maximum depth of categories we need to load
        val currentMax = maxFieldLevel.get()
        val maxDepth = max(fieldLevel, currentMax)
        if (maxDepth > currentMax) {
            maxFieldLevel.set(maxDepth)
        }
        if (fieldLevel != levelToProcess) {
            // If this category is a different level than we are currently processing,
            // short-circuit
            return false
        }
        return row[typeIdx] == typeNameFilter
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(FIELD_NAME)?.let { it as String } ?: ""
        val connectionQN = connectionImporter.getBuilder(deserializer).build().qualifiedName
        val qnDetails = getQualifiedNameDetails(deserializer.row, deserializer.heading, typeNameFilter)
        val parentQN = "$connectionQN/${qnDetails.parentPartialQN}"
        return CubeField.creator(name, parentQN)
            .cubeFieldLevel(levelToProcess)
            .cubeSubFieldCount(preprocessed.qualifiedNameToChildCount[qnDetails.uniqueQN]?.toLong())
    }
}
