/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import com.atlan.model.assets.Asset
import com.atlan.model.assets.ICatalog
import com.atlan.model.assets.LineageProcess
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.util.AssetBatch.AssetIdentity
import mu.KLogger

class LineageTransformer(
    private val ctx: Loader.Context,
    private val inputFile: String,
    private val qnMap: Map<AssetIdentity, String>,
    private val logger: KLogger,
) : CSVXformer(
    inputFile,
    listOf(
        Asset.TYPE_NAME.atlanFieldName,
        Asset.QUALIFIED_NAME.atlanFieldName,
        Asset.NAME.atlanFieldName,
        Asset.CONNECTION_QUALIFIED_NAME.atlanFieldName,
        "connectorType",
        LineageProcess.INPUTS.atlanFieldName,
        LineageProcess.OUTPUTS.atlanFieldName,
    ),
    logger,
) {
    companion object {
        const val XFORM_PREFIX = "Transformation"
        const val XFORM_CONNECTOR = "$XFORM_PREFIX ${AssetTransformer.CONNECTOR}"
        const val XFORM_CONNECTION = "$XFORM_PREFIX ${AssetTransformer.CONNECTION}"
        const val XFORM_IDENTITY = "$XFORM_PREFIX ${AssetTransformer.IDENTITY}"
        const val XFORM_NAME = "$XFORM_PREFIX ${AssetTransformer.NAME}"
    }

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val name = inputRow[XFORM_NAME] ?: ""
        val sourceQN = AssetTransformer.getAssetQN(ctx, inputRow, AssetTransformer.SOURCE_PREFIX, qnMap)
        val sourceType = inputRow[AssetTransformer.SOURCE_TYPE] ?: ""
        val source = FieldSerde.getRefByQualifiedName(sourceType, sourceQN)
        val targetQN = AssetTransformer.getAssetQN(ctx, inputRow, AssetTransformer.TARGET_PREFIX, qnMap)
        val targetType = inputRow[AssetTransformer.TARGET_TYPE] ?: ""
        val target = FieldSerde.getRefByQualifiedName(targetType, targetQN)
        if (source !is ICatalog || target !is ICatalog) {
            logger.warn { "Source and/or target asset are not subtypes of Catalog, and therefore cannot exist in lineage: $inputRow" }
            return listOf(listOf("", "", "", "", "", ""))
        } else {
            val xformConnector = inputRow[XFORM_CONNECTOR] ?: ""
            val xformConnection = inputRow[XFORM_CONNECTION] ?: ""
            val connectionId = Loader.ConnectionId(xformConnector, xformConnection)
            val connectionQN = ctx.connectionMap.getOrDefault(connectionId, "")
            val qualifiedName = LineageProcess.generateQualifiedName(
                name,
                connectionQN,
                inputRow[XFORM_IDENTITY],
                listOf(source as ICatalog),
                listOf(target as ICatalog),
                null,
            )
            return listOf(
                listOf(
                    LineageProcess.TYPE_NAME,
                    qualifiedName,
                    name,
                    connectionQN,
                    xformConnector,
                    AssetRefXformer.encode(source),
                    AssetRefXformer.encode(target),
                ),
            )
        }
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        // Rows will be limited by the extract, so everything extracted should be imported
        return true
    }
}
