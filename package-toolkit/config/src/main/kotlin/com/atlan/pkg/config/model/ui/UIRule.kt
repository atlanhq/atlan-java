/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.ui

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Configure basic UI rules that when the specified inputs have specified values, certain
 * other fields become required.
 *
 * @param whenInputs mapping from input ID to value for the step
 * @param required list of input IDs that should become required when the inputs all match
 */
@JsonPropertyOrder("properties", "required")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class UIRule(
    @JsonIgnore val whenInputs: Map<String, String>,
    val required: List<String>,
) {
    val properties: Map<String, Map<String, String>>
    init {
        val builder = mutableMapOf<String, Map<String, String>>()
        whenInputs.forEach {
            builder[it.key] = mapOf("const" to it.value)
        }
        properties = builder.toMap()
    }
}
