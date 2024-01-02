/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.pkg.config.widgets.BooleanInput
import com.atlan.pkg.config.widgets.DateInput
import com.atlan.pkg.config.widgets.FileCopier
import com.atlan.pkg.config.widgets.FileUploader
import com.atlan.pkg.config.widgets.NumericInput
import com.atlan.pkg.config.widgets.Widget
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonPropertyOrder("name", "value")
class NestedConfig(
    val name: String,
    @JsonSerialize(using = NestedConfigSerializer::class) val value: Map<String, Widget>,
) : NamedPair(name) {
    class NestedConfigSerializer : JsonSerializer<Map<String, Widget>>() {
        override fun serialize(value: Map<String, Widget>?, gen: JsonGenerator, provider: SerializerProvider) {
            val builder: StringBuilder = StringBuilder()
            value?.let { config ->
                builder.append("{\n")
                config.forEach { (k, ui) ->
                    builder.append("\"").append(k).append("\": ")
                    when (ui) {
                        is FileUploader.FileUploaderWidget,
                        is FileCopier.FileCopierWidget,
                        -> builder.append("\"/tmp/$k/{{inputs.parameters.").append(k).append("}}\",\n")
                        is NumericInput.NumericInputWidget,
                        is DateInput.DateInputWidget,
                        is BooleanInput.BooleanInputWidget,
                        -> builder.append("{{inputs.parameters.").append(k).append("}},\n")
                        else
                        -> builder.append("{{=toJson(inputs.parameters.").append(k).append(")}},\n")
                    }
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
