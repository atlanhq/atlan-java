/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "valueFrom")
class NamedSecret(
    val name: String,
    @JsonIgnore val secretName: String,
    @JsonIgnore val secretKey: String,
) : NamedPair(name) {
    val valueFrom = mapOf("secretKeyRef" to mapOf("name" to secretName, "key" to secretKey))
}
