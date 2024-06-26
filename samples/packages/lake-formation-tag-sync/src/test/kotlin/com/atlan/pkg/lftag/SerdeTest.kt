/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.Assert
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val TAG_KEY = "privacy_sensitivity"
private const val CATALOG_ID = "614518280298"
private const val JSON = """{"TagKey":"$TAG_KEY","TagValues":["public"],"CatalogId":"$CATALOG_ID"}"""
private val mapper = jacksonObjectMapper()
class SerdeTest {

    private val tagValues = listOf("public")
    val tableInfoJson = """
        {
          "Table": {
            "CatalogId": "614518280298",
            "DatabaseName": "dev_atlan_dev",
            "Name": "stg_customer_categories"
          },
          "LFTagOnDatabase": [
            {
              "CatalogId": "614518280298",
              "TagKey": "security_classification",
              "TagValues": [
                "public"
              ]
            }
          ],
          "LFTagsOnTable": [
            {
              "CatalogId": "614518280298",
              "TagKey": "security_classification",
              "TagValues": [
                "public"
              ]
            }
          ],
          "LFTagsOnColumns": [
            {
              "Name": "club_code",
              "LFTags": [
                {
                  "CatalogId": "614518280298",
                  "TagKey": "security_classification",
                  "TagValues": [
                    "private"
                  ]
                }
              ]
            }
          ]
        }
    """.trimIndent()

    @Test
    fun whenDeserializableTableThenSuccess() {
        val json = """
        {
            "CatalogId": "614518280298",
            "DatabaseName": "dev_atlan_dev",
            "Name": "stg_customer_categories"
        }
        """.trimIndent()
        val table = mapper.readValue(json, Table::class.java)
        assertEquals("614518280298", table.catalogId)
        assertEquals("dev_atlan_dev", table.databaseName)
        assertEquals("stg_customer_categories", table.name)
    }

    @Test
    fun whenDeserializableTableInfoThenSuccess() {
        val tableInfo = mapper.readValue(tableInfoJson, TableInfo::class.java)
        assertNotNull(tableInfo.table)
        assertEquals(1, tableInfo.lfTagOnDatabase.size)
        assertEquals(1, tableInfo.lfTagsOnTable.size)
        assertEquals(1, tableInfo.lfTagsOnColumn.size)
    }

    @Test
    fun getTagValuesByTagKey() {
        val tagValuesByTagKey = mutableMapOf<String, MutableSet<String>>()
        val tableInfo = mapper.readValue(tableInfoJson, TableInfo::class.java)
        assertEquals(mapOf("security_classification" to setOf("public", "private")), tableInfo.getTagValuesByTagKey(tagValuesByTagKey))
    }

    @Test
    fun whenSerializableLFTagThenSuccess() {
        val lfTagPair = LFTagPair(TAG_KEY, tagValues, "614518280298")
        val serialized = mapper.writeValueAsString(lfTagPair)
        Assert.assertEquals(serialized, JSON)
    }

    @Test
    fun whenDeserializableLFTagThenSuccess() {
        val lfTagPair = mapper.readValue(JSON, LFTagPair::class.java)
        Assert.assertEquals(TAG_KEY, lfTagPair.tagKey)
        Assert.assertEquals(CATALOG_ID, lfTagPair.catalogId)
        Assert.assertEquals(tagValues, lfTagPair.tagValues)
    }

    @Test
    fun whenDeserializableColumnLFTagThenSuccess() {
        val json = """
        {
          "Name": "club_code",
          "LFTags": [
            {
              "CatalogId": "614518280298",
              "TagKey": "security_classification",
              "TagValues": [
                "public"
              ]
            }
          ]
        }
        """.trimIndent()
        val columnLFTag = mapper.readValue(json, ColumnLFTag::class.java)
        assertEquals("club_code", columnLFTag.name)
        assertEquals(1, columnLFTag.lfTags.size)
    }

    @Test
    fun whenDeserializableTagDataThenSuccess() {
        val jsonString: String = File("./src/test/resources/lftag_association_1.json").readText(Charsets.UTF_8)
        val tagData = mapper.readValue(jsonString, TagData::class.java)
        assertEquals(1, tagData.tableList.size)
    }
}
