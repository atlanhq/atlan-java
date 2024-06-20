/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertEquals

private val mapper = jacksonObjectMapper()

class TagDataTest {
    @Test
    fun whenDeserializableTagDataThenSuccess() {
        val jsonString: String = File("./src/test/resources/sample.json").readText(Charsets.UTF_8)
        val tagData = mapper.readValue(jsonString, TagData::class.java)
        assertEquals(1, tagData.tableList.size)
    }
}
