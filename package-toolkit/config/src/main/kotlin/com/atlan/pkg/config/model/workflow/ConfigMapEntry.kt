/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "valueFrom")
class ConfigMapEntry(
    val name: String,
    @JsonIgnore val configMapName: String,
    @JsonIgnore val configMapKey: String,
) : NamedPair(name) {
    val valueFrom = mapOf("configMapKeyRef" to mapOf("name" to configMapName, "key" to configMapKey))
}
