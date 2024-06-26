/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */

package com.atlan.pkg.lftag

import com.atlan.pkg.serde.csv.CSVWriter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import java.io.File

private const val NAME = "name"

private const val TYPE_NAME = "typeName"

private const val QUALIFIED_NAME = "qualifiedName"

class CSVProducer(
    private val connectionMap: Map<String, String>,
    metadataMap: Map<String, String>,
    private val outputDirName: String,
) {
    private val logger = KotlinLogging.logger {}
    private val mapper = jacksonObjectMapper()
    private val missingConnectionKeys = mutableSetOf<String>()
    private val tagToMetadataMapper = TagToMetadataMapper(metadataMap)
    private val headerNames = setOf(NAME, TYPE_NAME, QUALIFIED_NAME) + (metadataMap.map { (_, value) -> value }).toSet()

    fun transform(inputFileName: String, outputFileName: String) {
        val fileName = "${this.outputDirName}${File.separator}$outputFileName"

        CSVWriter(fileName).use { csv ->
            csv.writeHeader(headerNames)
            val start = System.currentTimeMillis()
            val jsonString: String = File(inputFileName).readText(Charsets.UTF_8)
            val tagData = mapper.readValue(jsonString, TagData::class.java)
            tagData.tableList.forEach { tableInfo ->
                val table = tableInfo.table
                val (connectionKey, schemaName) = table.databaseName.split('_', limit = 2)
                if (connectionKey in this.connectionMap) {
                    val qualifiedName = "${connectionMap.getValue(connectionKey)}/$schemaName/${table.name}"
                    var row = mutableMapOf(QUALIFIED_NAME to qualifiedName, TYPE_NAME to "Table", NAME to table.name)
                    tagToMetadataMapper.getTagValues(tableInfo.lfTagsOnTable, row)
                    csv.writeHeader(rowAsList(row))
                    tableInfo.lfTagsOnColumn.forEach { column ->
                        val columnQualifiedName = "$qualifiedName/${column.name}"
                        row = mutableMapOf(
                            QUALIFIED_NAME to columnQualifiedName,
                            TYPE_NAME to "Column",
                            NAME to table.name,
                        )
                        tagToMetadataMapper.getTagValues(column.lfTags, row)
                        csv.writeHeader(rowAsList(row))
                    }
                } else {
                    missingConnectionKeys.add(connectionKey)
                }
            }
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
        }
    }

    private fun rowAsList(row: Map<String, String>): List<String> {
        val output = mutableListOf<String>()
        headerNames.forEach { header ->
            output.add(row.getOrDefault(header, ""))
        }
        return output
    }
}
