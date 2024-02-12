/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("tasks")
data class WorkflowDag(
    val tasks: List<TaskDefinition>,
) {
    @JsonPropertyOrder("name", "depends", "template", "templateRef", "when", "arguments")
    data class TaskDefinition(
        val name: String,
        val arguments: Arguments,
        @JsonInclude(JsonInclude.Include.NON_NULL) val templateRef: TemplateRef? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL) val template: String? = null,
        @JsonProperty("when") @JsonInclude(JsonInclude.Include.NON_NULL) val condition: String? = null,
        @JsonInclude(JsonInclude.Include.NON_NULL) val depends: String? = null,
    )

    @JsonPropertyOrder("name", "template")
    data class TemplateRef(
        val name: String,
        val template: String,
    )

    @JsonPropertyOrder("parameters")
    data class Arguments(
        val parameters: List<NamedPair>,
    )
}
