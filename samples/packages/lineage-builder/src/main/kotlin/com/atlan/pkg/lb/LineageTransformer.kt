/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import LineageBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.ICatalog
import com.atlan.model.assets.LineageProcess
import com.atlan.pkg.PackageContext
import com.atlan.pkg.lb.AssetTransformer.Companion.CONNECTION
import com.atlan.pkg.lb.AssetTransformer.Companion.CONNECTOR
import com.atlan.pkg.lb.AssetTransformer.Companion.IDENTITY
import com.atlan.pkg.lb.AssetTransformer.Companion.NAME
import com.atlan.pkg.lb.AssetTransformer.Companion.SOURCE_PREFIX
import com.atlan.pkg.lb.AssetTransformer.Companion.TARGET_PREFIX
import com.atlan.pkg.lb.AssetTransformer.Companion.TYPE
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.pkg.serde.csv.CSVXformer
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
    override fun validateHeader(header: List<String>?): List<String> {
        val missing = mutableListOf<String>()
        if (header.isNullOrEmpty()) {
            missing.add("$SOURCE_PREFIX $TYPE")
            missing.add("$SOURCE_PREFIX $CONNECTOR")
            missing.add("$SOURCE_PREFIX $CONNECTION")
            missing.add("$SOURCE_PREFIX $IDENTITY")
            missing.add("$SOURCE_PREFIX $NAME")
            missing.add("$TARGET_PREFIX $TYPE")
            missing.add("$TARGET_PREFIX $CONNECTOR")
            missing.add("$TARGET_PREFIX $CONNECTION")
            missing.add("$TARGET_PREFIX $IDENTITY")
            missing.add("$TARGET_PREFIX $NAME")
            missing.add("$XFORM_PREFIX $CONNECTOR")
            missing.add("$XFORM_PREFIX $CONNECTION")
            missing.add("$XFORM_PREFIX $IDENTITY")
            missing.add("$XFORM_PREFIX $NAME")
        } else {
            if (!header.contains("$SOURCE_PREFIX $TYPE")) {
                missing.add("$SOURCE_PREFIX $TYPE")
            }
            if (!header.contains("$SOURCE_PREFIX $CONNECTOR")) {
                missing.add("$SOURCE_PREFIX $CONNECTOR")
            }
            if (!header.contains("$SOURCE_PREFIX $CONNECTION")) {
                missing.add("$SOURCE_PREFIX $CONNECTION")
            }
            if (!header.contains("$SOURCE_PREFIX $IDENTITY")) {
                missing.add("$SOURCE_PREFIX $IDENTITY")
            }
            if (!header.contains("$SOURCE_PREFIX $NAME")) {
                missing.add("$SOURCE_PREFIX $NAME")
            }
            if (!header.contains("$TARGET_PREFIX $TYPE")) {
                missing.add("$TARGET_PREFIX $TYPE")
            }
            if (!header.contains("$TARGET_PREFIX $CONNECTOR")) {
                missing.add("$TARGET_PREFIX $CONNECTOR")
            }
            if (!header.contains("$TARGET_PREFIX $CONNECTION")) {
                missing.add("$TARGET_PREFIX $CONNECTION")
            }
            if (!header.contains("$TARGET_PREFIX $IDENTITY")) {
                missing.add("$TARGET_PREFIX $IDENTITY")
            }
            if (!header.contains("$TARGET_PREFIX $NAME")) {
                missing.add("$TARGET_PREFIX $NAME")
            }
            if (!header.contains("$XFORM_PREFIX $CONNECTOR")) {
                missing.add("$XFORM_PREFIX $CONNECTOR")
            }
            if (!header.contains("$XFORM_PREFIX $CONNECTION")) {
                missing.add("$XFORM_PREFIX $CONNECTION")
            }
            if (!header.contains("$XFORM_PREFIX $IDENTITY")) {
                missing.add("$XFORM_PREFIX $IDENTITY")
            }
            if (!header.contains("$XFORM_PREFIX $NAME")) {
                missing.add("$XFORM_PREFIX $NAME")
            }
        }
        return missing
    }

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val name = inputRow[XFORM_NAME] ?: ""
        val sourceType = inputRow[AssetTransformer.SOURCE_TYPE] ?: ""
        val targetType = inputRow[AssetTransformer.TARGET_TYPE] ?: ""
        val sourceQN = AssetTransformer.getAssetQN(ctx, inputRow, AssetTransformer.SOURCE_PREFIX, logger, qnMap)
        val targetQN = AssetTransformer.getAssetQN(ctx, inputRow, AssetTransformer.TARGET_PREFIX, logger, qnMap)
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
                val connectionQN = AssetTransformer.getConnectionQN(ctx, inputRow, XFORM_PREFIX, logger)
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
            RowSerde.getHeaderForField(Asset.CONNECTOR_NAME) to (inputRow[XFORM_CONNECTOR]?.lowercase() ?: ""),
            RowSerde.getHeaderForField(LineageProcess.NAME) to name,
            RowSerde.getHeaderForField(LineageProcess.CONNECTION_QUALIFIED_NAME) to connectionQN,
            RowSerde.getHeaderForField(LineageProcess.INPUTS) to AssetRefXformer.encode(ctx, source),
            RowSerde.getHeaderForField(LineageProcess.OUTPUTS) to AssetRefXformer.encode(ctx, target),
            RowSerde.getHeaderForField(ColumnProcess.PROCESS) to "",
        )
    }
}
