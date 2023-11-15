/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.pipeline

import com.atlan.pkg.config.model.workflow.ConfigMapEntry
import com.atlan.pkg.config.model.workflow.NameValuePair
import com.atlan.pkg.config.model.workflow.NamedSecret
import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Specification for a Numaflow pipeline.
 *
 * @param name dash-delimited name of the pipeline
 * @param containerImage container image to run the logic of the custom pipeline
 * @param logicCommand command to run the pipeline's custom processing logic, as a list rather than string
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param filter sprig expression used to filter the messages that will be processed by the pipeline
 */
class PipelineSpec(
    private val name: String,
    private val containerImage: String,
    private val logicCommand: List<String>,
    private val containerImagePullPolicy: String = "IfNotPresent",
    private val filter: String = "",
) {
    companion object {
        val SETTINGS = mapOf(
            "metadata" to mapOf(
                "annotations" to mapOf(
                    "cluster-autoscaler.kubernetes.io/safe-to-evict" to "true",
                ),
            ),
            "imagePullSecrets" to listOf(
                mapOf("name" to "github-docker-registry"),
            ),
            "tolerations" to listOf(
                mapOf(
                    "key" to "kubernetes.azure.com/scalesetpriority",
                    "operator" to "Equal",
                    "value" to "spot",
                    "effect" to "NoSchedule",
                ),
            ),
        )
        val DEBUG_TEMPLATE = mapOf(
            "env" to listOf(
                NameValuePair("NUMAFLOW_DEBUG", "false"),
            ),
        )
    }

    val templates = mapOf(
        "daemon" to SETTINGS,
    )

    val vertices = listOf(
        // Initial source
        source("atlas-source", "ATLAS_ENTITIES", "nf-$name", 2),
        // Retry source
        source("failed-source", "nf-$name-retry", "nf-$name-retry", 1),
        // Initial user-defined processing
        udf("process", 5),
        // Retry of user-defined processing
        udf("retry", 1),
        // Sink messages on initial failure
        sink("failed-sink", "nf-$name-retry"),
        // Sink messages for retry
        sink("retry-sink", "nf-$name-retry"),
        // Sink messages for DLQ
        sink("failed-sink-dlq", "nf-$name-dlq"),
    )

    // Define the routing of events across the vertices
    val edges = listOf(
        Edge("atlas-source", "process"),
        Edge("process", "failed-sink", RoutingCondition("failure")),
        Edge("failed-source", "retry"),
        Edge("retry", "retry-sink", RoutingCondition("retry")),
        Edge("retry", "failed-sink-dlq", RoutingCondition("dlq")),
    )

    private fun source(sourceName: String, topicName: String, consumerGroupName: String, maxScale: Int): Map<String, Any> {
        val map = SETTINGS.toMutableMap()
        map["name"] = sourceName
        val source = mutableMapOf<String, Any>()
        source["kafka"] = mapOf(
            "brokers" to listOf(
                "kafka-headless.kafka.svc.cluster.local:9092",
            ),
            "topic" to topicName,
            "consumerGroup" to consumerGroupName,
        )
        if (filter.isNotEmpty()) {
            source["transformer"] = mapOf(
                "builtin" to mapOf(
                    "name" to "filter",
                    "kwargs" to mapOf(
                        "expression" to "$filter\n",
                    ),
                ),
            )
        }
        map["source"] = source
        map["containerTemplate"] = DEBUG_TEMPLATE
        map["scale"] = Scale(max = maxScale)
        return map
    }

    private fun udf(udfName: String, maxScale: Int): Map<String, Any> {
        val map = SETTINGS.toMutableMap()
        map["name"] = udfName
        map["udf"] = mapOf(
            "container" to mapOf(
                "image" to containerImage,
                "command" to listOf(logicCommand[0]),
                "args" to logicCommand.subList(1, logicCommand.size),
                "env" to listOf(
                    NameValuePair("ATLAN_BASE_URL", "INTERNAL"),
                    NameValuePair("CONFIG_PREFIX", name),
                    NamedSecret("CLIENT_ID", "argo-client-creds", "login"),
                    NamedSecret("CLIENT_SECRET", "argo-client-creds", "password"),
                    ConfigMapEntry("AWS_S3_BUCKET_NAME", "atlan-defaults", "bucket"),
                    ConfigMapEntry("AWS_S3_REGION", "atlan-defaults", "region"),
                    mapOf(
                        "name" to "ATLAN_BASE_URL",
                        "value" to "INTERNAL",
                    ),
                ),
            ),
        )
        map["containerTemplate"] = DEBUG_TEMPLATE
        map["scale"] = Scale(max = maxScale)
        return map
    }

    private fun sink(sinkName: String, topicName: String): Map<String, Any> {
        val map = SETTINGS.toMutableMap()
        map["name"] = sinkName
        map["sink"] = mapOf(
            "kafka" to mapOf(
                "brokers" to listOf(
                    "kafka-headless.kafka.svc.cluster.local:9092",
                ),
                "topic" to topicName,
            ),
        )
        map["scale"] = Scale(max = 1)
        return map
    }

    data class Scale(
        val disabled: Boolean = false,
        val min: Int = 1,
        val max: Int = 1,
    )

    data class Edge(
        val from: String,
        val to: String,
        @JsonInclude(JsonInclude.Include.NON_NULL) val conditions: RoutingCondition? = null,
    )

    data class RoutingCondition(private val tag: String) {
        val tags: Map<String, List<String>> = mapOf(
            "values" to listOf(tag),
        )
    }
}
