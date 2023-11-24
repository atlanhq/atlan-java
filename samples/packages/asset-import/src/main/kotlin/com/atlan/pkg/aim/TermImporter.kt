/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.TermCache
import com.atlan.pkg.serde.cell.GlossaryTermXformer
import com.atlan.pkg.serde.cell.GlossaryXformer
import mu.KotlinLogging
import kotlin.system.exitProcess

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
 */
class TermImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
) : GTCImporter(
    filename = filename,
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    cache = TermCache,
    typeNameFilter = GlossaryTerm.TYPE_NAME,
    logger = KotlinLogging.logger {},
) {
    private val secondPassIgnore = setOf(
        GlossaryTerm.LINKS.atlanFieldName,
        GlossaryTerm.ATLAN_TAGS.atlanFieldName,
        GlossaryTerm.README.atlanFieldName,
    )

    /** {@inheritDoc} */
    override fun import() {
        cache.preload()
        // Import categories by level, top-to-bottom, and stop when we hit a level with no categories
        logger.info { "--- Loading terms in first pass, without term-to-term relationships... ---" }
        CSVReader(filename, updateOnly).use { csv ->
            val start = System.currentTimeMillis()
            val anyFailures = csv.streamRows(this, batchSize, logger, GlossaryTermXformer.TERM_TO_TERM_FIELDS)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
            if (anyFailures) {
                logger.error { "Some errors detected, failing the workflow." }
                exitProcess(1)
            }
            cacheCreated(csv.created)
        }
        // In this second pass we need to ignore fields that were loaded in the first pass,
        // or we will end up with duplicates (links) or extra audit log messages (tags, README)
        logger.info { "--- Loading term-to-term relationships (second pass)... ---" }
        CSVReader(filename, updateOnly).use { csv ->
            val start = System.currentTimeMillis()
            val anyFailures = csv.streamRows(this, batchSize, logger, secondPassIgnore)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
            if (anyFailures) {
                logger.error { "Some errors detected, failing the workflow." }
                exitProcess(1)
            }
        }
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): Boolean {
        return row[typeIdx] == typeNameFilter
    }

    /** {@inheritDoc} */
    override fun getCacheId(row: List<String>, header: List<String>): String {
        val nameIdx = header.indexOf(GlossaryTerm.NAME.atlanFieldName)
        val anchorIdx = header.indexOf(GlossaryTerm.ANCHOR.atlanFieldName)
        return if (nameIdx >= 0 && anchorIdx >= 0) {
            val glossaryName = row[anchorIdx]
            val termName = row[nameIdx]
            "$termName${GlossaryXformer.GLOSSARY_DELIMITER}$glossaryName"
        } else {
            ""
        }
    }
}
