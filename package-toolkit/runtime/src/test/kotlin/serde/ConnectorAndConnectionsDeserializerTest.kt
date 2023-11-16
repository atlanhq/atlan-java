/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.model.ConnectorAndConnections
import com.atlan.pkg.serde.WidgetSerde
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

class ConnectorAndConnectionsDeserializerTest {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    data class TestClass(
        @JsonDeserialize(using = WidgetSerde.ConnectorAndConnectionsDeserializer::class)
        val connectorType: ConnectorAndConnections?,
    )

    companion object {
        private const val EMPTY = """
            {}
        """
        private const val NULL = """
            {
                "connectorType": null
            }
        """
        private const val INVALID = """
            {
                "connectorType": "ONE"
            }
        """
        private const val VALID = """
            {
                "connectorType": "{\"source\":\"snowflake\",\"connections\":[\"default/snowflake/1700058127\",\"default/snowflake/1699877258\",\"default/snowflake/1699682048\"]}"
            }
        """
        private val MAPPER = jacksonObjectMapper()
    }

    @Test
    fun testEmpty() {
        val result = MAPPER.readValue<TestClass>(EMPTY)
        assertNull(result.connectorType)
    }

    @Test
    fun testNull() {
        val result = MAPPER.readValue<TestClass>(NULL)
        assertNull(result.connectorType)
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
        assertNotNull(result.connectorType)
        assertEquals(AtlanConnectorType.SNOWFLAKE, result.connectorType.source)
        assertNotNull(result.connectorType.connections)
        assertEquals(3, result.connectorType.connections.size)
        assertEquals("default/snowflake/1700058127", result.connectorType.connections[0])
    }
}
