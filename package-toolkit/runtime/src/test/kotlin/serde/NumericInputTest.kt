/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.testng.Assert.assertNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class NumericInputTest {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class TestClass(
        val test: Number?,
    )

    companion object {
        private const val EMPTY = """
            {}
        """
        private const val NULL = """
            {
                "test": null
            }
        """
        private const val INVALID = """
            {
                "test": "ONE"
            }
        """
        private const val VALID_INT = """
            {
                "test": 123
            }
        """
        private const val VALID_LONG = """
            {
                "test": 1234567890123456789
            }
        """
        private const val VALID_FLOAT = """
            {
                "test": 123.456
            }
        """
        private val MAPPER = jacksonObjectMapper()
    }

    @Test
    fun testEmpty() {
        val result = MAPPER.readValue<TestClass>(EMPTY)
        assertNull(result.test)
    }

    @Test
    fun testNull() {
        val result = MAPPER.readValue<TestClass>(NULL)
        assertNull(result.test)
    }

    @Test
    fun testInvalid() {
        assertFailsWith(
            exceptionClass = JsonMappingException::class,
            block = {
                MAPPER.readValue<TestClass>(INVALID)
            },
        )
    }

    @Test
    fun testValidInt() {
        val result = MAPPER.readValue<TestClass>(VALID_INT)
        assertNotNull(result.test)
        assertEquals(123, result.test)
    }

    @Test
    fun testValidLong() {
        val result = MAPPER.readValue<TestClass>(VALID_LONG)
        assertNotNull(result.test)
        assertEquals(1234567890123456789, result.test)
    }

    @Test
    fun testValidFloat() {
        val result = MAPPER.readValue<TestClass>(VALID_FLOAT)
        assertNotNull(result.test)
        assertEquals(123.456, result.test)
    }
}
