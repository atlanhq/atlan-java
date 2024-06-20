/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val mapper = jacksonObjectMapper()

class TableInfoTest {

    @Test
    fun whenDeserializableTableInfoThenSuccess() {
        var json = """
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
                    "public"
                  ]
                }
              ]
            }
          ]
        }
        """.trimIndent()
        val tableInfo = mapper.readValue(json, TableInfo::class.java)
        assertNotNull(tableInfo.table)
        assertEquals(1, tableInfo.lfTagOnDatabase.size)
        assertEquals(1, tableInfo.lfTagsOnTable.size)
        assertEquals(1, tableInfo.lfTagsOnColumn.size)
    }
}
