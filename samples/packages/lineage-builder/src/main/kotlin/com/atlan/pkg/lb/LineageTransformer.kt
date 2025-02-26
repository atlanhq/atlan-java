/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import LineageBuilderCfg
import com.atlan.model.assets.ICatalog
import com.atlan.model.assets.LineageProcess
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.util.AssetResolver
import com.atlan.util.AssetBatch.AssetIdentity
import mu.KLogger

class LineageTransformer(
    private val ctx: PackageContext<LineageBuilderCfg>,
    private val inputFile: String,
    private val lineageHeaders: List<String>,
    private val qnMap: Map<AssetIdentity, String>,
    private val logger: KLogger,
) : CSVXformer(
        inputFile = inputFile,
        targetHeader = lineageHeaders,
        logger = logger,
        fieldSeparator = ctx.config.fieldSeparator[0],
    ) {
    var anyFailures = false

    companion object {
        const val XFORM_PREFIX = "Transformation"
        const val XFORM_CONNECTOR = "$XFORM_PREFIX ${AssetTransformer.CONNECTOR}"
        const val XFORM_CONNECTION = "$XFORM_PREFIX ${AssetTransformer.CONNECTION}"
        const val XFORM_IDENTITY = "$XFORM_PREFIX ${AssetTransformer.IDENTITY}"
        const val XFORM_NAME = "$XFORM_PREFIX ${AssetTransformer.NAME}"
        val INPUT_HEADERS =
            listOf(
                XFORM_CONNECTOR,
                XFORM_CONNECTION,
                XFORM_IDENTITY,
                XFORM_NAME,
            )
    }

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val name = inputRow[XFORM_NAME] ?: ""
        val sourceQN = AssetTransformer.getAssetQN(ctx, inputRow, AssetTransformer.SOURCE_PREFIX, logger, qnMap)
        val sourceType = inputRow[AssetTransformer.SOURCE_TYPE] ?: ""
        val source =
            if (sourceQN.isNotBlank() && sourceType.isNotBlank()) {
                FieldSerde.getRefByQualifiedName(sourceType, sourceQN)
            } else {
                logger.warn { "Unable to translate source into a valid asset reference: $sourceType::$name" }
                null
            }
        val targetQN = AssetTransformer.getAssetQN(ctx, inputRow, AssetTransformer.TARGET_PREFIX, logger, qnMap)
        val targetType = inputRow[AssetTransformer.TARGET_TYPE] ?: ""
        val target =
            if (targetQN.isNotBlank() && targetType.isNotBlank()) {
                FieldSerde.getRefByQualifiedName(targetType, targetQN)
            } else {
                logger.warn { "Unable to translate target into a valid asset reference: $targetType::$name" }
                null
            }
        if (source != null && target != null) {
            if (source !is ICatalog || target !is ICatalog) {
                logger.warn { "Source and/or target asset are not subtypes of Catalog, and therefore cannot exist in lineage: $inputRow" }
            } else {
                val xformConnector = inputRow[XFORM_CONNECTOR]?.lowercase() ?: ""
                val xformConnection = inputRow[XFORM_CONNECTION] ?: ""
                val connectionId = AssetResolver.ConnectionIdentity(xformConnection, xformConnector)
                val connectionQN = ctx.connectionCache.getIdentityMap().getOrDefault(connectionId, "")
                if (connectionQN.isBlank()) {
                    logger.warn { "Unable to find transformation connection, and therefore cannot create lineage process within it: $xformConnector/$xformConnection" }
                } else {
                    val qualifiedName =
                        LineageProcess.generateQualifiedName(
                            name,
                            connectionQN,
                            inputRow[XFORM_IDENTITY],
                            listOf(source as ICatalog),
                            listOf(target as ICatalog),
                            null,
                        )
                    val row =
                        mutableListOf(
                            LineageProcess.TYPE_NAME,
                            qualifiedName,
                            name,
                            connectionQN,
                            xformConnector,
                            AssetRefXformer.encode(ctx, source),
                            AssetRefXformer.encode(ctx, target),
                        )
                    for (i in row.size until lineageHeaders.size) {
                        // Append other attributes onto the row
                        row.add(inputRow[lineageHeaders[i]] ?: "")
                    }
                    return listOf(row)
                }
            }
        }
        // If we fall through, we were unable to define the lineage, so return an empty row
        anyFailures = true
        return emptyList()
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        // Rows will be limited by the extract, so everything extracted should be imported
        return true
    }
}
