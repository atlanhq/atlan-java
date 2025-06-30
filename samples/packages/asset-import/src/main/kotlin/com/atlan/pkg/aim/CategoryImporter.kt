/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.GlossaryCategory
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.GlossaryCategoryXformer.CATEGORY_DELIMITER
import com.atlan.pkg.serde.cell.GlossaryXformer.GLOSSARY_DELIMITER
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import mu.KLogger
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
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param logger through which to write log entries
 */
class CategoryImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    logger: KLogger,
) : GTCImporter(
        ctx = ctx,
        filename = filename,
        cache = ctx.categoryCache,
        typeNameFilter = GlossaryCategory.TYPE_NAME,
        logger = logger,
    ) {
    // Maximum depth of any category in the CSV -- will be updated on first pass through the CSV
    // file by includeRow() method
    private val maxCategoryDepth = AtomicInteger(1)
    private val secondPassRemain =
        setOf(
            GlossaryCategory.NAME.atlanFieldName,
            GlossaryCategory.PARENT_CATEGORY.atlanFieldName,
            GlossaryCategory.ANCHOR.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(GlossaryCategory.QUALIFIED_NAME.atlanFieldName)
        return super.importHierarchy(cache, typeNameFilter, colsToSkip, secondPassRemain, maxCategoryDepth)
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
        if (maxBound > row.size || CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) != typeNameFilter) {
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
        return CSVXformer.trimWhitespace(row.getOrElse(typeIdx) { "" }) == typeNameFilter
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
