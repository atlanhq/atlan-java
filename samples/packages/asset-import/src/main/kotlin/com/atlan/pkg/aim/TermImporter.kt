/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.TermCache
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.GlossaryTermXformer
import com.atlan.pkg.serde.cell.GlossaryXformer
import com.atlan.pkg.serde.csv.ImportResults
import mu.KotlinLogging

/**
 * Import glossaries (only) into Atlan from a provided CSV file.
 *
 * Only the glossaries and attributes in the provided CSV file will attempt to be loaded.
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
class TermImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val failOnErrors: Boolean,
    private val fieldSeparator: Char,
) : GTCImporter(
    filename = filename,
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    cache = TermCache,
    typeNameFilter = GlossaryTerm.TYPE_NAME,
    logger = KotlinLogging.logger {},
    failOnErrors = failOnErrors,
    fieldSeparator = fieldSeparator,
) {
    private val secondPassIgnore = setOf(
        GlossaryTerm.LINKS.atlanFieldName,
        GlossaryTerm.ATLAN_TAGS.atlanFieldName,
        GlossaryTerm.README.atlanFieldName,
    )

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        cache.preload()
        val firstPassSkip = columnsToSkip.toMutableSet()
        firstPassSkip.add(GlossaryTerm.QUALIFIED_NAME.atlanFieldName)
        firstPassSkip.addAll(GlossaryTermXformer.TERM_TO_TERM_FIELDS)
        // Import categories by level, top-to-bottom, and stop when we hit a level with no categories
        logger.info { "--- Loading terms in first pass, without term-to-term relationships... ---" }
        val firstPassResults = super.import(firstPassSkip)
        return if (firstPassResults != null) {
            val secondPassSkip = columnsToSkip.toMutableSet()
            secondPassSkip.add(GlossaryTerm.QUALIFIED_NAME.atlanFieldName)
            secondPassSkip.addAll(secondPassIgnore)
            // In this second pass we need to ignore fields that were loaded in the first pass,
            // or we will end up with duplicates (links) or extra audit log messages (tags, README)
            logger.info { "--- Loading term-to-term relationships (second pass)... ---" }
            val secondPassResults = super.import(secondPassSkip)
            if (secondPassResults != null) {
                firstPassResults.combinedWith(secondPassResults)
            } else {
                firstPassResults
            }
        } else {
            null
        }
    }

    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String {
        val glossaryIdx = deserializer.heading.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)
        val termName = deserializer.getValue(GlossaryTerm.NAME.atlanFieldName)?.let { it as String } ?: ""
        return if (glossaryIdx >= 0) {
            val glossaryName = deserializer.row[glossaryIdx].ifBlank { "" }
            if (glossaryName.isNotBlank() && termName.isNotBlank()) {
                "$termName${GlossaryXformer.GLOSSARY_DELIMITER}$glossaryName"
            } else {
                ""
            }
        } else {
            ""
        }
    }
}
