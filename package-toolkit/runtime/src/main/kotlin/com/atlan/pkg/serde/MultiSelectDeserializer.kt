/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Utility class to deserialize the results of a multi-select widget, since it can return
 * either a plain string or a JSON array (string-encoded) depending on how many elements are
 * actually selected in the UI.
 */
class MultiSelectDeserializer : StdDeserializer<List<String>>(
    TypeFactory.defaultInstance().constructCollectionType(List::class.java, String::class.java),
) {
    override fun deserialize(
        p: JsonParser?,
        ctxt: DeserializationContext?,
    ): List<String> {
        val root = p?.codec?.readTree<JsonNode>(p)
        if (root != null && !root.isNull && root.isTextual) {
            return deserialize(root.textValue())
        }
        return listOf()
    }

    companion object {
        // Creates a single static mapper to use across calls, since we will need one anyway
        // for any direct calls for deserialization
        private val mapper = jacksonObjectMapper()
        fun deserialize(value: String?): List<String> {
            if (!value.isNullOrEmpty()) {
                return if (value.startsWith("[")) {
                    return mapper.readValue<List<String>>(value)
                } else {
                    listOf(value)
                }
            }
            return listOf()
        }
    }
}
