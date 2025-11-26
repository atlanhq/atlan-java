/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.GlossaryTerm
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
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
    private val secondPassRemain =
        setOf(
            GlossaryTerm.NAME.atlanFieldName,
            GlossaryTerm.ANCHOR.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun validateHeader(header: List<String>?): List<String> {
        val missing = super.validateHeader(header).toMutableList()
        if (header.isNullOrEmpty()) {
            missing.add(GlossaryTerm.ANCHOR.atlanFieldName)
        } else {
            if (!header.contains(GlossaryTerm.ANCHOR.atlanFieldName)) {
                missing.add(GlossaryTerm.ANCHOR.atlanFieldName)
            }
        }
        return missing
    }

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        cache.preload()
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(GlossaryTerm.QUALIFIED_NAME.atlanFieldName)
        colsToSkip.add(GlossaryTerm.ASSIGNED_ENTITIES.atlanFieldName)
        return super.import(typeNameFilter, colsToSkip, secondPassRemain)
    }

    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String {
        val glossaryIdx = deserializer.heading.indexOf(GlossaryTerm.ANCHOR.atlanFieldName)
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
