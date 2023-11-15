/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model

import com.atlan.pkg.config.model.pipeline.PipelineSpec
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Root class through which to define a Numaflow pipeline to use as always-on,
 * event-driven processing logic.
 *
 * @param name dash-separated name of the pipeline
 * @param containerImage container image to run the logic of the custom pipeline
 * @param containerCommand command to run the pipeline's custom processing logic, as a list rather than string
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param description of the pipeline
 * @param filter sprig expression used to filter the messages that will be processed by the pipeline
 */
@JsonPropertyOrder("apiVersion", "kind", "metadata", "spec")
class Pipeline(
    private val name: String,
    private val containerImage: String,
    private val containerCommand: List<String>,
    private val containerImagePullPolicy: String = "IfNotPresent",
    private val description: String = "",
    private val filter: String = "",
) {
    val apiVersion = "numaflow.numaproj.io/v1alpha1"
    val kind = "Pipeline"
    val metadata: Map<String, Any> = mapOf(
        "name" to name,
        "labels" to mapOf(
            "numaflow.numaproj.io/component" to "pipeline",
            "numaflow.numaproj.io/name" to name,
            "numaflow.numaproj.io/part-of" to "numaflow",
            "numaflow.numaproj.io/created-by" to "controller-manager",
        ),
        "annotations" to mapOf(
            "numaflow.numaproj.io/pipeline-name" to name,
            "numaflow.numaproj.io/description" to description,
            "numaflow.numaproj.io/owner" to "atlanhq",
            "numaflow.numaproj.io/test" to "true",
        ),
    )
    val spec = PipelineSpec(name, containerImage, containerCommand, containerImagePullPolicy, filter)
}
