/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.pkg.config.model.ui.UIConfig
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "inputs", "outputs", "container")
class WorkflowTemplateDefinition(
    @JsonIgnore val config: UIConfig,
    val container: WorkflowContainer,
    @JsonInclude(JsonInclude.Include.NON_NULL) val outputs: WorkflowOutputs? = null,
) {
    val name = "main"
    val inputs = WorkflowInputs(config)
}
