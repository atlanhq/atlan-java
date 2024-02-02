/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import com.atlan.model.assets.Asset
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.util.AssetBatch.AssetIdentity
import mu.KLogger

class AssetTransformer(
    private val ctx: Loader.Context,
    private val inputFile: String,
    private val logger: KLogger,
) : CSVXformer(
    inputFile,
    listOf(
        Asset.QUALIFIED_NAME.atlanFieldName,
        Asset.TYPE_NAME.atlanFieldName,
        Asset.NAME.atlanFieldName,
        "connectorType",
        Asset.CONNECTION_QUALIFIED_NAME.atlanFieldName,
    ),
    logger,
) {
    companion object {
        const val TYPE = "Type"
        const val NAME = "Name"
        const val CONNECTOR = "Connector"
        const val CONNECTION = "Connection"
        const val IDENTITY = "Identity"
        const val SOURCE_PREFIX = "Source"
        const val SOURCE_TYPE = "$SOURCE_PREFIX $TYPE"
        const val TARGET_PREFIX = "Target"
        const val TARGET_TYPE = "$TARGET_PREFIX $TYPE"
        val INPUT_HEADERS = listOf(
            "$SOURCE_PREFIX $TYPE",
            "$SOURCE_PREFIX $NAME",
            "$SOURCE_PREFIX $CONNECTOR",
            "$SOURCE_PREFIX $CONNECTION",
            "$SOURCE_PREFIX $IDENTITY",
            "$TARGET_PREFIX $TYPE",
            "$TARGET_PREFIX $NAME",
            "$TARGET_PREFIX $CONNECTOR",
            "$TARGET_PREFIX $CONNECTION",
            "$TARGET_PREFIX $IDENTITY",
        )

        fun getConnectionQN(ctx: Loader.Context, inputRow: Map<String, String>, prefix: String): String {
            val connectorType = inputRow["$prefix $CONNECTOR"] ?: ""
            val connectionName = inputRow["$prefix $CONNECTION"] ?: ""
            val connectionId = Loader.ConnectionId(connectorType, connectionName)
            return ctx.connectionMap.getOrDefault(connectionId, "")
        }

        fun getAssetQN(ctx: Loader.Context, inputRow: Map<String, String>, prefix: String, qnMap: Map<AssetIdentity, String> = mapOf()): String {
            val assetType = inputRow["$prefix $TYPE"] ?: ""
            val partialQN = inputRow["$prefix $IDENTITY"] ?: ""
            val connectionQN = getConnectionQN(ctx, inputRow, prefix)
            if (connectionQN.isBlank()) {
                // Short-circuit if there is no connection qualifiedName
                return ""
            }
            val candidateQN = "$connectionQN/$partialQN"
            return if (qnMap.isNotEmpty()) {
                // If there is data in the qnMap, translate case-insensitive qualifiedName to
                // actual qualifiedName
                val assetId = AssetIdentity(assetType, candidateQN, true)
                qnMap.getOrDefault(assetId, "")
            } else if (connectionQN.isNotBlank() && partialQN.isNotBlank()) {
                // Otherwise, fallback to the qualifiedName as we calculated it (as long as it
                // has all components it should)
                candidateQN
            } else {
                // Or if none of that is true, short-circuit to a blank qualifiedName as we do
                // not have a valid qualifiedName
                ""
            }
        }
    }

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val source = mapAsset(inputRow, SOURCE_PREFIX)
        val target = mapAsset(inputRow, TARGET_PREFIX)
        return listOf(source, target)
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        // Rows will be limited by the extract, so everything extracted should be imported
        return true
    }

    private fun mapAsset(inputRow: Map<String, String>, prefix: String): List<String> {
        val connectionQN = getConnectionQN(ctx, inputRow, prefix)
        val assetQN = getAssetQN(ctx, inputRow, prefix)
        return if (assetQN.isNotBlank()) {
            return listOf(
                assetQN,
                inputRow["$prefix $TYPE"] ?: "",
                inputRow["$prefix $NAME"] ?: "",
                inputRow["$prefix $CONNECTOR"] ?: "",
                connectionQN,
            )
        } else {
            listOf("", "", "", "", "")
        }
    }
}
