/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.pkg.serde.MultiSelectDeserializer
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MultiSelectDeserializerTest {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class TestClass(
        @JsonDeserialize(using = MultiSelectDeserializer::class) val property: List<String>?,
    )

    companion object {
        private const val EMPTY = """
            {}
        """
        private const val NULL = """
            {
                "property": null
            }
        """
        private const val NON_ARRAY = """
            {
                "property": "ONE"
            }
        """
        private const val ARRAY = """
            {
                "property": "[\"ONE\", \"TWO\"]"
            }
        """
        private val MAPPER = jacksonObjectMapper()
    }

    @Test
    fun testEmpty() {
        val result = MAPPER.readValue<TestClass>(EMPTY)
        assertNull(result.property)
    }

    @Test
    fun testNull() {
        val result = MAPPER.readValue<TestClass>(NULL)
        assertTrue(result.property.isNullOrEmpty())
    }

    @Test
    fun testNonArray() {
        val result = MAPPER.readValue<TestClass>(NON_ARRAY)
        assertTrue(result.property!!.isNotEmpty())
        assertEquals(1, result.property.size)
        assertEquals("ONE", result.property[0])
    }

    @Test
    fun testArray() {
        val result = MAPPER.readValue<TestClass>(ARRAY)
        assertTrue(result.property!!.isNotEmpty())
        assertEquals(2, result.property.size)
        assertEquals("ONE", result.property[0])
        assertEquals("TWO", result.property[1])
    }
}
