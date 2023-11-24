/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.CategoryCache
import com.atlan.pkg.serde.cell.GlossaryCategoryXformer.CATEGORY_DELIMITER
import com.atlan.pkg.serde.cell.GlossaryXformer.GLOSSARY_DELIMITER
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.system.exitProcess

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
 */
class CategoryImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
) : GTCImporter(
    filename = filename,
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    cache = CategoryCache,
    typeNameFilter = GlossaryCategory.TYPE_NAME,
    logger = KotlinLogging.logger {},
) {
    private var levelToProcess = 0

    // Maximum depth of any category in the CSV -- will be updated on first pass through the CSV
    // file by includeRow() method
    private val maxCategoryDepth = AtomicInteger(1)

    /** {@inheritDoc} */
    override fun import() {
        cache.preload()
        // Import categories by level, top-to-bottom, and stop when we hit a level with no categories
        logger.info { "Loading categories in multiple passes, by level..." }
        while (levelToProcess < maxCategoryDepth.get()) {
            levelToProcess += 1
            logger.info { "--- Loading level $levelToProcess categories... ---" }
            CSVReader(filename, updateOnly).use { csv ->
                val start = System.currentTimeMillis()
                val anyFailures = csv.streamRows(this, batchSize, logger)
                logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
                if (anyFailures) {
                    logger.error { "Some errors detected, failing the workflow." }
                    exitProcess(1)
                }
                cacheCreated(csv.created)
            }
        }
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean {
        val nameIdx = header.indexOf(GlossaryCategory.NAME.atlanFieldName)
        val parentIdx = header.indexOf(GlossaryCategory.PARENT_CATEGORY.atlanFieldName)
        val anchorIdx = header.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)

        val maxBound = max(typeIdx, max(nameIdx, max(parentIdx, anchorIdx)))
        if (maxBound > row.size || row[typeIdx] != typeNameFilter) {
            // If any of the columns are beyond the size of the row, or the row
            // represents something other than a category, short-circuit
            return false
        }
        val categoryLevel = if (row[parentIdx].isBlank()) {
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
    override fun getCacheId(row: List<String>, header: List<String>): String {
        val nameIdx = header.indexOf(GlossaryCategory.NAME.atlanFieldName)
        val parentIdx = header.indexOf(GlossaryCategory.PARENT_CATEGORY.atlanFieldName)
        val anchorIdx = header.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)
        return if (nameIdx >= 0 && parentIdx >= 0 && anchorIdx >= 0) {
            val glossaryName = row[anchorIdx]
            val categoryPath = if (row[parentIdx].isBlank()) {
                row[nameIdx]
            } else {
                val parentPath = row[parentIdx].split(CATEGORY_DELIMITER)[0]
                "$parentPath$CATEGORY_DELIMITER${row[nameIdx]}"
            }
            "$categoryPath$GLOSSARY_DELIMITER$glossaryName"
        } else {
            ""
        }
    }
}
