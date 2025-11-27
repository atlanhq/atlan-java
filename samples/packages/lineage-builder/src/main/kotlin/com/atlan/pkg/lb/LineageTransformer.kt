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
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.serde.Serde
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
        const val XFORM_CONNECTOR = "$XFORM_PREFIX $CONNECTOR"
        const val XFORM_CONNECTION = "$XFORM_PREFIX $CONNECTION"
        const val XFORM_IDENTITY = "$XFORM_PREFIX $IDENTITY"
        const val XFORM_NAME = "$XFORM_PREFIX $NAME"
        val INPUT_HEADERS =
            listOf(
                XFORM_CONNECTOR,
                XFORM_CONNECTION,
                XFORM_IDENTITY,
                XFORM_NAME,
            )
        val REQUIRED_HEADERS =
            mapOf<String, Set<String>>(
                "$SOURCE_PREFIX $TYPE" to emptySet(),
                "$SOURCE_PREFIX $CONNECTOR" to emptySet(),
                "$SOURCE_PREFIX $CONNECTION" to emptySet(),
                "$SOURCE_PREFIX $IDENTITY" to emptySet(),
                "$SOURCE_PREFIX $NAME" to emptySet(),
                "$TARGET_PREFIX $TYPE" to emptySet(),
                "$TARGET_PREFIX $CONNECTOR" to emptySet(),
                "$TARGET_PREFIX $CONNECTION" to emptySet(),
                "$TARGET_PREFIX $IDENTITY" to emptySet(),
                "$TARGET_PREFIX $NAME" to emptySet(),
                XFORM_CONNECTOR to emptySet(),
                XFORM_CONNECTION to emptySet(),
                XFORM_IDENTITY to emptySet(),
                XFORM_NAME to emptySet(),
            )
    }

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val name = inputRow[XFORM_NAME] ?: ""
        val sourceType = inputRow[AssetTransformer.SOURCE_TYPE] ?: ""
        val targetType = inputRow[AssetTransformer.TARGET_TYPE] ?: ""
        val sourceQN = AssetTransformer.getAssetQN(ctx, inputRow, SOURCE_PREFIX, logger, qnMap)
        val targetQN = AssetTransformer.getAssetQN(ctx, inputRow, TARGET_PREFIX, logger, qnMap)
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

    class Preprocessor(
        val ctx: PackageContext<*>,
        originalFile: String,
        fieldSeparator: Char,
        logger: KLogger,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
            requiredHeaders = REQUIRED_HEADERS,
        ) {
        private val typesInFile = mutableSetOf<String>()
        private val invalidTypes = mutableSetOf<String>()

        /** {@inheritDoc} */
        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            // Keep a running collection of the types that are in the file
            checkType(trimWhitespace(row.getOrElse(header.indexOf(AssetTransformer.SOURCE_TYPE)) { "" }))
            checkType(trimWhitespace(row.getOrElse(header.indexOf(AssetTransformer.TARGET_TYPE)) { "" }))
            return row
        }

        private fun checkType(typeName: String) {
            if (typeName.isNotBlank()) {
                if (!typesInFile.contains(typeName)) {
                    try {
                        Serde.getAssetClassForType(typeName)
                    } catch (e: ClassNotFoundException) {
                        invalidTypes.add(typeName)
                    }
                    typesInFile.add(typeName)
                }
            }
        }

        /** {@inheritDoc} */
        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): Results {
            val results = super.finalize(header, outputFile)
            if (invalidTypes.isNotEmpty()) {
                throw IllegalArgumentException("Invalid types were supplied in the input file, which cannot be loaded. Remove these or replace with a valid typeName: $invalidTypes")
            }
            return Results(
                hasLinks = results.hasLinks,
                hasTermAssignments = results.hasTermAssignments,
                outputFile = outputFile ?: filename,
                hasDomainRelationship = results.hasDomainRelationship,
                hasProductRelationship = results.hasProductRelationship,
            )
        }
    }
}
