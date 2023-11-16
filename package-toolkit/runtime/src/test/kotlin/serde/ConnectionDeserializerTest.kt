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
                "connection": "{\"attributes\":{\"name\":\"swagger3\",\"qualifiedName\":\"default/api/1700144027\",\"allowQuery\":true,\"allowQueryPreview\":true,\"rowLimit\":\"10000\",\"defaultCredentialGuid\":\"\",\"connectorName\":\"api\",\"sourceLogo\":\"http://assets.atlan.com/assets/apispec.png\",\"isDiscoverable\":true,\"isEditable\":false,\"category\":\"API\",\"adminUsers\":[\"jsmith\"],\"adminGroups\":[],\"adminRoles\":[\"db6d07eb-b3e9-493b-ad1a-b82027da37d5\"]},\"typeName\":\"Connection\"}"
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
        assertEquals("swagger3", result.connection.name)
        assertEquals(AtlanConnectorType.API, result.connection.connectorType)
    }
}
