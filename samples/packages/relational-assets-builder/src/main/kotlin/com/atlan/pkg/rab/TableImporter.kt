/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Table
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.util.DeltaProcessor
import mu.KLogger

/**
 * Import tables into Atlan from a provided CSV file.
 *
 * Only the tables and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context through which this package is running
 * @param delta the processor containing any details about file deltas
 * @param preprocessed details of the preprocessed CSV file
 * @param connectionImporter that was used to import connections
 * @param logger through which to record logging
 */
class TableImporter(
    ctx: PackageContext<RelationalAssetsBuilderCfg>,
    private val delta: DeltaProcessor,
    private val preprocessed: Importer.Results,
    private val connectionImporter: ConnectionImporter,
    logger: KLogger,
) : AssetImporter(
        ctx = ctx,
        delta = delta,
        filename = preprocessed.preprocessedFile,
        typeNameFilter = Table.TYPE_NAME,
        logger = logger,
    ) {
    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(ENTITY_NAME)?.let { it as String } ?: ""
        val connectionQN = connectionImporter.getBuilder(deserializer).build().qualifiedName
        val qnDetails = getQualifiedNameDetails(deserializer.row, deserializer.heading, typeNameFilter)
        val schemaQN = "$connectionQN/${qnDetails.parentPartialQN}"
        return Table.creator(name, schemaQN)
            .columnCount(preprocessed.qualifiedNameToChildCount[qnDetails.uniqueQN]?.toLong())
    }
}
