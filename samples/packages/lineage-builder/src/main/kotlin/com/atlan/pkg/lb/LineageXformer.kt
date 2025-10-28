/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import LineageBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.ICatalog
import com.atlan.model.assets.LineageProcess
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.pkg.serde.csv.CSVXformer
import mu.KLogger

class LineageXformer(
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
        val name = inputRow[XFORM_NAME] ?: ""
        val sourceType = inputRow[AssetXformer.SOURCE_TYPE] ?: ""
        val targetType = inputRow[AssetXformer.TARGET_TYPE] ?: ""
        val sourceQN = AssetXformer.getAssetQN(inputRow, AssetXformer.SOURCE_PREFIX, logger)
        val targetQN = AssetXformer.getAssetQN(inputRow, AssetXformer.TARGET_PREFIX, logger)
        val source =
            if (sourceQN.isNotBlank() && sourceType.isNotBlank()) {
                FieldSerde.getRefByQualifiedName(sourceType, sourceQN)
            } else {
                logger.warn { "Unable to translate source into a valid asset reference: $sourceType::$name" }
                null
            }
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
                val connectionQN = AssetXformer.getConnectionQN(inputRow, XFORM_PREFIX, logger)
                if (connectionQN.isNotBlank()) {
                    val processMap = mapProcess(inputRow, name, connectionQN, source, target)
                    val valueList = mutableListOf<String>()
                    targetHeader!!.forEach { header ->
                        if (header != null) {
                            // Look for the transformed value first, then fallback to passing through what came in the input
                            val transformed = processMap.getOrElse(header) { inputRow.getOrElse(header) { "" } }
                            valueList.add(transformed)
                        }
                    }
                    return listOf(valueList)
                }
            }
        }
        // If we fall through, we were unable to define the lineage, so return an empty row
        anyFailures = true
        return emptyList()
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        // Rows will be limited by the extract, so everything (non-empty) extracted should be imported
        return inputRow.values.any { it.trim().isNotBlank() }
    }

    private fun mapProcess(
        inputRow: Map<String, String>,
        name: String,
        connectionQN: String,
        source: Asset,
        target: Asset,
    ): Map<String, String> {
        val qualifiedName =
            LineageProcess.generateQualifiedName(
                name,
                connectionQN,
                inputRow[XFORM_IDENTITY],
                listOf(source as ICatalog),
                listOf(target as ICatalog),
                null,
            )
        return mapOf(
            RowSerde.getHeaderForField(LineageProcess.QUALIFIED_NAME) to qualifiedName,
            RowSerde.getHeaderForField(Asset.TYPE_NAME) to LineageProcess.TYPE_NAME,
            RowSerde.getHeaderForField(LineageProcess.NAME) to name,
            RowSerde.getHeaderForField(LineageProcess.CONNECTION_QUALIFIED_NAME) to connectionQN,
            RowSerde.getHeaderForField(LineageProcess.INPUTS) to AssetRefXformer.encode(ctx, source),
            RowSerde.getHeaderForField(LineageProcess.OUTPUTS) to AssetRefXformer.encode(ctx, target),
            RowSerde.getHeaderForField(ColumnProcess.PROCESS) to "",
        )
    }

    companion object {
        const val XFORM_PREFIX = "Transformation"
        const val XFORM_CONNECTOR = "$XFORM_PREFIX ${AssetXformer.CONNECTOR}"
        const val XFORM_CONNECTION = "$XFORM_PREFIX ${AssetXformer.CONNECTION}"
        const val XFORM_IDENTITY = "$XFORM_PREFIX ${AssetXformer.IDENTITY}"
        const val XFORM_NAME = "$XFORM_PREFIX ${AssetXformer.NAME}"
        val INPUT_HEADERS =
            listOf(
                XFORM_CONNECTOR,
                XFORM_CONNECTION,
                XFORM_IDENTITY,
                XFORM_NAME,
            )
        val BASE_OUTPUT_HEADERS =
            listOf(
                RowSerde.getHeaderForField(LineageProcess.QUALIFIED_NAME),
                RowSerde.getHeaderForField(Asset.TYPE_NAME),
                RowSerde.getHeaderForField(LineageProcess.NAME),
                RowSerde.getHeaderForField(LineageProcess.CONNECTION_QUALIFIED_NAME),
                RowSerde.getHeaderForField(LineageProcess.INPUTS),
                RowSerde.getHeaderForField(LineageProcess.OUTPUTS),
                RowSerde.getHeaderForField(ColumnProcess.PROCESS),
            )
    }
}
