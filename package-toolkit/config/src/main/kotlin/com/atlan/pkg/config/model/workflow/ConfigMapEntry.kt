/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "valueFrom")
class ConfigMapEntry(
    name: String,
    @JsonIgnore val configMapName: String,
    @JsonIgnore val configMapKey: String,
    @JsonIgnore val default: String? = null,
) : NamedPair(name) {
    val valueFrom = if (default != null) {
        mapOf(
            "default" to default,
            "configMapKeyRef" to mapOf(
                "name" to configMapName, "key" to configMapKey,
            ),
        )
    } else {
        mapOf(
            "configMapKeyRef" to mapOf(
                "name" to configMapName, "key" to configMapKey,
            ),
        )
    }
}
