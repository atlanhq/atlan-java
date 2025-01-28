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
    val typeNameFilter: String,
    val preprocessedDetails: Importer.Results,
    private val logger: KLogger,
) : CSVXformer(
    inputFile = preprocessedDetails.preprocessedFile,
    targetHeader = BASE_OUTPUT_HEADERS,
    logger = logger,
    fieldSeparator = ctx.config.assetsFieldSeparator[0],
) {

    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        val assetMap = mapAsset(inputRow)
        val valueList = mutableListOf<String>()
        targetHeader!!.forEach { header ->
            if (header != null) {
                valueList.add(assetMap.getOrElse(header) { "" })
            }
        }
        return listOf(valueList)
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        return trimWhitespace(inputRow.getOrElse(Asset.TYPE_NAME.atlanFieldName) { "" }) == typeNameFilter
    }

    abstract fun mapAsset(inputRow: Map<String, String>): Map<String, String>

    companion object {
        const val ENTITY_NAME = "entityName"

        val BASE_OUTPUT_HEADERS = listOf(
            RowSerde.getHeaderForField(Asset.QUALIFIED_NAME),
            RowSerde.getHeaderForField(Asset.TYPE_NAME),
            RowSerde.getHeaderForField(Asset.NAME),
            RowSerde.getHeaderForField(Asset.CONNECTOR_TYPE),
            RowSerde.getHeaderForField(Asset.CONNECTION_QUALIFIED_NAME),
            RowSerde.getHeaderForField(Database.SCHEMA_COUNT, Database::class.java), // TODO: new
            RowSerde.getHeaderForField(Schema.DATABASE_NAME, Schema::class.java),
            RowSerde.getHeaderForField(Schema.DATABASE_QUALIFIED_NAME, Schema::class.java),
            RowSerde.getHeaderForField(Schema.DATABASE, Schema::class.java),
            RowSerde.getHeaderForField(Schema.TABLE_COUNT, Schema::class.java), // TODO: new
            RowSerde.getHeaderForField(Schema.VIEW_COUNT, Schema::class.java), // TODO: new
            RowSerde.getHeaderForField(Table.SCHEMA_NAME, Table::class.java),
            RowSerde.getHeaderForField(Table.SCHEMA_QUALIFIED_NAME, Table::class.java),
            RowSerde.getHeaderForField(Table.SCHEMA, Table::class.java),
            RowSerde.getHeaderForField(Table.COLUMN_COUNT, Table::class.java), // TODO: new
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

        fun getConnectionQN(
            ctx: PackageContext<RelationalAssetsBuilderCfg>,
            inputRow: Map<String, String>,
        ): String {
            val connectorType = trimWhitespace(inputRow.getOrElse("connectorType") { "" })
            val connectionName = trimWhitespace(inputRow.getOrElse(Asset.CONNECTION_NAME.atlanFieldName) { "" })
            val connectionId = ConnectionIdentity(connectionName, connectorType)
            return ctx.connectionCache.getIdentityMap().getOrDefault(connectionId, "")
        }

        fun getConnectorType(
            inputRow: Map<String, String>,
        ): String {
            return trimWhitespace(inputRow.getOrElse("connectorType") { "" })
        }

        /** {@inheritDoc} */
        fun getSQLHierarchyDetails(
            row: Map<String, String>,
            typeName: String,
        ): SQLHierarchyDetails {
            val parent: SQLHierarchyDetails?
            val current: String
            when (typeName) {
                Connection.TYPE_NAME -> {
                    val connection = trimWhitespace(row.getOrElse(Asset.CONNECTION_NAME.atlanFieldName) { "" })
                    val connector = trimWhitespace(row.getOrElse(ConnectionImporter.CONNECTOR_TYPE) { "" }).lowercase()
                    current = ConnectionIdentity(connection, connector).toString()
                    parent = null
                }
                Database.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ISQL.DATABASE_NAME.atlanFieldName) { "" })
                    parent = getSQLHierarchyDetails(row, Connection.TYPE_NAME)
                }
                Schema.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ISQL.SCHEMA_NAME.atlanFieldName) { "" })
                    parent = getSQLHierarchyDetails(row, Database.TYPE_NAME)
                }
                Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ENTITY_NAME) { "" })
                    parent = getSQLHierarchyDetails(row, Schema.TYPE_NAME)
                }
                Column.TYPE_NAME -> {
                    current = trimWhitespace(row.getOrElse(ColumnImporter.COLUMN_NAME) { "" })
                    parent = getSQLHierarchyDetails(row, Table.TYPE_NAME)
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
            return SQLHierarchyDetails(
                current,
                partial,
                unique,
                typeName,
                parent?.name ?: "",
                parent?.partialQN ?: "",
                parent?.uniqueQN ?: "",
                parent?.typeName ?: "",
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
    )
}
