/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.pkg.lftag.model.LFTagData
import com.atlan.pkg.serde.csv.CSVWriter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging

class CSVProducer(
    private val connectionMap: Map<String, String>,
    metadataMap: Map<String, String>,
) {
    private val logger = KotlinLogging.logger {}
    private val mapper = jacksonObjectMapper()
    private val missingConnectionKeys = mutableSetOf<String>()
    private val tagToMetadataMapper = TagToMetadataMapper(metadataMap)
    private val headerNames =
        setOf(
            Asset.NAME.atlanFieldName,
            Asset.TYPE_NAME.atlanFieldName,
            Asset.QUALIFIED_NAME.atlanFieldName,
        ) + metadataMap.values.toSet()

    fun transform(
        tagData: LFTagData,
        fileName: String,
    ) {
        CSVWriter(fileName).use { csv ->
            csv.writeHeader(headerNames)
            val start = System.currentTimeMillis()
            tagData.tableList.forEach { tableInfo ->
                val table = tableInfo.table
                val (connectionKey, schemaName) = table.databaseName.split('_', limit = 2)
                if (connectionKey in this.connectionMap) {
                    val schemaQualifiedName = "${connectionMap.getValue(connectionKey)}/$schemaName"
                    var row =
                        mutableMapOf(
                            Asset.QUALIFIED_NAME.atlanFieldName to schemaQualifiedName,
                            Asset.TYPE_NAME.atlanFieldName to Schema.TYPE_NAME,
                            Asset.NAME.atlanFieldName to schemaName,
                        )
                    tagToMetadataMapper.getTagValues(tableInfo.lfTagOnDatabase, row)
                    csv.writeRecord(row)
                    val qualifiedName = "$schemaQualifiedName/${table.name}"
                    row =
                        mutableMapOf(
                            Asset.QUALIFIED_NAME.atlanFieldName to qualifiedName,
                            Asset.TYPE_NAME.atlanFieldName to Table.TYPE_NAME,
                            Asset.NAME.atlanFieldName to table.name,
                        )
                    tagToMetadataMapper.getTagValues(tableInfo.lfTagsOnTable, row)
                    csv.writeRecord(row)
                    tableInfo.lfTagsOnColumn.forEach { column ->
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
                } else {
                    missingConnectionKeys.add(connectionKey)
                }
            }
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
        }
    }
}
