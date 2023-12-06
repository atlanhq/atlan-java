/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model

import com.atlan.pkg.CustomPackage.Companion.pp
import com.atlan.pkg.config.model.ui.UIConfig
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Root class through which to define the configuration for the user interface of a custom package.
 * This is done through a ConfigMap, within which the core piece is the definition of the UIConfig.
 */
@JsonPropertyOrder("apiVersion", "kind", "metadata", "data")
class ConfigMap(private val name: String, config: UIConfig) {
    val apiVersion = "v1"
    val kind = "ConfigMap"
    val metadata = mapOf("name" to name)
    val data: Map<String, String>
    init {
        data = mapOf("config" to mapper.writer(pp).writeValueAsString(config))
    }
    companion object {
        private val mapper = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}
