/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import com.atlan.model.assets.Glossary
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.cache.GlossaryCache
import com.atlan.pkg.serde.RowDeserializer
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
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 */
class GlossaryImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    private val trackBatches: Boolean = true,
) : GTCImporter(
    filename = filename,
    attrsToOverwrite = attrsToOverwrite,
    updateOnly = updateOnly,
    batchSize = batchSize,
    cache = GlossaryCache,
    typeNameFilter = Glossary.TYPE_NAME,
    logger = KotlinLogging.logger {},
    trackBatches = trackBatches,
) {
    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String {
        return deserializer.getValue(Glossary.NAME.atlanFieldName)?.let { it as String } ?: ""
    }
}
