/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Cube
import com.atlan.model.assets.CubeDimension
import com.atlan.model.assets.CubeField
import com.atlan.model.assets.CubeHierarchy
import com.atlan.model.assets.IMultiDimensionalDataset
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.cab.Importer.QN_DELIMITER
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.AssetResolver
import com.atlan.pkg.util.AssetResolver.ConnectionIdentity
import com.atlan.pkg.util.AssetResolver.QualifiedNameDetails
import com.atlan.pkg.util.DeltaProcessor
import mu.KLogger

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param ctx context in which this package is running
 * @param delta the processor containing any details about file deltas
 * @param filename name of the file to import
 * @param typeNameFilter asset types to which to restrict loading
 * @param logger through which to record logging
 * @param creationHandling what to do with assets that do not exist (create full, partial, or ignore)
 * @param batchSize maximum number of records to save per API request
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 */
abstract class AssetImporter(
    ctx: PackageContext<CubeAssetsBuilderCfg>,
    private val delta: DeltaProcessor?,
    filename: String,
    typeNameFilter: String,
    logger: KLogger,
    creationHandling: AssetCreationHandling = Utils.getCreationHandling(ctx.config.assetsUpsertSemantic, AssetCreationHandling.FULL),
    batchSize: Int = ctx.config.assetsBatchSize.toInt(),
    trackBatches: Boolean = ctx.config.trackBatches,
) : CSVImporter(
        ctx = ctx,
        filename = filename,
        logger = logger,
        typeNameFilter = typeNameFilter,
        attrsToOverwrite = attributesToClear(ctx.config.assetsAttrToOverwrite.toMutableList(), "assets", logger),
        creationHandling = creationHandling,
        customMetadataHandling = Utils.getCustomMetadataHandling(ctx.config.assetsCmHandling, CustomMetadataHandling.MERGE),
        atlanTagHandling = Utils.getAtlanTagHandling(ctx.config.assetsTagHandling, AtlanTagHandling.REPLACE),
        batchSize = batchSize,
        trackBatches = trackBatches,
        fieldSeparator = ctx.config.assetsFieldSeparator[0],
    ) {
    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Can skip all of these columns when deserializing a row as they will be set by
        // the creator methods anyway
        return super.import(
            setOf(
                Asset.CONNECTION_NAME.atlanFieldName,
                // ConnectionImporter.CONNECTOR_TYPE, // Let this be loaded, for mis-named connections
                IMultiDimensionalDataset.CUBE_NAME.atlanFieldName,
                IMultiDimensionalDataset.CUBE_DIMENSION_NAME.atlanFieldName,
                IMultiDimensionalDataset.CUBE_HIERARCHY_NAME.atlanFieldName,
            ),
        )
    }

    companion object : AssetResolver {
        /** {@inheritDoc} */
        override fun getQualifiedNameDetails(
            row: List<String>,
            header: List<String>,
            typeName: String,
        ): QualifiedNameDetails {
            val parent: QualifiedNameDetails?
            val current: String
            val unique: String
            val partial: String
            when (typeName) {
                Connection.TYPE_NAME -> {
                    val connection = CSVXformer.trimWhitespace(row[header.indexOf(Asset.CONNECTION_NAME.atlanFieldName)])
                    val connector = CSVXformer.trimWhitespace(row[header.indexOf(ConnectionImporter.CONNECTOR_TYPE)]).lowercase()
                    parent = null
                    unique = ConnectionIdentity(connection, connector).toString()
                    partial = ""
                }
                Cube.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(IMultiDimensionalDataset.CUBE_NAME.atlanFieldName)])
                    parent = getQualifiedNameDetails(row, header, Connection.TYPE_NAME)
                    unique = Cube.generateQualifiedName(current, parent.uniqueQN)
                    partial = current
                }
                CubeDimension.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(IMultiDimensionalDataset.CUBE_DIMENSION_NAME.atlanFieldName)])
                    parent = getQualifiedNameDetails(row, header, Cube.TYPE_NAME)
                    unique = CubeDimension.generateQualifiedName(current, parent.uniqueQN)
                    partial = CubeDimension.generateQualifiedName(current, parent.partialQN)
                }
                CubeHierarchy.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(IMultiDimensionalDataset.CUBE_HIERARCHY_NAME.atlanFieldName)])
                    parent = getQualifiedNameDetails(row, header, CubeDimension.TYPE_NAME)
                    unique = CubeHierarchy.generateQualifiedName(current, parent.uniqueQN)
                    partial = CubeHierarchy.generateQualifiedName(current, parent.partialQN)
                }
                CubeField.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(FieldImporter.FIELD_NAME)])
                    val parentField = if (header.indexOf(FieldImporter.PARENT_FIELD_QN) >= 0) CSVXformer.trimWhitespace(row[header.indexOf(FieldImporter.PARENT_FIELD_QN)]) else ""
                    if (parentField.isBlank()) {
                        parent = getQualifiedNameDetails(row, header, CubeHierarchy.TYPE_NAME)
                        unique = CubeField.generateQualifiedName(current, parent.uniqueQN)
                        partial = CubeField.generateQualifiedName(current, parent.partialQN)
                    } else {
                        val hierarchy = getQualifiedNameDetails(row, header, CubeHierarchy.TYPE_NAME)
                        val grandParent = if (parentField.indexOf(QN_DELIMITER) > 0) parentField.substringBeforeLast(QN_DELIMITER) else ""
                        val parentUnique = calculatePath(parentField, hierarchy.uniqueQN)
                        val parentPartial = calculatePath(parentField, hierarchy.partialQN)
                        val grandParentUnique = if (grandParent.isNotBlank()) calculatePath(grandParent, hierarchy.uniqueQN) else hierarchy.uniqueQN
                        val grandParentPartial = if (grandParent.isNotBlank()) calculatePath(grandParent, hierarchy.partialQN) else hierarchy.partialQN
                        parent =
                            QualifiedNameDetails(
                                parentUnique,
                                parentPartial,
                                grandParentUnique,
                                grandParentPartial,
                            )
                        unique = CubeField.generateQualifiedName(current, parent.uniqueQN)
                        partial = CubeField.generateQualifiedName(current, parent.partialQN)
                    }
                }
                else -> throw IllegalStateException("Unknown multi-dimensional dataset type: $typeName")
            }
            return QualifiedNameDetails(
                unique,
                partial,
                parent?.uniqueQN ?: "",
                parent?.partialQN ?: "",
            )
        }

        private fun calculatePath(
            parentField: String,
            appendToPath: String,
        ): String =
            if (!parentField.contains(QN_DELIMITER)) {
                CubeField.generateQualifiedName(parentField, appendToPath)
            } else {
                val tokens = parentField.split(QN_DELIMITER)
                val parentPath =
                    tokens.subList(0, tokens.size - 1).joinToString("/") {
                        IMultiDimensionalDataset.getSlugForName(it)
                    }
                CubeField.generateQualifiedName(
                    tokens[tokens.size - 1],
                    "$appendToPath/$parentPath",
                )
            }
    }

    /** {@inheritDoc} */
    override fun includeRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): Boolean {
        if (super.includeRow(row, header, typeIdx, qnIdx)) {
            delta?.resolveAsset(row, header)?.let { identity ->
                return delta.reloadAsset(identity)
            } ?: return true
        }
        return false
    }
}
