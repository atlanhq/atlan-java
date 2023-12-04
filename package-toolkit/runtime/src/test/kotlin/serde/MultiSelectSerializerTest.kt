/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.pkg.Utils.MAPPER
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.test.Test
import kotlin.test.assertEquals

class MultiSelectSerializerTest {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class TestClass(
        @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class) val property: List<String>?,
    )

    companion object {
        private val EMPTY = TestClass(
            property = listOf(),
        )
        private val NULL = null
        private val ONE_VALUE = TestClass(
            property = listOf("ONE"),
        )
        private val MULTI_VALUE = TestClass(
            property = listOf("ONE", "TWO"),
        )
        private val MAPPER = jacksonObjectMapper()
    }

    @Test
    fun testEmpty() {
        val result = MAPPER.writeValueAsString(EMPTY)
        assertEquals(
            """
            {"property":"[]"}
            """.trimIndent(),
            result,
        )
    }

    @Test
    fun testNull() {
        val result = MAPPER.writeValueAsString(NULL)
        assertEquals("null", result)
    }

    @Test
    fun testOneValue() {
        val result = MAPPER.writeValueAsString(ONE_VALUE)
        assertEquals(
            """
            {"property":"[\"ONE\"]"}
            """.trimIndent(),
            result,
        )
    }

    @Test
    fun testMultiValue() {
        val result = MAPPER.writeValueAsString(MULTI_VALUE)
        assertEquals(
            """
            {"property":"[\"ONE\",\"TWO\"]"}
            """.trimIndent(),
            result,
        )
    }
}
