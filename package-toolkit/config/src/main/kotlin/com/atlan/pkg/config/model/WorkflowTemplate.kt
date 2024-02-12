/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model

import com.atlan.pkg.config.model.workflow.NameValuePair
import com.atlan.pkg.config.model.workflow.WorkflowDag
import com.atlan.pkg.config.model.workflow.WorkflowTemplateDefinition
import com.atlan.pkg.config.widgets.FileUploader
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
        // If the provided template has any file uploads defined in it, then we need
        // to create a first template in the DAG that will copy the files from blob storage
        // into Argo's S3
        val fileMoves = mutableListOf<WorkflowDag.TaskDefinition>()
        template.inputs.config.properties.forEach { (k, u) ->
            if (u.ui is FileUploader.FileUploaderWidget) {
                fileMoves.add(
                    WorkflowDag.TaskDefinition(
                        name = "move-${getSafeTaskName(k)}",
                        templateRef = WorkflowDag.TemplateRef(
                            name = "atlan-workflow-helpers",
                            template = "move-artifact-to-s3",
                        ),
                        condition = "'{{inputs.parameters.cloud_provider}}' == 'azure' && {{inputs.parameters.is_azure_artifacts}} == false && '{{inputs.parameters.${k}_id}}' != ''",
                        arguments = WorkflowDag.Arguments(
                            parameters = listOf(
                                NameValuePair("file-id", "{{inputs.parameters.${k}_id}}"),
                                NameValuePair("s3-file-key", "{{inputs.parameters.${k}_key}}"),
                            ),
                        ),
                    ),
                )
            }
        }
        spec = if (fileMoves.isNotEmpty()) {
            val tasks = fileMoves.toMutableList()
            val passthrough = mutableListOf(
                NameValuePair("output_prefix", "{{inputs.parameters.output_prefix}}"),
            )
            template.inputs.config.properties.forEach { (k, u) ->
                when (u.ui) {
                    is FileUploader.FileUploaderWidget -> passthrough.add(NameValuePair(k, "{{inputs.parameters.${k}_key}}"))
                    else -> passthrough.add(NameValuePair(k, "{{inputs.parameters.$k}}"))
                }
            }
            /*template.inputs.parameters.forEach {
                passthrough.add(NameValuePair(it.name, "{{inputs.parameters.${it.name}}}"))
            }*/
            tasks.add(
                WorkflowDag.TaskDefinition(
                    name = "process",
                    template = "process",
                    depends = fileMoves.joinToString(" && ") { it.name },
                    arguments = WorkflowDag.Arguments(passthrough),
                ),
            )
            // Remove the input artifacts from this setup step
            val setup = WorkflowTemplateDefinition(
                config = template.config,
                dag = WorkflowDag(tasks),
                name = "main",
                pkgName = template.pkgName,
                fileInputSetup = true,
            )
            template.name = "process"
            mapOf(
                "templates" to listOf(
                    setup,
                    template,
                ),
            )
        } else {
            mapOf(
                "templates" to listOf(
                    template,
                ),
            )
        }
    }

    private fun getSafeTaskName(name: String): String {
        return name.replace('_', '-')
    }
}
