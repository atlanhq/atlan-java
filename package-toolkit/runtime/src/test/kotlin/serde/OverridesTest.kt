/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.test.Test
import kotlin.test.assertEquals

class OverridesTest {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class TestClass(
        val config: String = "default",
        val test: String? = "default-value",
    ) : CustomConfig<TestClass>()

    companion object {
        private const val EMPTY = """
            {}
        """
        private const val NULL = """
            {
                "config": null,
                "test": "ignored-value"
            }
        """
        private const val OVERRIDE = """
            {
                "config": "advanced",
                "test": "new-value"
            }
        """
        private const val DEFAULT = """
            {
                "config": "default",
                "test": "new-value-ignored"
            }
        """
        private val MAPPER = jacksonObjectMapper()
    }

    @Test
    fun testEmpty() {
        val result = MAPPER.readValue<TestClass>(EMPTY)
        assertEquals("default-value", result.getEffectiveValue(TestClass::test, TestClass::config))
    }

    @Test
    fun testNull() {
        val result = MAPPER.readValue<TestClass>(EMPTY)
        assertEquals("default-value", result.getEffectiveValue(TestClass::test, TestClass::config))
    }

    @Test
    fun testOverride() {
        val result = MAPPER.readValue<TestClass>(OVERRIDE)
        assertEquals("new-value", result.getEffectiveValue(TestClass::test, TestClass::config))
    }

    @Test
    fun testDefault() {
        val result = MAPPER.readValue<TestClass>(DEFAULT)
        assertEquals("default-value", result.getEffectiveValue(TestClass::test, TestClass::config))
    }
}
