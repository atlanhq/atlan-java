/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.ui

import com.atlan.pkg.config.widgets.UIElement
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import mu.KotlinLogging

/**
 * Class through which to define the user interface inputs for a custom package.
 * The only required setup is the list of steps (that appear along the left the setup wizard),
 * and the individual input entries within each of those steps.
 * You can also optionally provide a set of rules for how different input entries should appear
 * based on other input entries that have been selected.
 */
@JsonPropertyOrder("properties", "anyOf", "steps")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class UIConfig(
    val steps: List<UIStep>,
    @JsonProperty("anyOf") val rules: List<Any> = listOf(),
) {
    val properties: Map<String, UIElement>
    init {
        // Build the complete set of steps from those defined across all steps
        val builder = mutableMapOf<String, UIElement>()
        steps.forEach {
                s ->
            s.inputs.forEach { i ->
                if (builder.put(i.key, i.value) != null) {
                    logger.warn("Duplicate key found across steps: {}", i.key)
                }
            }
        }
        properties = builder.toMap()
    }
    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
