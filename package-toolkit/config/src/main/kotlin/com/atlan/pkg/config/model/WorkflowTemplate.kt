/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model

import com.atlan.pkg.config.model.workflow.WorkflowTemplateDefinition
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Root class through which to define the inputs, outputs and container image to use
 * to execute any schedule-able custom package batch-processing logic.
 */
@JsonPropertyOrder("apiVersion", "kind", "metadata", "spec")
class WorkflowTemplate(private val name: String, private val template: WorkflowTemplateDefinition) {
    val apiVersion = "argoproj.io/v1alpha1"
    val kind = "WorkflowTemplate"
    val metadata = mapOf("name" to name)
    val spec: Map<String, List<WorkflowTemplateDefinition>>
    init {
        spec = mapOf("templates" to listOf(template))
    }
}
