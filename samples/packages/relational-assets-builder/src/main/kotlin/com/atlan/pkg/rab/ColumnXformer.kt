/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.cell.DataTypeXformer
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.util.DeltaProcessor
import mu.KLogger
import java.util.concurrent.atomic.AtomicInteger
import kotlin.String
import kotlin.collections.Set

class ColumnXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    completeHeaders: List<String>,
    preprocessedDetails: Results,
    private val logger: KLogger,
) : AssetXformer(
        ctx = ctx,
        completeHeaders = completeHeaders,
        typeNameFilter = Column.TYPE_NAME,
        preprocessedDetails = preprocessedDetails,
        logger = logger,
    ) {
    companion object {
        const val COLUMN_PARENT_QN = "columnParentQualifiedName"
        const val COLUMN_NAME = "columnName"
        val PARENT_COLUMN_QN_HEADER = RowSerde.getHeaderForField(Column.PARENT_COLUMN_QUALIFIED_NAME, Column::class.java)
        val PARENT_COLUMN_HEADER = RowSerde.getHeaderForField(Column.PARENT_COLUMN, Column::class.java)
        val PARENT_COLUMN_NAME_HEADER = RowSerde.getHeaderForField(Column.PARENT_COLUMN_NAME, Column::class.java)
        val NESTED_COLUMN_ORDER_HEADER = RowSerde.getHeaderForField(Column.NESTED_COLUMN_ORDER, Column::class.java)
        val COLUMN_DEPTH_LEVEL_HEADER = RowSerde.getHeaderForField(Column.COLUMN_DEPTH_LEVEL, Column::class.java)
        val COLUMN_HIERARCHY_HEADER = RowSerde.getHeaderForField(Column.COLUMN_HIERARCHY, Column::class.java)
        val REQUIRED_HEADERS =
            mapOf<String, Set<String>>(
                Asset.TYPE_NAME.atlanFieldName to setOf(),
                Asset.CONNECTION_NAME.atlanFieldName to setOf(),
                Asset.CONNECTOR_NAME.atlanFieldName to setOf("connectorType", "connectorName"),
                Database.DATABASE_NAME.atlanFieldName to setOf(),
                Database.SCHEMA_NAME.atlanFieldName to setOf(),
                "entityName" to setOf(),
                "columnName" to setOf(),
                Column.DATA_TYPE.atlanFieldName to setOf(),
            )
        val RDBMS_TYPES =
            setOf<String>(
                Connection.TYPE_NAME,
                Database.TYPE_NAME,
                Schema.TYPE_NAME,
                Table.TYPE_NAME,
                View.TYPE_NAME,
                MaterializedView.TYPE_NAME,
                Column.TYPE_NAME,
            )
    }

    /** {@inheritDoc} */
    override fun mapAsset(inputRow: Map<String, String>): Map<String, String> {
        val connectionQN = getConnectionQN(inputRow)
        val details = getSQLHierarchyDetails(inputRow, typeNameFilter, preprocessedDetails.entityQualifiedNameToType)
        val assetQN = "$connectionQN/${details.partialQN}"
        val parentQN = "$connectionQN/${details.parentPartialQN}"
        val rawDataType = trimWhitespace(inputRow.getOrElse(Column.DATA_TYPE.atlanFieldName) { "" })
        val displayDataType = baseTypeName(rawDataType)
        var precision: Int? = null
        var scale: Double? = null
        var maxLength: Long? = null
        if (rawDataType.isNotBlank()) {
            if (!rawDataType.contains("<") && !rawDataType.contains(">")) {
                // Only attempt to parse things like precision, scale and max-length if this is not a complex type
                precision = DataTypeXformer.getPrecision(rawDataType)
                scale = DataTypeXformer.getScale(rawDataType)
                maxLength = DataTypeXformer.getMaxLength(rawDataType)
            }
        }
        return if (assetQN.isNotBlank()) {
            return mapOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME) to assetQN,
                RowSerde.getHeaderForField(Asset.TYPE_NAME) to typeNameFilter,
                RowSerde.getHeaderForField(Asset.NAME) to details.name,
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE) to getConnectorType(inputRow),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME) to connectionQN,
                RowSerde.getHeaderForField(Column.DATABASE_NAME, Column::class.java) to details.databaseName,
                RowSerde.getHeaderForField(Column.DATABASE_QUALIFIED_NAME, Column::class.java) to "$connectionQN/${details.databasePQN}",
                RowSerde.getHeaderForField(Column.SCHEMA_NAME, Column::class.java) to details.schemaName,
                RowSerde.getHeaderForField(Column.SCHEMA_QUALIFIED_NAME, Column::class.java) to "$connectionQN/${details.schemaPQN}",
                RowSerde.getHeaderForField(Column.TABLE_NAME, Column::class.java) to details.tableName,
                RowSerde.getHeaderForField(Column.TABLE_QUALIFIED_NAME, Column::class.java) to if (details.tablePQN.isNotBlank()) "$connectionQN/${details.tablePQN}" else "",
                RowSerde.getHeaderForField(Column.TABLE, Column::class.java) to if (details.parentTypeName == Table.TYPE_NAME) "${details.parentTypeName}@$parentQN" else "",
                RowSerde.getHeaderForField(Column.VIEW_NAME, Column::class.java) to details.viewName,
                RowSerde.getHeaderForField(Column.VIEW_QUALIFIED_NAME, Column::class.java) to if (details.viewPQN.isNotBlank()) "$connectionQN/${details.viewPQN}" else "",
                RowSerde.getHeaderForField(Column.VIEW, Column::class.java) to if (details.parentTypeName == View.TYPE_NAME) "${details.parentTypeName}@$parentQN" else "",
                RowSerde.getHeaderForField(Column.MATERIALIZED_VIEW, Column::class.java) to if (details.parentTypeName == MaterializedView.TYPE_NAME) "${details.parentTypeName}@$parentQN" else "",
                RowSerde.getHeaderForField(Column.DATA_TYPE, Column::class.java) to displayDataType,
                RowSerde.getHeaderForField(Column.ORDER, Column::class.java) to inputRow.getOrElse(Column.ORDER.atlanFieldName) { "" },
                RowSerde.getHeaderForField(Column.RAW_DATA_TYPE_DEFINITION, Column::class.java) to rawDataType,
                RowSerde.getHeaderForField(Column.PRECISION, Column::class.java) to (precision?.toString() ?: ""),
                RowSerde.getHeaderForField(Column.NUMERIC_SCALE, Column::class.java) to (scale?.toString() ?: ""),
                RowSerde.getHeaderForField(Column.MAX_LENGTH, Column::class.java) to (maxLength?.toString() ?: ""),
            )
        } else {
            mapOf()
        }
    }

    /** Returns the base type name, stripping any angle-bracket type parameters.
     *  E.g. "STRUCT<a:INT,b:DOUBLE>" → "STRUCT", "INT" → "INT". */
    private fun baseTypeName(rawType: String): String = if (rawType.contains("<")) rawType.substringBefore("<").trim().uppercase() else rawType

    /** {@inheritDoc}
     *
     * Overridden to emit additional child column rows when the column's data type is a complex type
     * (STRUCT, ARRAY<STRUCT>, or MAP<K, STRUCT>). Child columns are generated recursively for
     * deeply nested types.
     */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val rows = super.mapRow(inputRow).toMutableList()
        val rawType = trimWhitespace(inputRow.getOrElse(Column.DATA_TYPE.atlanFieldName) { "" })
        val parseResult = ComplexTypeParser.extractStructFields(rawType)
        if (parseResult != null) {
            val connectionQN = getConnectionQN(inputRow)
            val details = getSQLHierarchyDetails(inputRow, typeNameFilter, preprocessedDetails.entityQualifiedNameToType)
            val parentColumnQN = "$connectionQN/${details.partialQN}"
            val parentAssetMap = mapAsset(inputRow)
            rows.addAll(buildSubColumnRows(parentAssetMap, parentColumnQN, parseResult))
        }
        return rows
    }

    /**
     * Recursively build child column rows for all fields in the given [parseResult].
     *
     * @param baseAssetMap field map of the immediate parent column (used to inherit context fields)
     * @param parentColumnQN qualified name of the parent column asset (used for [PARENT_COLUMN_QN_HEADER])
     * @param parseResult parsed complex type fields and optional synthetic QN node (e.g. "items" for ARRAY)
     * @param depth nesting depth of the child columns (1 for direct children of a top-level column, 2 for grandchildren, etc.)
     */
    private fun buildSubColumnRows(
        baseAssetMap: Map<String, String>,
        parentColumnQN: String,
        parseResult: ComplexTypeParser.ParseResult,
        depth: Int = 1,
    ): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        // For ARRAY / MAP, insert the synthetic node into the QN path but NOT into parentColumnQN
        val qnBase = if (parseResult.syntheticNode != null) "$parentColumnQN/${parseResult.syntheticNode}" else parentColumnQN
        parseResult.fields.forEachIndexed { idx, field ->
            val childQN = "$qnBase/${field.name}"
            val childAssetMap = buildChildAssetMap(baseAssetMap, parentColumnQN, childQN, field, idx + 1, depth)
            rows.add(assetMapToValueList(childAssetMap))
            // Recurse for nested complex types (e.g. STRUCT within STRUCT, ARRAY within STRUCT)
            val nestedResult = ComplexTypeParser.extractStructFields(field.rawType)
            if (nestedResult != null) {
                rows.addAll(buildSubColumnRows(childAssetMap, childQN, nestedResult, depth + 1))
            }
        }
        return rows
    }

    /**
     * Build the asset map for a single child column, inheriting all context fields from
     * [parentAssetMap] and overriding the column-specific fields.
     *
     * @param parentAssetMap asset map of the immediate parent column
     * @param parentColumnQN qualified name of the parent column (for [PARENT_COLUMN_QN_HEADER])
     * @param childQN qualified name for the child column
     * @param field field definition (name and raw type) for the child column
     * @param order ordinal position of the child column within its parent
     * @param depth nesting depth of this child column (1 for direct children of a top-level column, 2 for grandchildren, etc.)
     */
    private fun buildChildAssetMap(
        parentAssetMap: Map<String, String>,
        parentColumnQN: String,
        childQN: String,
        field: ComplexTypeParser.FieldDefinition,
        order: Int,
        depth: Int,
    ): Map<String, String> {
        val childMap = parentAssetMap.toMutableMap()
        childMap[RowSerde.getHeaderForField(Asset.QUALIFIED_NAME)] = childQN
        childMap[RowSerde.getHeaderForField(Asset.NAME)] = field.name
        childMap[RowSerde.getHeaderForField(Column.DATA_TYPE, Column::class.java)] = baseTypeName(field.rawType)
        childMap[RowSerde.getHeaderForField(Column.RAW_DATA_TYPE_DEFINITION, Column::class.java)] = field.rawType
        childMap[RowSerde.getHeaderForField(Column.ORDER, Column::class.java)] = order.toString()
        // Clear numeric type-specific fields — they're not meaningful for the child's raw type
        childMap[RowSerde.getHeaderForField(Column.PRECISION, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.NUMERIC_SCALE, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.MAX_LENGTH, Column::class.java)] = ""
        // Clear table/view references on sub-columns so they do not appear in the table's flat
        // column list (table_columns relationship). Navigation is via parentColumn chain instead.
        childMap[RowSerde.getHeaderForField(Column.TABLE_QUALIFIED_NAME, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.TABLE_NAME, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.TABLE, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.VIEW_QUALIFIED_NAME, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.VIEW_NAME, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.VIEW, Column::class.java)] = ""
        childMap[RowSerde.getHeaderForField(Column.MATERIALIZED_VIEW, Column::class.java)] = ""
        childMap[PARENT_COLUMN_QN_HEADER] = parentColumnQN
        childMap[PARENT_COLUMN_HEADER] = "${Column.TYPE_NAME}@$parentColumnQN"
        childMap[PARENT_COLUMN_NAME_HEADER] = parentColumnQN.substringAfterLast('/')
        childMap[NESTED_COLUMN_ORDER_HEADER] = order.toString()
        // columnDepthLevel tells Atlan this is a nested sub-column (not a top-level table column).
        childMap[COLUMN_DEPTH_LEVEL_HEADER] = depth.toString()
        // columnHierarchy lists all ancestor columns from depth-1 up to the immediate parent.
        // Each entry is a JSON object: {"depth":"<n>","qualifiedName":"<qn>","name":"<name>"}.
        // Multiple entries are newline-delimited (CellXformer.LIST_DELIMITER).
        // Matches the format used in AIM nested_columns.csv reference and Databricks connector.
        val parentHierarchyStr = parentAssetMap.getOrElse(COLUMN_HIERARCHY_HEADER) { "" }
        val parentName = parentColumnQN.substringAfterLast('/')
        val newEntry = """{"depth": "$depth","qualifiedName": "$parentColumnQN","name": "$parentName"}"""
        childMap[COLUMN_HIERARCHY_HEADER] = if (parentHierarchyStr.isBlank()) newEntry else "$parentHierarchyStr\n$newEntry"
        childMap[RowSerde.getHeaderForField(Asset.SUB_TYPE)] = "nested"
        return childMap
    }

    class Preprocessor(
        originalFile: String,
        fieldSeparator: Char,
        logger: KLogger,
        outputFile: String? = null,
        outputHeaders: List<String>? = null,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
            producesFile = outputFile,
            usingHeaders = outputHeaders,
            requiredHeaders = REQUIRED_HEADERS,
        ) {
        val invalidTypes = mutableSetOf<String>()
        val entityQualifiedNameToType = mutableMapOf<String, String>()
        val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToTableCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToViewCount = mutableMapOf<String, AtomicInteger>()

        var lastParentQN = ""
        var columnOrder = 1

        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            val values = row.toMutableList()
            val typeName = trimWhitespace(values.getOrElse(typeIdx) { "" })
            if (typeName.isNotBlank() && !RDBMS_TYPES.contains(typeName)) {
                invalidTypes.add(typeName)
            }
            if (!invalidTypes.contains(typeName)) {
                val qnDetails = getSQLHierarchyDetails(getRowByHeader(header, values), typeName)
                if (typeName !in setOf(Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME)) {
                    if (!qualifiedNameToChildCount.containsKey(qnDetails.parentUniqueQN)) {
                        qualifiedNameToChildCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                    }
                    qualifiedNameToChildCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                }
                when (typeName) {
                    Connection.TYPE_NAME, Database.TYPE_NAME, Schema.TYPE_NAME -> {
                        values.add("")
                        values.add("")
                    }

                    Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                        addContainerDetails(
                            if (typeName == Table.TYPE_NAME) qualifiedNameToTableCount else qualifiedNameToViewCount,
                            entityQualifiedNameToType,
                            qnDetails,
                            typeName,
                        )
                        values.add("")
                        values.add("")
                    }

                    Column.TYPE_NAME -> {
                        // If it is a column, calculate the order and parent qualifiedName and inject them
                        if (qnDetails.parentUniqueQN == lastParentQN) {
                            columnOrder += 1
                        } else {
                            lastParentQN = qnDetails.parentUniqueQN
                            columnOrder = 1
                        }
                        values.add("$columnOrder")
                        values.add(qnDetails.parentPartialQN)
                    }
                }
            }
            return values
        }

        private fun addContainerDetails(
            qnToCount: MutableMap<String, AtomicInteger>,
            entityQNToType: MutableMap<String, String>,
            qnDetails: SQLHierarchyDetails,
            typeName: String,
        ) {
            if (!qnToCount.containsKey(qnDetails.parentUniqueQN)) {
                qnToCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
            }
            qnToCount[qnDetails.parentUniqueQN]?.incrementAndGet()
            entityQNToType[qnDetails.uniqueQN] = typeName
        }

        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): Results {
            val results = super.finalize(header, outputFile)
            if (invalidTypes.isNotEmpty()) {
                throw IllegalArgumentException("Found non-relational assets supplied in the input file, which cannot be loaded through this utility: $invalidTypes")
            }
            return Results(
                results.hasLinks,
                results.hasTermAssignments,
                results.hasDomainRelationship,
                results.hasProductRelationship,
                results.outputFile!!,
                entityQualifiedNameToType,
                qualifiedNameToChildCount,
                qualifiedNameToTableCount,
                qualifiedNameToViewCount,
            )
        }
    }

    class Results(
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        hasDomainRelationship: Boolean,
        hasProductRelationship: Boolean,
        preprocessedFile: String,
        val entityQualifiedNameToType: Map<String, String>,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
        val qualifiedNameToTableCount: Map<String, AtomicInteger>,
        val qualifiedNameToViewCount: Map<String, AtomicInteger>,
    ) : DeltaProcessor.Results(
            assetRootName = "",
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            hasDomainRelationship = hasDomainRelationship,
            hasProductRelationship = hasProductRelationship,
            preprocessedFile = preprocessedFile,
        )
}
