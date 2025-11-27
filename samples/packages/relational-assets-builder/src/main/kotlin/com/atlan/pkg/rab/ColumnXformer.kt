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
