/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonIgnore

class WorkflowOutputs(
    @JsonIgnore val files: Map<String, String>,
) {
    val artifacts: List<NamePathPair>
    init {
        val builder = mutableListOf<NamePathPair>()
        files.forEach {
            builder.add(NamePathPair(it.key, it.value))
        }
        artifacts = builder.toList()
    }
}
