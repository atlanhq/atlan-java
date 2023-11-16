/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.pkg.CustomPipeline
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "path", "archive", "s3", "artifactGC")
data class S3Artifact(
    val name: String,
    val path: String = "/tmp/$name.json",
    val archive: Map<String, Map<String, String>> = mapOf("none" to mapOf()),
    val s3: Map<String, String> = mapOf("key" to "{{inputs.parameters.${CustomPipeline.S3_CONFIG_PREFIX}}}/$name.json"),
    val artifactGC: Map<String, String> = mapOf("strategy" to "OnWorkflowDeletion"),
) : NamedPair(name)
