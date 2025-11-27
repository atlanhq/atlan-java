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
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.AssetResolver
import com.atlan.pkg.util.AssetResolver.ConnectionIdentity
import com.atlan.pkg.util.AssetResolver.QualifiedNameDetails
import com.atlan.pkg.util.DeltaProcessor
import mu.KLogger
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern
import kotlin.system.exitProcess

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
        val results =
            super.import(
                setOf(
                    Asset.CONNECTION_NAME.atlanFieldName,
                    // ConnectionImporter.CONNECTOR_TYPE, // Let this be loaded, for mis-named connections
                    IMultiDimensionalDataset.CUBE_NAME.atlanFieldName,
                    IMultiDimensionalDataset.CUBE_DIMENSION_NAME.atlanFieldName,
                    IMultiDimensionalDataset.CUBE_HIERARCHY_NAME.atlanFieldName,
                ),
            )
        if (results != null) ctx.processedResults.extendWith(results)
        return results
    }

    companion object : AssetResolver {
        val REQUIRED_HEADERS =
            mapOf<String, Set<String>>(
                Asset.TYPE_NAME.atlanFieldName to emptySet(),
                Asset.CONNECTION_NAME.atlanFieldName to emptySet(),
                Asset.CONNECTOR_NAME.atlanFieldName to setOf("connectorType", "connectorName"),
                Cube.CUBE_NAME.atlanFieldName to emptySet(),
            )

        val CUBE_TYPES =
            setOf<String>(
                Connection.TYPE_NAME,
                Cube.TYPE_NAME,
                CubeDimension.TYPE_NAME,
                CubeHierarchy.TYPE_NAME,
                CubeField.TYPE_NAME,
            )

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
                    val connection = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(Asset.CONNECTION_NAME.atlanFieldName)) { "" })
                    val connector = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(ConnectionImporter.CONNECTOR_TYPE)) { "" }).lowercase()
                    parent = null
                    unique = ConnectionIdentity(connection, connector).toString()
                    partial = ""
                }
                Cube.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(IMultiDimensionalDataset.CUBE_NAME.atlanFieldName)) { "" })
                    parent = getQualifiedNameDetails(row, header, Connection.TYPE_NAME)
                    unique = Cube.generateQualifiedName(current, parent.uniqueQN)
                    partial = current
                }
                CubeDimension.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(IMultiDimensionalDataset.CUBE_DIMENSION_NAME.atlanFieldName)) { "" })
                    parent = getQualifiedNameDetails(row, header, Cube.TYPE_NAME)
                    unique = CubeDimension.generateQualifiedName(current, parent.uniqueQN)
                    partial = CubeDimension.generateQualifiedName(current, parent.partialQN)
                }
                CubeHierarchy.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(IMultiDimensionalDataset.CUBE_HIERARCHY_NAME.atlanFieldName)) { "" })
                    parent = getQualifiedNameDetails(row, header, CubeDimension.TYPE_NAME)
                    unique = CubeHierarchy.generateQualifiedName(current, parent.uniqueQN)
                    partial = CubeHierarchy.generateQualifiedName(current, parent.partialQN)
                }
                CubeField.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(FieldImporter.FIELD_NAME)) { "" })
                    val parentField = CSVXformer.trimWhitespace(row.getOrElse(header.indexOf(FieldImporter.PARENT_FIELD_QN)) { "" })
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

    open class Preprocessor(
        originalFile: String,
        fieldSeparator: Char,
        logger: KLogger,
        override val requiredHeaders: Map<String, Set<String>> = REQUIRED_HEADERS,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
            requiredHeaders = requiredHeaders,
        ) {
        val invalidTypes = mutableSetOf<String>()
        val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
        var cubeName: String? = null
        var connectionIdentity: AssetResolver.ConnectionIdentity? = null

        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            val cubeNameOnRow = row.getOrNull(header.indexOf(Cube.CUBE_NAME.atlanFieldName)) ?: ""
            if (cubeName.isNullOrBlank()) {
                cubeName = cubeNameOnRow
            }
            if (cubeName != cubeNameOnRow) {
                logger.error { "Cube name changed mid-file: $cubeName -> $cubeNameOnRow" }
                logger.error { "This package is designed to only process a single cube per input file, exiting." }
                exitProcess(101)
            }
            if (connectionIdentity == null) {
                val name = row.getOrNull(header.indexOf("connectionName"))
                val type = row.getOrNull(header.indexOf("connectorType"))?.lowercase()
                if (name != null && type != null) {
                    connectionIdentity = AssetResolver.ConnectionIdentity(name, type)
                }
            }
            val values = row.toMutableList()
            val typeName = CSVXformer.trimWhitespace(values.getOrElse(typeIdx) { "" })
            if (typeName.isNotBlank() && !CUBE_TYPES.contains(typeName)) {
                invalidTypes.add(typeName)
            }
            if (!invalidTypes.contains(typeName)) {
                val qnDetails = getQualifiedNameDetails(values, header, typeName)
                if (qnDetails.parentUniqueQN.isNotBlank()) {
                    if (!qualifiedNameToChildCount.containsKey(qnDetails.parentUniqueQN)) {
                        qualifiedNameToChildCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                    }
                    qualifiedNameToChildCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                    if (typeName == CubeField.TYPE_NAME) {
                        val hierarchyQN = getHierarchyQualifiedName(qnDetails.parentUniqueQN)
                        if (hierarchyQN != qnDetails.parentUniqueQN) {
                            // Only further increment the field count of the hierarchy for nested
                            // fields (top-level fields are already counted by the logic above)
                            if (!qualifiedNameToChildCount.containsKey(hierarchyQN)) {
                                qualifiedNameToChildCount[hierarchyQN] = AtomicInteger(0)
                            }
                            qualifiedNameToChildCount[hierarchyQN]?.incrementAndGet()
                        }
                    }
                }
            }
            return row
        }

        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): AssetImporter.Results {
            val results = super.finalize(header, outputFile)
            if (invalidTypes.isNotEmpty()) {
                throw IllegalArgumentException("Found non-dimensional assets supplied in the input file, which cannot be loaded through this utility: $invalidTypes")
            }
            return Results(
                cubeName!!,
                results.hasLinks,
                results.hasTermAssignments,
                results.hasDomainRelationship,
                results.hasProductRelationship,
                filename,
                connectionIdentity,
                qualifiedNameToChildCount,
            )
        }

        private val hierarchyQNPrefix: Pattern = Pattern.compile("([^/]*/[a-z0-9-]+/[^/]*(/[^/]*){2}).*")

        /**
         * Extracts the unique name of the hierarchy from the qualified name of the CubeField's parent.
         *
         * @param parentQualifiedName unique name of the hierarchy or parent field in which this CubeField exists
         * @return the unique name of the CubeHierarchy in which the field exists
         */
        private fun getHierarchyQualifiedName(parentQualifiedName: String): String {
            val m = hierarchyQNPrefix.matcher(parentQualifiedName)
            return if (m.find() && m.groupCount() > 0) m.group(1) else ""
        }
    }

    open class Results(
        assetRootName: String,
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        hasDomainRelationship: Boolean,
        hasProductRelationship: Boolean,
        preprocessedFile: String,
        val connectionIdentity: ConnectionIdentity?,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
    ) : DeltaProcessor.Results(
            assetRootName = assetRootName,
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            hasDomainRelationship = hasDomainRelationship,
            hasProductRelationship = hasProductRelationship,
            preprocessedFile = preprocessedFile,
        )
}
