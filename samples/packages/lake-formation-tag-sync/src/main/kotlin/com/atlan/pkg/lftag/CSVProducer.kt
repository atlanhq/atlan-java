/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.pkg.Utils
import com.atlan.pkg.lftag.model.LFTableInfo
import com.atlan.pkg.lftag.model.LFTagData
import com.atlan.pkg.serde.csv.CSVWriter
import com.fasterxml.jackson.databind.JsonNode

/**
 * Turns Lake Formation tag associations into an asset import CSV.
 *
 * @param connectionMap connection key to one or more candidate database qualifiedNames. The key is the full
 *                      LF database name when the map has it, otherwise the database name prefix up to the
 *                      first '_'. A key with a single candidate is always written
 *                      to that candidate. A key with several candidates writes each table to the one candidate
 *                      that holds it, as reported by [tableLocator].
 * @param metadataMap LF tag key to custom metadata attribute
 * @param tableLocator returns which of the given table qualifiedNames exist in Atlan; only called for keys
 *                     with more than one candidate
 */
class CSVProducer(
    private val connectionMap: Map<String, List<String>>,
    metadataMap: Map<String, String>,
    private val tableLocator: TableLocator? = null,
) {
    private val logger = Utils.getLogger(this.javaClass.name)
    private val tagToMetadataMapper = TagToMetadataMapper(metadataMap)
    private val headerNames =
        setOf(
            Asset.NAME.atlanFieldName,
            Asset.TYPE_NAME.atlanFieldName,
            Asset.QUALIFIED_NAME.atlanFieldName,
        ) + metadataMap.values.toSet()

    /** Finds which table-level assets exist, by qualifiedName. */
    fun interface TableLocator {
        /**
         * @param qualifiedNames table-level qualifiedNames to look for
         * @return the subset of [qualifiedNames] that exist as active assets
         */
        fun findExisting(qualifiedNames: Collection<String>): Set<String>
    }

    private data class TableEntry(
        val info: LFTableInfo,
        val connectionKey: String,
        val schemaName: String,
        val tableName: String,
    )

    fun transform(
        tagData: LFTagData,
        fileName: String,
        removeSchema: Boolean = false,
        keepDatabasePrefix: Boolean = false,
    ) {
        CSVWriter(fileName).use { csv ->
            csv.writeHeader(headerNames)
            val start = System.currentTimeMillis()
            val entries = mutableListOf<TableEntry>()
            val missingConnectionKeys = mutableSetOf<String>()
            tagData.tableList.forEach { tableInfo ->
                val table = tableInfo.table
                val parts = table.databaseName.split('_', limit = 2)
                if (parts.size < 2) {
                    logger.warn { "Skipping table ${table.name}: database name ${table.databaseName} has no '_' separating a connection key." }
                    return@forEach
                }
                // A key for the full database name overrides the prefix key, so one LF database can be sent
                // to a different connection than the rest of its environment.
                val connectionKey = if (table.databaseName in this.connectionMap) table.databaseName else parts[0]
                // By default the connection key prefix is stripped from the schema name; keep it when the
                // crawled schema name is the full database name.
                val schemaName = if (keepDatabasePrefix) table.databaseName else parts[1]
                val tableName = if (removeSchema) table.name.replace("$schemaName.", "") else table.name
                if (connectionKey in this.connectionMap) {
                    entries.add(TableEntry(tableInfo, connectionKey, schemaName, tableName))
                } else {
                    missingConnectionKeys.add(connectionKey)
                }
            }
            val located = locateTables(entries)
            var unresolved = 0
            var ambiguous = 0
            entries.forEach { entry ->
                val candidates = connectionMap.getValue(entry.connectionKey)
                val target =
                    if (candidates.size == 1) {
                        candidates[0]
                    } else {
                        val hits = candidates.filter { tableQualifiedName(it, entry) in located }
                        when (hits.size) {
                            1 -> {
                                hits[0]
                            }

                            0 -> {
                                logger.warn { "Skipping ${entry.schemaName}/${entry.tableName}: not found under any connection mapped to '${entry.connectionKey}'." }
                                unresolved++
                                null
                            }

                            else -> {
                                logger.warn { "Skipping ${entry.schemaName}/${entry.tableName}: found under more than one connection mapped to '${entry.connectionKey}': $hits" }
                                ambiguous++
                                null
                            }
                        }
                    }
                if (target != null) {
                    writeRows(csv, target, entry)
                }
            }
            if (missingConnectionKeys.isNotEmpty()) {
                logger.warn { "Skipped every table whose connection key is not in the connection map: $missingConnectionKeys" }
            }
            if (unresolved > 0 || ambiguous > 0) {
                logger.warn { "Tables not written because no mapped connection holds them: $unresolved; because several do: $ambiguous" }
            }
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
        }
    }

    private fun tableQualifiedName(
        databaseQualifiedName: String,
        entry: TableEntry,
    ) = "$databaseQualifiedName/${entry.schemaName}/${entry.tableName}"

    private fun locateTables(entries: List<TableEntry>): Set<String> {
        val toLocate =
            entries
                .filter { connectionMap.getValue(it.connectionKey).size > 1 }
                .flatMap { entry -> connectionMap.getValue(entry.connectionKey).map { tableQualifiedName(it, entry) } }
                .toSet()
        if (toLocate.isEmpty()) {
            return emptySet()
        }
        val locator =
            tableLocator
                ?: throw IllegalStateException("A connection map key lists several connections, but no table locator was provided.")
        return toLocate.chunked(LOOKUP_BATCH_SIZE).flatMap { locator.findExisting(it) }.toSet()
    }

    private fun writeRows(
        csv: CSVWriter,
        databaseQualifiedName: String,
        entry: TableEntry,
    ) {
        val tableInfo = entry.info
        val schemaQualifiedName = "$databaseQualifiedName/${entry.schemaName}"
        var row =
            mutableMapOf(
                Asset.QUALIFIED_NAME.atlanFieldName to schemaQualifiedName,
                Asset.TYPE_NAME.atlanFieldName to Schema.TYPE_NAME,
                Asset.NAME.atlanFieldName to entry.schemaName,
            )
        tagToMetadataMapper.getTagValues(tableInfo.lfTagOnDatabase, row)
        csv.writeRecord(row)
        val qualifiedName = "$schemaQualifiedName/${entry.tableName}"
        row =
            mutableMapOf(
                Asset.QUALIFIED_NAME.atlanFieldName to qualifiedName,
                Asset.TYPE_NAME.atlanFieldName to Table.TYPE_NAME,
                Asset.NAME.atlanFieldName to entry.tableName,
            )
        if (tableInfo.lfTagsOnTable != null) {
            tagToMetadataMapper.getTagValues(tableInfo.lfTagsOnTable!!, row)
            csv.writeRecord(row)
        }
        tableInfo.lfTagsOnColumn?.forEach { column ->
            val columnQualifiedName = "$qualifiedName/${column.name}"
            row =
                mutableMapOf(
                    Asset.QUALIFIED_NAME.atlanFieldName to columnQualifiedName,
                    Asset.TYPE_NAME.atlanFieldName to Column.TYPE_NAME,
                    Asset.NAME.atlanFieldName to column.name,
                )
            tagToMetadataMapper.getTagValues(column.lfTags, row)
            csv.writeRecord(row)
        }
    }

    companion object {
        private const val LOOKUP_BATCH_SIZE = 500

        /**
         * Read connection_map.json, where each value is either one database qualifiedName or a list of them.
         *
         * @throws IllegalArgumentException if a value is neither a string nor a non-empty list of strings
         */
        fun parseConnectionMap(root: JsonNode): Map<String, List<String>> {
            require(root.isObject) { "connection_map.json must be a JSON object." }
            val result = mutableMapOf<String, List<String>>()
            root.fields().forEach { (key, value) ->
                val candidates =
                    when {
                        value.isTextual -> listOf(value.asText())

                        value.isArray && value.size() > 0 && value.all { it.isTextual } -> value.map { it.asText() }.distinct()

                        else -> throw IllegalArgumentException(
                            "connection_map.json key '$key' must map to a qualifiedName or a non-empty list of qualifiedNames.",
                        )
                    }
                result[key] = candidates
            }
            return result
        }
    }
}
