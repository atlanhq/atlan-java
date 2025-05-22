/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.ISQL
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.pkg.PackageContext
import com.atlan.pkg.serde.RowSerde
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.util.AssetResolver.ConnectionIdentity
import mu.KLogger

abstract class AssetXformer(
    private val ctx: PackageContext<RelationalAssetsBuilderCfg>,
    completeHeaders: List<String>,
    val typeNameFilter: String,
    val preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : CSVXformer(
        inputFile = preprocessedDetails.preprocessedFile,
        targetHeader = completeHeaders,
        logger = logger,
        fieldSeparator = ctx.config.assetsFieldSeparator[0],
    ) {
    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val assetMap = mapAsset(inputRow)
        val valueList = mutableListOf<String>()
        targetHeader!!.forEach { header ->
            if (header != null) {
                // Look for the transformed value first, then fallback to passing through what came in the input
                val transformed = assetMap.getOrElse(header) { inputRow.getOrElse(header) { "" } }
                valueList.add(transformed)
            }
        }
        return listOf(valueList)
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean = trimWhitespace(inputRow.getOrElse(Asset.TYPE_NAME.atlanFieldName) { "" }) == typeNameFilter

    abstract fun mapAsset(inputRow: Map<String, String>): Map<String, String>

    fun getConnectionQN(inputRow: Map<String, String>): String {
        val connectorType = getConnectorType(inputRow)
        val connectionName = trimWhitespace(inputRow.getOrElse(Asset.CONNECTION_NAME.atlanFieldName) { "" })
        return "{{$connectorType/$connectionName}}"
    }

    companion object {
        const val ENTITY_NAME = "entityName"

        val BASE_OUTPUT_HEADERS =
            listOf(
                RowSerde.getHeaderForField(Asset.QUALIFIED_NAME),
                RowSerde.getHeaderForField(Asset.TYPE_NAME),
                RowSerde.getHeaderForField(Asset.NAME),
                RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE),
                RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME),
                RowSerde.getHeaderForField(Database.SCHEMA_COUNT, Database::class.java),
                RowSerde.getHeaderForField(Schema.DATABASE_NAME, Schema::class.java),
                RowSerde.getHeaderForField(Schema.DATABASE_QUALIFIED_NAME, Schema::class.java),
                RowSerde.getHeaderForField(Schema.DATABASE, Schema::class.java),
                RowSerde.getHeaderForField(Schema.TABLE_COUNT, Schema::class.java),
                RowSerde.getHeaderForField(Schema.VIEW_COUNT, Schema::class.java),
                RowSerde.getHeaderForField(Table.SCHEMA_NAME, Table::class.java),
                RowSerde.getHeaderForField(Table.SCHEMA_QUALIFIED_NAME, Table::class.java),
                RowSerde.getHeaderForField(Table.SCHEMA, Table::class.java),
                RowSerde.getHeaderForField(Table.COLUMN_COUNT, Table::class.java),
                RowSerde.getHeaderForField(Column.TABLE_NAME, Column::class.java),
                RowSerde.getHeaderForField(Column.TABLE_QUALIFIED_NAME, Column::class.java),
                RowSerde.getHeaderForField(Column.TABLE, Column::class.java),
                RowSerde.getHeaderForField(Column.VIEW_NAME, Column::class.java),
                RowSerde.getHeaderForField(Column.VIEW_QUALIFIED_NAME, Column::class.java),
                RowSerde.getHeaderForField(Column.VIEW, Column::class.java),
                RowSerde.getHeaderForField(Column.MATERIALIZED_VIEW, Column::class.java),
                RowSerde.getHeaderForField(Column.ORDER, Column::class.java),
                RowSerde.getHeaderForField(Column.RAW_DATA_TYPE_DEFINITION, Column::class.java),
                RowSerde.getHeaderForField(Column.PRECISION, Column::class.java),
                RowSerde.getHeaderForField(Column.NUMERIC_SCALE, Column::class.java),
                RowSerde.getHeaderForField(Column.MAX_LENGTH, Column::class.java),
            )

        fun getConnectorType(inputRow: Map<String, String>): String =
            trimWhitespace(
                inputRow.getOrElse("connectorName") {
                    inputRow.getOrElse("connectorType") {
                        inputRow.getOrElse("customConnectorType") { "" }
                    }
                },
            ).lowercase()

        /**
         * Attempt to resolve the full SQL hierarchy details of a row (asset).
         * Note: when the entityQualifiedNameToType is not passed, only very limited details can be resolved and returned,
         * so use with caution unless you're able to provide the entityQualifiedNameToType map.
         *
         * @param row of data, representing a single asset
         * @param typeName of that single row's asset
         * @param entityQualifiedNameToType a map from unresolved (unique) qualifiedName of an asset to its type
         */
        fun getSQLHierarchyDetails(
            row: Map<String, String>,
            typeName: String,
            entityQualifiedNameToType: Map<String, String>? = null,
        ): SQLHierarchyDetails {
            val parent: SQLHierarchyDetails?
            val current: String
            var actualTypeName = typeName
            when (typeName) {
                Connection.TYPE_NAME -> {
                    val connection = trimWhitespace(row.getOrElse(Asset.CONNECTION_NAME.atlanFieldName) { "" })
                    val connector = trimWhitespace(row.getOrElse(ConnectionXformer.CONNECTOR_TYPE) { "" }).lowercase()
                    current = ConnectionIdentity(connection, connector).toString()
                    parent = null
                }
                Database.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ISQL.DATABASE_NAME.atlanFieldName) { "" })
                    parent = getSQLHierarchyDetails(row, Connection.TYPE_NAME, entityQualifiedNameToType)
                }
                Schema.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ISQL.SCHEMA_NAME.atlanFieldName) { "" })
                    parent = getSQLHierarchyDetails(row, Database.TYPE_NAME, entityQualifiedNameToType)
                }
                "CONTAINER", Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ENTITY_NAME) { "" })
                    parent = getSQLHierarchyDetails(row, Schema.TYPE_NAME, entityQualifiedNameToType)
                    // Only do this lookup if we have been passed a map -- otherwise this is detail that cannot
                    // yet be resolved (and will not yet be used, either)
                    if (entityQualifiedNameToType != null) {
                        actualTypeName =
                            entityQualifiedNameToType.getOrElse("${parent.uniqueQN}/$current") {
                                throw IllegalStateException("Could not find any table/view at: ${parent.uniqueQN}/$current")
                            }
                    }
                }
                Column.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ColumnXformer.COLUMN_NAME) { "" })
                    parent = getSQLHierarchyDetails(row, "CONTAINER", entityQualifiedNameToType)
                }
                else -> throw IllegalStateException("Unknown SQL type: $typeName")
            }
            val unique =
                parent?.let {
                    if (parent.uniqueQN.isBlank()) current else "${parent.uniqueQN}/$current"
                } ?: current
            val partial =
                parent?.let {
                    if (parent.partialQN.isBlank()) current else "${parent.partialQN}/$current"
                } ?: ""
            var databaseName = ""
            var databasePQN = ""
            var schemaName = ""
            var schemaPQN = ""
            var tableName = ""
            var tablePQN = ""
            var viewName = ""
            var viewPQN = ""
            when (actualTypeName) {
                Schema.TYPE_NAME -> {
                    databaseName = parent?.name ?: ""
                    databasePQN = parent?.partialQN ?: ""
                }
                Table.TYPE_NAME -> {
                    databaseName = parent?.databaseName ?: ""
                    databasePQN = parent?.databasePQN ?: ""
                    schemaName = parent?.name ?: ""
                    schemaPQN = parent?.partialQN ?: ""
                }
                View.TYPE_NAME -> {
                    databaseName = parent?.databaseName ?: ""
                    databasePQN = parent?.databasePQN ?: ""
                    schemaName = parent?.name ?: ""
                    schemaPQN = parent?.partialQN ?: ""
                }
                MaterializedView.TYPE_NAME -> {
                    databaseName = parent?.databaseName ?: ""
                    databasePQN = parent?.databasePQN ?: ""
                    schemaName = parent?.name ?: ""
                    schemaPQN = parent?.partialQN ?: ""
                }
                Column.TYPE_NAME -> {
                    databaseName = parent?.databaseName ?: ""
                    databasePQN = parent?.databasePQN ?: ""
                    schemaName = parent?.schemaName ?: ""
                    schemaPQN = parent?.schemaPQN ?: ""
                    val parentType = parent?.typeName ?: ""
                    when (parentType) {
                        Table.TYPE_NAME -> {
                            tableName = parent?.name ?: ""
                            tablePQN = parent?.partialQN ?: ""
                        }
                        View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                            viewName = parent?.name ?: ""
                            viewPQN = parent?.partialQN ?: ""
                        }
                    }
                }
            }
            return SQLHierarchyDetails(
                current,
                partial,
                unique,
                actualTypeName,
                parent?.name ?: "",
                parent?.partialQN ?: "",
                parent?.uniqueQN ?: "",
                parent?.typeName ?: "",
                databaseName,
                databasePQN,
                schemaName,
                schemaPQN,
                tableName,
                tablePQN,
                viewName,
                viewPQN,
            )
        }
    }

    data class SQLHierarchyDetails(
        val name: String,
        val partialQN: String,
        val uniqueQN: String,
        val typeName: String,
        val parentName: String,
        val parentPartialQN: String,
        val parentUniqueQN: String,
        val parentTypeName: String,
        val databaseName: String,
        val databasePQN: String,
        val schemaName: String,
        val schemaPQN: String,
        val tableName: String,
        val tablePQN: String,
        val viewName: String,
        val viewPQN: String,
    )
}
