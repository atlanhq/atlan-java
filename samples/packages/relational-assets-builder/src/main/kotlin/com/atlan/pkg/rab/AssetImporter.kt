/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.ISQL
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.serde.csv.CSVImporter
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.ImportResults
import com.atlan.pkg.util.AssetResolver
import com.atlan.pkg.util.AssetResolver.ConnectionIdentity
import com.atlan.pkg.util.AssetResolver.QualifiedNameDetails
import mu.KLogger

/**
 * Import assets into Atlan from a provided CSV file.
 *
 * Only the assets and attributes in the provided CSV file will attempt to be loaded.
 * By default, any blank values in a cell in the CSV file will be ignored. If you would like any
 * particular column's blank values to actually overwrite (i.e. remove) existing values for that
 * asset in Atlan, then add that column's field to getAttributesToOverwrite.
 *
 * @param filename name of the file to import
 * @param attrsToOverwrite list of fields that should be overwritten in Atlan, if their value is empty in the CSV
 * @param creationHandling what to do with assets that do not exist (create full, partial, or ignore)
 * @param batchSize maximum number of records to save per API request
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
abstract class AssetImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val creationHandling: AssetCreationHandling,
    private val batchSize: Int,
    typeNameFilter: String,
    logger: KLogger,
    trackBatches: Boolean,
    fieldSeparator: Char,
) : CSVImporter(
    filename,
    logger,
    typeNameFilter,
    attrsToOverwrite,
    creationHandling = creationHandling,
    batchSize = batchSize,
    trackBatches = trackBatches,
    fieldSeparator = fieldSeparator,
) {

    /** {@inheritDoc} */
    override fun import(columnsToSkip: Set<String>): ImportResults? {
        // Can skip all of these columns when deserializing a row as they will be set by
        // the creator methods anyway
        return super.import(
            setOf(
                Asset.CONNECTION_NAME.atlanFieldName,
                // ConnectionImporter.CONNECTOR_TYPE, // Let this be loaded, for mis-named connections
                ISQL.DATABASE_NAME.atlanFieldName,
                ISQL.SCHEMA_NAME.atlanFieldName,
                ENTITY_NAME,
                ColumnImporter.COLUMN_PARENT_QN,
                Column.ORDER.atlanFieldName,
            ),
        )
    }

    companion object : AssetResolver {
        const val ENTITY_NAME = "entityName"

        /** {@inheritDoc} */
        override fun getQualifiedNameDetails(row: List<String>, header: List<String>, typeName: String): QualifiedNameDetails {
            val parent: QualifiedNameDetails?
            val current: String
            when (typeName) {
                Connection.TYPE_NAME -> {
                    val connection = CSVXformer.trimWhitespace(row[header.indexOf(Asset.CONNECTION_NAME.atlanFieldName)])
                    val connector = CSVXformer.trimWhitespace(row[header.indexOf(ConnectionImporter.CONNECTOR_TYPE)])
                    current = ConnectionIdentity(connection, connector).toString()
                    parent = null
                }
                Database.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(ISQL.DATABASE_NAME.atlanFieldName)])
                    parent = getQualifiedNameDetails(row, header, Connection.TYPE_NAME)
                }
                Schema.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(ISQL.SCHEMA_NAME.atlanFieldName)])
                    parent = getQualifiedNameDetails(row, header, Database.TYPE_NAME)
                }
                Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(ENTITY_NAME)])
                    parent = getQualifiedNameDetails(row, header, Schema.TYPE_NAME)
                }
                Column.TYPE_NAME -> {
                    current = CSVXformer.trimWhitespace(row[header.indexOf(ColumnImporter.COLUMN_NAME)])
                    parent = getQualifiedNameDetails(row, header, Table.TYPE_NAME)
                }
                else -> throw IllegalStateException("Unknown SQL type: $typeName")
            }
            val unique = parent?.let {
                if (parent.uniqueQN.isBlank()) current else "${parent.uniqueQN}/$current"
            } ?: current
            val partial = parent?.let {
                if (parent.partialQN.isBlank()) current else "${parent.partialQN}/$current"
            } ?: ""
            return QualifiedNameDetails(
                unique,
                partial,
                parent?.uniqueQN ?: "",
                parent?.partialQN ?: "",
            )
        }
    }
}
