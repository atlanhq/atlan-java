/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Cube
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowDeserializer
import com.atlan.pkg.util.DeltaProcessor
import mu.KLogger

/**
 * Import cubes into Atlan from a provided CSV file.
 *
 * Only the cubes and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which this package is running
 * @param delta the processor containing any details about file deltas
 * @param preprocessed details of the preprocessed CSV file
 * @param connectionImporter that was used to import connections
 * @param logger through which to record any logging
 */
class CubeImporter(
    ctx: PackageContext<CubeAssetsBuilderCfg>,
    private val delta: DeltaProcessor,
    private val preprocessed: Results,
    private val connectionImporter: ConnectionImporter,
    logger: KLogger,
) : AssetImporter(
        ctx = ctx,
        delta = delta,
        filename = preprocessed.preprocessedFile,
        typeNameFilter = Cube.TYPE_NAME,
        logger = logger,
    ) {
    /** {@inheritDoc} */
    override fun getBuilder(deserializer: RowDeserializer): Asset.AssetBuilder<*, *> {
        val name = deserializer.getValue(Cube.CUBE_NAME.atlanFieldName)?.let { it as String } ?: ""
        val connectionQN = connectionImporter.getBuilder(deserializer).build().qualifiedName
        val qnDetails = getQualifiedNameDetails(deserializer.row, deserializer.heading, typeNameFilter)
        return Cube
            .creator(name, connectionQN)
            .cubeDimensionCount(preprocessed.qualifiedNameToChildCount[qnDetails.uniqueQN]?.toLong())
    }
}
