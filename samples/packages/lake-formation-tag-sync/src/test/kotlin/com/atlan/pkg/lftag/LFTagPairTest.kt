/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

private const val TAG_KEY = "privacy_sensitivity"
private const val CATALOG_ID = "614518280298"
private const val JSON = """{"TagKey":"$TAG_KEY","TagValues":["public"],"CatalogId":"$CATALOG_ID"}"""
private val mapper = jacksonObjectMapper()
class LFTagPairTest {

    private val tagValues = listOf("public")

    @Test
    fun whenSerializableLFTagThenSuccess() {
        val lfTagPair = LFTagPair(TAG_KEY, tagValues, "614518280298")
        val serialized = mapper.writeValueAsString(lfTagPair)
        assertEquals(serialized, JSON)
    }

    @Test
    fun whenDeserializableLFTagThenSuccess() {
        val lfTagPair = mapper.readValue(JSON, LFTagPair::class.java)
        assertEquals(TAG_KEY, lfTagPair.tagKey)
        assertEquals(CATALOG_ID, lfTagPair.catalogId)
        assertEquals(tagValues, lfTagPair.tagValues)
    }
}
