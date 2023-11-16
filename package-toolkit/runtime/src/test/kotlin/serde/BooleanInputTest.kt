/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.testng.Assert.assertFalse
import org.testng.Assert.assertNull
import org.testng.Assert.assertTrue
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class BooleanInputTest {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class TestClass(
        val test: Boolean?,
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
        private const val VALID_TRUE = """
            {
                "test": true
            }
        """
        private const val VALID_FALSE = """
            {
                "test": false
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
    fun testValidTrue() {
        val result = MAPPER.readValue<TestClass>(VALID_TRUE)
        assertNotNull(result.test)
        assertTrue(result.test)
    }

    @Test
    fun testValidFalse() {
        val result = MAPPER.readValue<TestClass>(VALID_FALSE)
        assertNotNull(result.test)
        assertFalse(result.test)
    }
}
