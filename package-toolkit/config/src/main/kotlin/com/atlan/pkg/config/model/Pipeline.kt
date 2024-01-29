/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model

import com.atlan.Atlan
import com.atlan.pkg.config.model.pipeline.PipelineSpec
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Root class through which to define a Numaflow pipeline to use as always-on,
 * event-driven processing logic.
 *
 * @param packageId unique identifier for the pipeline, including its namespace
 * @param name dash-separated name of the pipeline
 * @param containerImage container image to run the logic of the custom pipeline
 * @param logicClass the class to run when the pipeline executes (if this is supplied, logicCommand will be built for you)
 * @param containerCommand command to run the pipeline's custom processing logic, as a list rather than string
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param description of the pipeline
 * @param filter sprig expression used to filter the messages that will be processed by the pipeline
 * @param minMemory minimum amount of memory to allocate to the custom logic
 * @param maxMemory maximum amount of memory to allocate to the custom logic
 */
@JsonPropertyOrder("apiVersion", "kind", "metadata", "spec")
class Pipeline(
    private val packageId: String,
    private val name: String,
    private val containerImage: String,
    private val logicClass: Class<*>? = null,
    private val containerCommand: List<String> = listOf(),
    private val containerImagePullPolicy: String = "IfNotPresent",
    private val description: String = "",
    private val filter: String = "",
    private val minMemory: Int = 128,
    private val maxMemory: Int = 256,
) {
    val apiVersion = "numaflow.numaproj.io/v1alpha1"
    val kind = "Pipeline"
    val metadata: Map<String, Any> = mapOf(
        "name" to name,
        "annotations" to mapOf(
            "numaflow.numaproj.io/description" to description,
            "numaflow.numaproj.io/owner" to "atlanhq",
            "numaflow.numaproj.io/pipeline-name" to name,
            "numaflow.numaproj.io/test" to "true",
            "package.argoproj.io/author" to "\"Atlan CSA\"",
            "package.argoproj.io/description" to description,
            "package.argoproj.io/homepage" to "",
            "package.argoproj.io/name" to packageId,
            "package.argoproj.io/parent" to ".",
            "package.argoproj.io/registry" to "local",
            "package.argoproj.io/support" to "\"\"",
        ),
        "finalizers" to listOf("pipeline-controller"),
        "labels" to mapOf(
            "numaflow.numaproj.io/component" to "pipeline",
            "numaflow.numaproj.io/created-by" to "controller-manager",
            "numaflow.numaproj.io/name" to name,
            "numaflow.numaproj.io/part-of" to "numaflow",
            "package.argoproj.io/installer" to "argopm",
            "package.argoproj.io/name" to "a-t-rcsas-l-a-s-h$name",
            "package.argoproj.io/parent" to "\"\"",
            "package.argoproj.io/registry" to "local",
            "package.argoproj.io/version" to Atlan.VERSION,
        ),
    )
    val spec = PipelineSpec(
        name,
        containerImage,
        logicClass,
        containerCommand,
        containerImagePullPolicy,
        filter,
        minMemory,
        maxMemory,
    )
}
