/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.atlan.AtlanClient
import com.atlan.model.assets.Connection
import com.atlan.model.core.AtlanObject
import com.atlan.pkg.model.ConnectorAndConnections
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object WidgetSerde {
    // Creates a single static mapper to use across calls
    val mapper = jacksonObjectMapper()

    class MultiSelectDeserializer :
        StdDeserializer<List<String>>(
            TypeFactory.defaultInstance().constructCollectionType(List::class.java, String::class.java),
        ) {
        override fun deserialize(
            p: JsonParser?,
            ctxt: DeserializationContext?,
        ): List<String> {
            val root = p?.codec?.readTree<JsonNode>(p)
            if (root != null && !root.isNull && root.isTextual) {
                val value = root.textValue()
                if (!value.isNullOrEmpty()) {
                    return if (value.startsWith("[")) {
                        return mapper.readValue<List<String>>(value)
                    } else {
                        listOf(value)
                    }
                }
            }
            return listOf()
        }
    }

    class MultiSelectSerializer :
        StdSerializer<List<String>>(
            TypeFactory.defaultInstance().constructCollectionType(List::class.java, String::class.java),
        ) {
        override fun serializeWithType(
            value: List<String>?,
            gen: JsonGenerator?,
            serializers: SerializerProvider?,
            typeSer: TypeSerializer?,
        ) {
            serialize(value, gen, serializers)
        }

        override fun serialize(
            value: List<String>?,
            gen: JsonGenerator?,
            provider: SerializerProvider?,
        ) {
            StringWrapperSerializer.wrap(value, gen, provider)
        }
    }

    class ConnectorAndConnectionsDeserializer :
        StdDeserializer<ConnectorAndConnections>(
            ConnectorAndConnections::class.java,
        ) {
        override fun deserializeWithType(
            p: JsonParser?,
            ctxt: DeserializationContext?,
            typeDeserializer: TypeDeserializer?,
        ): ConnectorAndConnections? = deserialize(p, ctxt)

        override fun deserialize(
            p: JsonParser?,
            ctxt: DeserializationContext?,
        ): ConnectorAndConnections? {
            val root = p?.codec?.readTree<JsonNode>(p)
            if (root != null && !root.isNull && root.isTextual) {
                val value = root.textValue()
                if (!value.isNullOrEmpty()) {
                    return mapper.readValue<ConnectorAndConnections>(value)
                }
            }
            return null
        }
    }

    class ConnectorAndConnectionsSerializer :
        StdSerializer<ConnectorAndConnections>(
            ConnectorAndConnections::class.java,
        ) {
        override fun serializeWithType(
            value: ConnectorAndConnections?,
            gen: JsonGenerator?,
            serializers: SerializerProvider?,
            typeSer: TypeSerializer?,
        ) {
            serialize(value, gen, serializers)
        }

        override fun serialize(
            value: ConnectorAndConnections?,
            gen: JsonGenerator?,
            provider: SerializerProvider?,
        ) {
            StringWrapperSerializer.wrap(value, gen, provider)
        }
    }

    class ConnectionDeserializer :
        StdDeserializer<Connection>(
            Connection::class.java,
        ) {
        override fun deserializeWithType(
            p: JsonParser?,
            ctxt: DeserializationContext?,
            typeDeserializer: TypeDeserializer?,
        ): Connection? = deserialize(p, ctxt)

        override fun deserialize(
            p: JsonParser?,
            ctxt: DeserializationContext?,
        ): Connection? {
            val root = p?.codec?.readTree<JsonNode>(p)
            if (root != null && !root.isNull && root.isTextual) {
                val value = root.textValue()
                if (!value.isNullOrEmpty()) {
                    AtlanClient("INTERNAL", "unused").use { client ->
                        return client.readValue(value, Connection::class.java)
                    }
                }
            }
            return null
        }
    }

    class ConnectionSerializer :
        StdSerializer<Connection>(
            Connection::class.java,
        ) {
        override fun serializeWithType(
            value: Connection?,
            gen: JsonGenerator?,
            serializers: SerializerProvider?,
            typeSer: TypeSerializer?,
        ) {
            serialize(value, gen, serializers)
        }

        override fun serialize(
            value: Connection?,
            gen: JsonGenerator?,
            provider: SerializerProvider?,
        ) {
            StringWrapperSerializer.wrap(value, gen, provider)
        }
    }

    object StringWrapperSerializer {
        fun wrap(
            value: Any?,
            gen: JsonGenerator?,
            provider: SerializerProvider?,
        ) {
            if (value == null) {
                gen?.writeNull()
            } else {
                when (value) {
                    is AtlanObject -> {
                        AtlanClient("INTERNAL").use { client ->
                            gen?.writeString(value.toJson(client))
                        }
                    }
                    else -> gen?.writeString(mapper.writeValueAsString(value))
                }
            }
        }
    }
}
