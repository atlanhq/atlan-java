/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import LineageBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.util.AssetBatch.AssetIdentity
import mu.KLogger

class AssetTransformer(
    private val ctx: PackageContext<LineageBuilderCfg>,
    private val inputFile: String,
    private val logger: KLogger,
) : CSVXformer(
        inputFile = inputFile,
        targetHeader =
            listOf(
                Asset.QUALIFIED_NAME.atlanFieldName,
                Asset.TYPE_NAME.atlanFieldName,
                Asset.NAME.atlanFieldName,
                Asset.CONNECTOR_NAME.atlanFieldName,
                Asset.CONNECTION_QUALIFIED_NAME.atlanFieldName,
            ),
        logger = logger,
        fieldSeparator = ctx.config.fieldSeparator[0],
    ) {
    var anyFailures = false

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
        val INPUT_HEADERS =
            listOf(
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

        fun getConnectionQN(
            ctx: PackageContext<LineageBuilderCfg>,
            inputRow: Map<String, String>,
            prefix: String,
            logger: KLogger,
        ): String {
            val connectorType = inputRow["$prefix $CONNECTOR"]?.lowercase() ?: ""
            val connectionName = inputRow["$prefix $CONNECTION"] ?: ""
            val connectionId = ctx.connectionCache.getIdentityForAsset(connectionName, connectorType)
            val connectionQN = ctx.connectionCache.getByIdentity(connectionId)?.qualifiedName ?: ""
            if (connectionQN.isBlank()) {
                logger.warn { "Unable to find connection for the provided details: $connectorType::$connectionName" }
            }
            return connectionQN
        }

        fun getAssetQN(
            ctx: PackageContext<LineageBuilderCfg>,
            inputRow: Map<String, String>,
            prefix: String,
            logger: KLogger,
            qnMap: Map<AssetIdentity, String> = mapOf(),
        ): String {
            val assetType = inputRow["$prefix $TYPE"] ?: ""
            val partialQN = inputRow["$prefix $IDENTITY"] ?: ""
            val connectionQN = getConnectionQN(ctx, inputRow, prefix, logger)
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
                logger.warn { "Unable to determine full qualifiedName from provided details: $connectionQN/$partialQN" }
                ""
            }
        }
    }

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val source = mapAsset(inputRow, SOURCE_PREFIX)
        val target = mapAsset(inputRow, TARGET_PREFIX)
        return if (source != null && target != null) {
            listOf(source, target)
        } else if (source != null) {
            anyFailures = true
            logger.info { "Unable to map and transform the source asset -- skipped." }
            listOf(source)
        } else if (target != null) {
            anyFailures = true
            logger.info { "Unable to map and transform the target asset -- skipped." }
            listOf(target)
        } else {
            anyFailures = true
            logger.info { "Unable to map and transform both the source and target asset -- skipped both." }
            emptyList()
        }
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        // Rows will be limited by the extract, so everything extracted should be imported
        return true
    }

    private fun mapAsset(
        inputRow: Map<String, String>,
        prefix: String,
    ): List<String>? {
        val connectionQN = getConnectionQN(ctx, inputRow, prefix, logger)
        val assetQN =
            if (connectionQN.isNotBlank()) {
                getAssetQN(ctx, inputRow, prefix, logger)
            } else {
                ""
            }
        return if (assetQN.isNotBlank()) {
            return listOf(
                assetQN,
                inputRow["$prefix $TYPE"] ?: "",
                inputRow["$prefix $NAME"] ?: "",
                inputRow["$prefix $CONNECTOR"]?.lowercase() ?: "",
                connectionQN,
            )
        } else {
            null
        }
    }
}
