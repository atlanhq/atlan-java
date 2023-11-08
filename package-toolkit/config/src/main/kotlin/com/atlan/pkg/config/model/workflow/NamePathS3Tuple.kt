/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "path", "s3")
data class NamePathS3Tuple(
    @JsonIgnore val inputName: String,
    val name: String = "${inputName}_s3",
    val path: String = "/tmp/original_file/{{inputs.parameters.$inputName}}",
    val s3: Map<String, String> = mapOf("key" to "{{inputs.parameters.$inputName}}"),
) : NamedPair(name)
