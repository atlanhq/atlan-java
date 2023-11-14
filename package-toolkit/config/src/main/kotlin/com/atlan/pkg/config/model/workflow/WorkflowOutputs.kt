/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonIgnore

class WorkflowOutputs(
    @JsonIgnore val files: Map<String, String> = mapOf(),
    @JsonIgnore val s3Objects: List<S3Artifact> = listOf(),
) {
    val artifacts: List<NamedPair>
    init {
        val builder = mutableListOf<NamedPair>()
        if (s3Objects.isNotEmpty()) {
            builder.addAll(s3Objects)
        }
        files.forEach {
            builder.add(NamePathPair(it.key, it.value))
        }
        artifacts = builder.toList()
    }
}
