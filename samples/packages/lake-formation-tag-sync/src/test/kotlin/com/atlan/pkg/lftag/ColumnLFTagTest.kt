/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.annotations.Test
import kotlin.test.assertEquals
private val mapper = jacksonObjectMapper()
class ColumnLFTagTest {

    @Test
    fun whenDeserializableColumnLFTagThenSuccess() {
        var json = """
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
}
