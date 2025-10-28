/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import LineageBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.util.AssetBatch.AssetIdentity
import mu.KLogger

class AssetXformer(
    private val ctx: PackageContext<LineageBuilderCfg>,
    private val inputFile: String,
    private val completeHeaders: List<String>,
    private val logger: KLogger,
) : CSVXformer(
        inputFile = inputFile,
        targetHeader = completeHeaders,
        logger = logger,
        fieldSeparator = ctx.config.fieldSeparator[0],
    ) {
    var anyFailures = false

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val sourceValues = mutableListOf<String>()
        val targetValues = mutableListOf<String>()
        val sourceMap = mapAsset(inputRow, SOURCE_PREFIX)
        val targetMap = mapAsset(inputRow, TARGET_PREFIX)
        targetHeader!!.forEach { header ->
            // Note: we do NOT want to pass through any other attributes on the row here (those should be for processes only)
            if (header != null) {
                val transformedSource = sourceMap?.getOrElse(header) { "" } ?: ""
                sourceValues.add(transformedSource)
                val transformedTarget = targetMap?.getOrElse(header) { "" } ?: ""
                targetValues.add(transformedTarget)
            }
        }
        return if (sourceValues.isNotEmpty() && targetValues.isNotEmpty()) {
            listOf(sourceValues, targetValues)
        } else if (sourceValues.isNotEmpty()) {
            anyFailures = true
            logger.info { "Unable to map and transform the target asset -- skipped." }
            listOf(sourceValues)
        } else if (targetValues.isNotEmpty()) {
            anyFailures = true
            logger.info { "Unable to map and transform the source asset -- skipped." }
            listOf(targetValues)
        } else {
            anyFailures = true
            logger.info { "Unable to map and transform either the source and target asset -- skipped both." }
            emptyList()
        }
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        // Rows will be limited by the extract, so everything (non-empty) extracted should be imported
        return inputRow.values.any { it.trim().isNotBlank() }
    }

    private fun mapAsset(
        inputRow: Map<String, String>,
        prefix: String,
    ): Map<String, String>? {
        val connectionQN = getConnectionQN(inputRow, prefix, logger)
        val assetQN =
            if (connectionQN.isNotBlank()) {
                getAssetQN(inputRow, prefix, logger)
            } else {
                ""
            }
        return if (assetQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to assetQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to (inputRow["$prefix $TYPE"] ?: ""),
                RowSerde.getHeaderForField(Asset.NAME) to (inputRow["$prefix $NAME"] ?: ""),
                RowSerde.getHeaderForField(Asset.CONNECTOR_NAME) to (inputRow["$prefix $CONNECTOR"]?.lowercase() ?: ""),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME) to connectionQN,
            )
        } else {
            null
        }
    }

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
        val BASE_OUTPUT_HEADERS =
            listOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME),
                RowSerde.getHeaderForField(Asset.TYPE_NAME),
                RowSerde.getHeaderForField(Asset.NAME),
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME),
            )

        fun getConnectionQN(
            inputRow: Map<String, String>,
            prefix: String,
            logger: KLogger,
        ): String {
            val connectorType = inputRow["$prefix $CONNECTOR"]?.lowercase() ?: ""
            val connectionName = inputRow["$prefix $CONNECTION"] ?: ""
            return if (connectorType.isNotBlank() && connectionName.isNotBlank()) {
                "{{$connectorType/$connectionName}}"
            } else {
                logger.warn { "Unable to find $prefix connection, and therefore cannot create lineage process within it: $connectorType/$connectionName" }
                ""
            }
        }

        fun getAssetQN(
            inputRow: Map<String, String>,
            prefix: String,
            logger: KLogger,
            qnMap: Map<AssetIdentity, String> = mapOf(),
        ): String {
            val assetType = inputRow["$prefix $TYPE"] ?: ""
            val partialQN = inputRow["$prefix $IDENTITY"] ?: ""
            val connectionQN = getConnectionQN(inputRow, prefix, logger)
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
}
