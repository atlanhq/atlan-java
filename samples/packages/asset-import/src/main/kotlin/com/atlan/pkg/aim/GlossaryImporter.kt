/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.Glossary
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.GlossaryCache
import com.atlan.pkg.serde.RowDeserializer
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
class GlossaryImporter(
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
        cache = GlossaryCache,
        typeNameFilter = Glossary.TYPE_NAME,
        logger = KotlinLogging.logger {},
        failOnErrors = failOnErrors,
        fieldSeparator = fieldSeparator,
    ) {
    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        cache.preload()
        // Also ignore any inbound qualifiedName
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(Glossary.QUALIFIED_NAME.atlanFieldName)
        return super.import(colsToSkip)
    }

    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String {
        return deserializer.getValue(Glossary.NAME.atlanFieldName)?.let { it as String } ?: ""
    }
}
