/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Table
import com.atlan.pkg.lftag.model.LFTagData
import com.atlan.pkg.serde.csv.CSVWriter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import java.io.File

class CSVProducer(
    private val connectionMap: Map<String, String>,
    metadataMap: Map<String, String>,
) {
    private val logger = KotlinLogging.logger {}
    private val mapper = jacksonObjectMapper()
    private val missingConnectionKeys = mutableSetOf<String>()
    private val tagToMetadataMapper = TagToMetadataMapper(metadataMap)
    private val headerNames = setOf(
        Asset.NAME.atlanFieldName,
        Asset.TYPE_NAME.atlanFieldName,
        Asset.QUALIFIED_NAME.atlanFieldName,
    ) + metadataMap.values.toSet()

    fun transform(inputFileName: String, fileName: String) {
        CSVWriter(fileName).use { csv ->
            csv.writeHeader(headerNames)
            val start = System.currentTimeMillis()
            val jsonString: String = File(inputFileName).readText(Charsets.UTF_8)
            val tagData = mapper.readValue(jsonString, LFTagData::class.java)
            tagData.tableList.forEach { tableInfo ->
                val table = tableInfo.table
                val (connectionKey, schemaName) = table.databaseName.split('_', limit = 2)
                if (connectionKey in this.connectionMap) {
                    val qualifiedName = "${connectionMap.getValue(connectionKey)}/$schemaName/${table.name}"
                    var row = mutableMapOf(
                        Asset.QUALIFIED_NAME.atlanFieldName to qualifiedName,
                        Asset.TYPE_NAME.atlanFieldName to Table.TYPE_NAME,
                        Asset.NAME.atlanFieldName to table.name,
                    )
                    tagToMetadataMapper.getTagValues(tableInfo.lfTagsOnTable, row)
                    csv.writeRecord(row)
                    tableInfo.lfTagsOnColumn.forEach { column ->
                        val columnQualifiedName = "$qualifiedName/${column.name}"
                        row = mutableMapOf(
                            Asset.QUALIFIED_NAME.atlanFieldName to columnQualifiedName,
                            Asset.TYPE_NAME.atlanFieldName to Column.TYPE_NAME,
                            Asset.NAME.atlanFieldName to table.name,
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
