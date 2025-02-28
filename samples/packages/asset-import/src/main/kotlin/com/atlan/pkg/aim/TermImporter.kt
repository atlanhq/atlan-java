/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.serde.cell.GlossaryTermXformer
import com.atlan.pkg.serde.cell.GlossaryXformer
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import mu.KLogger

/**
 * Import glossaries (only) into Atlan from a provided CSV file.
 *
 * Only the glossaries and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which the package is running
 * @param filename name of the file to import
 * @param logger through which to write log entries
 */
class TermImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    logger: KLogger,
) : GTCImporter(
        ctx = ctx,
        filename = filename,
        cache = ctx.termCache,
        typeNameFilter = GlossaryTerm.TYPE_NAME,
        logger = logger,
    ) {
    private val secondPassIgnore =
        setOf(
            GlossaryTerm.LINKS.atlanFieldName,
            GlossaryTerm.ATLAN_TAGS.atlanFieldName,
            GlossaryTerm.README.atlanFieldName,
            GlossaryTerm.ASSIGNED_ENTITIES.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        cache.preload()
        val firstPassSkip = columnsToSkip.toMutableSet()
        firstPassSkip.add(GlossaryTerm.QUALIFIED_NAME.atlanFieldName)
        firstPassSkip.addAll(GlossaryTermXformer.TERM_TO_TERM_FIELDS)
        firstPassSkip.add(GlossaryTerm.ASSIGNED_ENTITIES.atlanFieldName)
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
            return ImportResults.combineAll(ctx.client, true, firstPassResults, secondPassResults)
        } else {
            null
        }
    }

    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String {
        val glossaryIdx = deserializer.heading.indexOf(GlossaryCategory.ANCHOR.atlanFieldName)
        val termName = deserializer.getValue(GlossaryTerm.NAME.atlanFieldName)?.let { it as String } ?: ""
        return if (glossaryIdx >= 0) {
            val glossaryName = CSVXformer.trimWhitespace(deserializer.row[glossaryIdx].ifBlank { "" })
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
