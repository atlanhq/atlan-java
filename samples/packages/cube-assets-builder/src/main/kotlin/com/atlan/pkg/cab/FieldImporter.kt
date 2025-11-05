/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.CubeField
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.DeltaProcessor
import com.atlan.util.StringUtils
import mu.KLogger
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
 * @param ctx context in which this package is running
 * @param delta the processor containing any details about file deltas
 * @param preprocessed details of the preprocessed CSV file
 * @param connectionImporter that was used to import connections
 * @param logger through which to record any logging
 */
class FieldImporter(
    ctx: PackageContext<CubeAssetsBuilderCfg>,
    private val delta: DeltaProcessor,
    private val preprocessed: Importer.Results,
    private val connectionImporter: ConnectionImporter,
    logger: KLogger,
) : AssetImporter(
        ctx = ctx,
        delta = delta,
        filename = preprocessed.preprocessedFile,
        typeNameFilter = CubeField.TYPE_NAME,
        logger = logger,
    ) {
    private val leafNodeLevel = 1L
    private var generationToProcess = 0L

    // Maximum depth of any field in the CSV (overall and by hierarchy)
    private val maxFieldGeneration = AtomicLong(1)
    private val maxLevelByPath: MutableMap<String, AtomicLong> = mutableMapOf()

    companion object {
        const val PARENT_FIELD_QN = "parentFieldQualifiedName"
        const val FIELD_NAME = "fieldName"
        const val CUBE_NAME = "cubeName"
        const val DIMENSION_NAME = "cubeDimensionName"
        const val HIERARCHY_NAME = "cubeHierarchyName"
    }

    /** {@inheritDoc} */
    override fun preprocessRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): List<String> {
        if (CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) == typeNameFilter) {
            // Only build up the details if this is in fact a field row
            val hierarchyPath = getHierarchyPath(row, header)
            val path = getFieldPath(hierarchyPath, row, header)
            if (!maxLevelByPath.containsKey(path)) {
                // If path not yet seen, treat it as a leaf (for now)
                maxLevelByPath[path] = AtomicLong(leafNodeLevel)
            }
            bubbleUpParentLevel(path, hierarchyPath)
            // Consider whether we need to update the maximum depth of fields we need to load
            val currentMax = maxFieldGeneration.get()
            val fieldGeneration = getFieldGeneration(row, header)
            val maxDepth = max(fieldGeneration, currentMax)
            if (maxDepth > currentMax) {
                maxFieldGeneration.set(maxDepth)
            }
        }
        return row
    }

    /**
     * Recursively bubble-up setting the parent level(s) based on lower-field level updates.
     *
     * @param path of the field from which to bubble up levels
     * @param hierarchyPath path of the hierarchy for the field
     */
    private fun bubbleUpParentLevel(
        path: String,
        hierarchyPath: String,
    ) {
        if (path != hierarchyPath) { // Short-circuit once we reach hierarchy level (no need to bubble up further)
            val levelFromThisChild = maxLevelByPath[path]!!.get() + 1
            val parentPath = StringUtils.getParentQualifiedNameFromQualifiedName(path, Importer.QN_DELIMITER)
            if (parentPath != null) {
                val currentParentLevel = maxLevelByPath[parentPath]?.get() ?: leafNodeLevel
                // Logic for level calculation:
                //  - If there are no children, level = 0
                //  - Else level = max(child) + 1
                if (levelFromThisChild >= currentParentLevel) {
                    if (maxLevelByPath.containsKey(parentPath)) {
                        maxLevelByPath[parentPath]!!.set(levelFromThisChild)
                    } else {
                        maxLevelByPath[parentPath] = AtomicLong(levelFromThisChild)
                    }
                }
                bubbleUpParentLevel(parentPath, hierarchyPath)
            }
        }
    }

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Import fields by generation, top-to-bottom, and stop when we hit a generation with no fields
        logger.info { "Loading fields in multiple passes, by generation..." }
        val individualResults = mutableListOf<ImportResults>()
        while (generationToProcess < maxFieldGeneration.get()) {
            generationToProcess += 1
            logger.info { "--- Loading generation $generationToProcess fields... ---" }
            val results = super.import(columnsToSkip)
            if (results != null) individualResults.add(results)
        }
        return ImportResults.combineAll(ctx.client, true, *individualResults.toTypedArray())
    }

    /** {@inheritDoc} */
    override fun includeRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): Boolean {
        if (super.includeRow(row, header, typeIdx, qnIdx)) {
            val nameIdx = header.indexOf(FIELD_NAME)
            val parentIdx = header.indexOf(PARENT_FIELD_QN)

            val maxBound = max(typeIdx, max(nameIdx, parentIdx))
            if (maxBound > row.size || CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) != typeNameFilter) {
                // If any of the columns are beyond the size of the row, or the row
                // represents something other than a field, short-circuit
                return false
            }
            val fieldGeneration = getFieldGeneration(row, header)
            if (fieldGeneration != generationToProcess) {
                // If this field is a different generation than we are currently processing,
                // short-circuit
                return false
            }
            return CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) == typeNameFilter
        }
        return false
    }

    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(FIELD_NAME)?.let { it as String } ?: ""
        val connectionQN = connectionImporter.getBuilder(deserializer).build().qualifiedName
        val qnDetails = getQualifiedNameDetails(deserializer.row, deserializer.heading, typeNameFilter)
        val parentQN = "$connectionQN/${qnDetails.parentPartialQN}"
        val level = getFieldLevel(deserializer.row, deserializer.heading)
        return CubeField
            .creator(name, parentQN)
            .cubeFieldLevel(level)
            .cubeFieldGeneration(generationToProcess)
            .cubeSubFieldCount(preprocessed.qualifiedNameToChildCount[qnDetails.uniqueQN]?.toLong())
    }

    /**
     * Calculate the generation of the field in a given row of the CSV.
     *
     * @param row of values in the CSV
     * @param header names of columns for the CSV
     * @return numeric generation of the (nested) field
     */
    private fun getFieldGeneration(
        row: List<String>,
        header: List<String>,
    ): Long {
        val parentIdx = header.indexOf(PARENT_FIELD_QN)
        val parent = if (parentIdx >= 0) row[parentIdx] else ""
        return if (parent.isBlank()) {
            1L
        } else {
            val parentPath = parent.split(Importer.QN_DELIMITER)
            (parentPath.size + 1).toLong()
        }
    }

    /**
     * Calculate the level of the field in a given row of the CSV.
     *
     * @param row of values in the CSV
     * @param header names of columns for the CSV
     * @return numeric level of the (nested) field
     */
    private fun getFieldLevel(
        row: List<String>,
        header: List<String>,
    ): Long {
        val path = getFieldPath(getHierarchyPath(row, header), row, header)
        return maxLevelByPath[path]?.get() ?: leafNodeLevel
    }

    /**
     * Calculate the full path for the field on the provided row.
     *
     * @param hierarchyPath path of the hierarchy for the field
     * @param row of values in the CSV
     * @param header names of columns for the CSV
     * @return unique path for the field on the row
     */
    private fun getFieldPath(
        hierarchyPath: String,
        row: List<String>,
        header: List<String>,
    ): String {
        val parentIdx = header.indexOf(PARENT_FIELD_QN)
        val nameIdx = header.indexOf(FIELD_NAME)
        val parentPath = if (parentIdx >= 0) row[parentIdx] else ""
        return if (parentPath.isBlank()) {
            "$hierarchyPath${Importer.QN_DELIMITER}${row[nameIdx]}"
        } else {
            "$hierarchyPath${Importer.QN_DELIMITER}$parentPath${Importer.QN_DELIMITER}${row[nameIdx]}"
        }
    }

    /**
     * Calculate the hierarchy path for the field on the provided row.
     *
     * @param row of values in the CSV
     * @param header names of columns for the CSV
     * @return unique path for the hierarchy of the field on the row
     */
    private fun getHierarchyPath(
        row: List<String>,
        header: List<String>,
    ): String {
        val cubeIdx = header.indexOf(CUBE_NAME)
        val dimIdx = header.indexOf(DIMENSION_NAME)
        val hierIdx = header.indexOf(HIERARCHY_NAME)
        return "${row[cubeIdx]}${Importer.QN_DELIMITER}${row[dimIdx]}${Importer.QN_DELIMITER}${row[hierIdx]}"
    }
}
