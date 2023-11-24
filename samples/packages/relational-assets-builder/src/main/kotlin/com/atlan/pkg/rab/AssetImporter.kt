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
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.rab.Importer.clearField
import com.atlan.pkg.serde.RowDeserialization
import com.atlan.pkg.serde.RowDeserializer
import mu.KLogger
import kotlin.system.exitProcess

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
 * @param updateOnly if true, only update an asset (first check it exists), if false allow upserts (create if it does not exist)
 * @param batchSize maximum number of records to save per API request
 */
abstract class AssetImporter(
    private val filename: String,
    private val attrsToOverwrite: List<AtlanField>,
    private val updateOnly: Boolean,
    private val batchSize: Int,
    protected val typeNameFilter: String,
    protected val logger: KLogger,
) : AssetGenerator {
    // Can skip all of these columns when deserializing a row as they will be set by
    // the creator methods anyway
    private val skipColumns = setOf(
        Asset.CONNECTION_NAME.atlanFieldName,
        ConnectionImporter.CONNECTOR_TYPE,
        ISQL.DATABASE_NAME.atlanFieldName,
        ISQL.SCHEMA_NAME.atlanFieldName,
        ENTITY_NAME,
        ColumnImporter.COLUMN_PARENT_QN,
        Column.ORDER.atlanFieldName,
    )

    companion object {
        const val ENTITY_NAME = "entityName"

        /**
         * Calculate the qualifiedName components from a row of data, completely in-memory (no calls to Atlan).
         *
         * @param row of data
         * @param header list of column names giving their position
         * @param typeName for which to determine the qualifiedName
         * @return details about the qualifiedName(s) inherent in this row of data
         */
        fun getQualifiedNameDetails(row: List<String>, header: List<String>, typeName: String): QualifiedNameDetails {
            val parent: QualifiedNameDetails?
            val current: String
            when (typeName) {
                Connection.TYPE_NAME -> {
                    val connection = row[header.indexOf(Asset.CONNECTION_NAME.atlanFieldName)]
                    val connector = row[header.indexOf(ConnectionImporter.CONNECTOR_TYPE)]
                    current = "$connection/$connector"
                    parent = null
                }
                Database.TYPE_NAME -> {
                    current = row[header.indexOf(ISQL.DATABASE_NAME.atlanFieldName)]
                    parent = getQualifiedNameDetails(row, header, Connection.TYPE_NAME)
                }
                Schema.TYPE_NAME -> {
                    current = row[header.indexOf(ISQL.SCHEMA_NAME.atlanFieldName)]
                    parent = getQualifiedNameDetails(row, header, Database.TYPE_NAME)
                }
                Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                    current = row[header.indexOf(ENTITY_NAME)]
                    parent = getQualifiedNameDetails(row, header, Schema.TYPE_NAME)
                }
                Column.TYPE_NAME -> {
                    current = row[header.indexOf(ColumnImporter.COLUMN_NAME)]
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

    /**
     * Actually run the import.
     */
    fun import() {
        CSVReader(filename, updateOnly).use { csv ->
            val start = System.currentTimeMillis()
            val anyFailures = csv.streamRows(this, batchSize, logger, skipColumns)
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
            if (anyFailures) {
                logger.error { "Some errors detected, failing the workflow." }
                exitProcess(1)
            }
            cacheCreated(csv.created)
        }
    }

    /**
     * Translate a row of CSV values into an asset object, overwriting any attributes that were empty
     * in the CSV with blank values, per the job configuration.
     *
     * @param row of values in the CSV
     * @param header names of columns (and their position) in the header of the CSV
     * @param typeIdx index of the typeName column
     * @param skipColumns columns to skip, i.e. that need to be processed in a later pass
     * @return the deserialized asset object(s)
     */
    override fun buildFromRow(row: List<String>, header: List<String>, typeIdx: Int, skipColumns: Set<String>): RowDeserialization? {
        // Deserialize the objects represented in that row (could be more than one due to flattening
        // of in particular things like READMEs and Links)
        if (includeRow(row, header, typeIdx)) {
            val deserializer = RowDeserializer(
                heading = header,
                row = row,
                typeName = typeNameFilter,
                logger = logger,
                skipColumns = skipColumns,
            )
            val assets = deserializer.getAssets(getBuilder(deserializer))
            if (assets != null) {
                val builder = assets.primary
                val candidate = builder.build()
                val identity = RowDeserialization.AssetIdentity(candidate.typeName, candidate.qualifiedName)
                // Then apply any field clearances based on attributes configured in the job
                for (field in attrsToOverwrite) {
                    clearField(field, candidate, builder)
                    // If there are no related assets
                    if (!assets.related.containsKey(field.atlanFieldName)) {
                        assets.delete.add(field)
                    }
                }
                return RowDeserialization(identity, builder, assets.related, assets.delete)
            }
        }
        return null
    }

    /** {@inheritDoc} */
    override fun includeRow(row: List<String>, header: List<String>, typeIdx: Int): Boolean {
        return row[typeIdx] == typeNameFilter
    }

    data class QualifiedNameDetails(
        val uniqueQN: String,
        val partialQN: String,
        val parentUniqueQN: String,
        val parentPartialQN: String,
    )
}
