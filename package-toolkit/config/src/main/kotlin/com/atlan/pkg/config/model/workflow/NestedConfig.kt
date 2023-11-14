/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonPropertyOrder("name", "value")
class NestedConfig(
    val name: String,
    @JsonSerialize(using = NestedConfigSerializer::class) val value: List<String>,
) : NamedPair(name) {
    class NestedConfigSerializer : JsonSerializer<List<String>>() {
        override fun serialize(value: List<String>?, gen: JsonGenerator, provider: SerializerProvider) {
            val builder: StringBuilder = StringBuilder()
            value?.let { config ->
                builder.append("{\n")
                config.forEach { k ->
                    builder.append("\"").append(k).append("\": {{=toJson(inputs.parameters.").append(k).append(")}},\n")
                }
                if (config.isNotEmpty()) {
                    // Remove the final comma, if there is one (leaving the newline)
                    builder.deleteCharAt(builder.length - 2)
                }
                builder.append("}")
                gen.writeString(builder.toString())
            } ?: gen.writeNull()
        }
    }
}
