/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Glossary
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
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
class GlossaryImporter(
    ctx: PackageContext<AssetImportCfg>,
    filename: String,
    logger: KLogger,
) : GTCImporter(
        ctx = ctx,
        filename = filename,
        cache = ctx.glossaryCache,
        typeNameFilter = Glossary.TYPE_NAME,
        logger = logger,
    ) {
    private val secondPassRemain =
        setOf(
            Asset.NAME.atlanFieldName,
        )

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Also ignore any inbound qualifiedName
        val colsToSkip = columnsToSkip.toMutableSet()
        colsToSkip.add(Glossary.QUALIFIED_NAME.atlanFieldName)
        return super.import(typeNameFilter, colsToSkip, secondPassRemain, cache)
    }

    /** {@inheritDoc} */
    override fun getCacheId(deserializer: RowDeserializer): String = deserializer.getValue(Glossary.NAME.atlanFieldName)?.let { it as String } ?: ""
}
