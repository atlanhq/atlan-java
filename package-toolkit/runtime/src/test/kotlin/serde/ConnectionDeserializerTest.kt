/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.serde.ConnectionDeserializer
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ConnectionDeserializerTest {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class TestClass(
        @JsonDeserialize(using = ConnectionDeserializer::class)
        val connection: Connection?,
    )

    companion object {
        private const val EMPTY = """
            {}
        """
        private const val NULL = """
            {
                "connection": null
            }
        """
        private const val INVALID = """
            {
                "connection": "ONE"
            }
        """
        private const val VALID = """
            {
                "connection": "{\"typeName\": \"Connection\", \"attributes\": {\"name\": \"Test123\", \"connectorName\": \"snowflake\", \"qualifiedName\": \"default/snowflake/1234567890\"}}"
            }
        """
        private val MAPPER = jacksonObjectMapper()
    }

    @Test
    fun testEmpty() {
        val result = MAPPER.readValue<TestClass>(EMPTY)
        assertNull(result.connection)
    }

    @Test
    fun testNull() {
        val result = MAPPER.readValue<TestClass>(NULL)
        assertNull(result.connection)
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
    fun testValid() {
        val result = MAPPER.readValue<TestClass>(VALID)
        assertNotNull(result.connection)
        assertEquals(result.connection.name, "Test123")
        assertEquals(result.connection.connectorType, AtlanConnectorType.SNOWFLAKE)
    }
}
