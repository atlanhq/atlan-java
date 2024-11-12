/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.CategoryCache
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.GlossaryCategoryXformer.CATEGORY_DELIMITER
import com.atlan.pkg.serde.cell.GlossaryXformer.GLOSSARY_DELIMITER
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

/**
 * Import categories (only) into Atlan from a provided CSV file.
 *
 * Only the categories and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 * @param failOnErrors if true, fail if errors are encountered, otherwise continue processing
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class CategoryImporter(
    filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val failOnErrors: Boolean,
    fieldSeparator: Char,
) : GTCImporter(
        filename = filename,
        attrsToOverwrite = attrsToOverwrite,
        updateOnly = updateOnly,
        batchSize = batchSize,
        cache = CategoryCache,
        typeNameFilter = GlossaryCategory.TYPE_NAME,
        logger = KotlinLogging.logger {},
        failOnErrors = failOnErrors,
        fieldSeparator = fieldSeparator,
    ) {
    private var levelToProcess = 0

    // Maximum depth of any category in the CSV -- will be updated on first pass through the CSV
    // file by includeRow() method
    private val maxCategoryDepth = AtomicInteger(1)

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        cache.preload()
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(GlossaryCategory.QUALIFIED_NAME.atlanFieldName)
        // Import categories by level, top-to-bottom, and stop when we hit a level with no categories
        logger.info { "Loading categories in multiple passes, by level..." }
        val individualResults = mutableListOf<ImportResults?>()
        while (levelToProcess < maxCategoryDepth.get()) {
            levelToProcess += 1
            logger.info { "--- Loading level $levelToProcess categories... ---" }
            val results = super.import(colsToSkip)
            individualResults.add(results)
        }
        return ImportResults.combineAll(true, *individualResults.toTypedArray())
    }

    /** {@inheritDoc} */
    override fun includeRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): Boolean {
        val nameIdx = header.indexOf(GlossaryCategory.NAME.atlanFieldName)
        val parentIdx = header.indexOf(GlossaryCategory.PARENT_CATEGORY.atlanFieldName)
        val anchorIdx = header.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)

        val maxBound = max(typeIdx, max(nameIdx, max(parentIdx, anchorIdx)))
        if (maxBound > row.size || row[typeIdx] != typeNameFilter) {
            // If any of the columns are beyond the size of the row, or the row
            // represents something other than a category, short-circuit
            return false
        }
        val categoryLevel =
            if (row[parentIdx].isBlank()) {
                1
            } else {
                val parentPath = row[parentIdx].split(GLOSSARY_DELIMITER)[0]
                parentPath.split(CATEGORY_DELIMITER).size + 1
            }
        // Consider whether we need to update the maximum depth of categories we need to load
        val currentMax = maxCategoryDepth.get()
        val maxDepth = max(categoryLevel, currentMax)
        if (maxDepth > currentMax) {
            maxCategoryDepth.set(maxDepth)
        }
        if (categoryLevel != levelToProcess) {
            // If this category is a different level than we are currently processing,
            // short-circuit
            return false
        }
        return row[typeIdx] == typeNameFilter
    }

    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String {
        val glossaryIdx = deserializer.heading.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)
        val parentCategory = deserializer.getValue(GlossaryCategory.PARENT_CATEGORY.atlanFieldName)?.let { it as GlossaryCategory }
        val categoryName = deserializer.getValue(GlossaryCategory.NAME.atlanFieldName)?.let { it as String } ?: ""
        return if (glossaryIdx >= 0 && categoryName.isNotBlank()) {
            val glossaryName = CSVXformer.trimWhitespace(deserializer.row[glossaryIdx].ifBlank { "" })
            val categoryPath =
                if (parentCategory == null) {
                    categoryName
                } else {
                    val parentIdx = deserializer.heading.indexOf(GlossaryCategory.PARENT_CATEGORY.atlanFieldName)
                    val parentPath = CSVXformer.trimWhitespace(deserializer.row[parentIdx].split(GLOSSARY_DELIMITER)[0])
                    "$parentPath$CATEGORY_DELIMITER$categoryName"
                }
            "$categoryPath$GLOSSARY_DELIMITER$glossaryName"
        } else {
            ""
        }
    }
}
