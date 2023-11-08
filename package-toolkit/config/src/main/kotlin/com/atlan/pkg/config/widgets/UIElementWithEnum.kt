/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.widgets

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("type", "required", "enum", "enumNames", "default", "ui")
abstract class UIElementWithEnum(
    type: String,
    required: Boolean,
    @JsonIgnore val possibleValues: Map<String, String>,
    val default: String? = null,
    ui: Widget,
) : UIElement(
    type,
    required,
    ui,
) {
    val enum: List<String>
    val enumNames: List<String>
    init {
        val keys = mutableListOf<String>()
        val values = mutableListOf<String>()
        possibleValues.forEach { t, u ->
            keys.add(t)
            values.add(u)
        }
        enum = keys.toList()
        enumNames = values.toList()
    }
}
