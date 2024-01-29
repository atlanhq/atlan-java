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
 * @param logicClass the class to run when the pipeline executes (if this is supplied, logicCommand will be built for you)
 * @param logicCommand command to run the pipeline's custom processing logic, as a list rather than string
 * @param containerImagePullPolicy (optional) override the default IfNotPresent policy
 * @param filter sprig expression used to filter the messages that will be processed by the pipeline
 * @param minMemory minimum amount of memory to allocate to the custom logic
 * @param maxMemory maximum amount of memory to allocate to the custom logic
 */
class PipelineSpec(
    private val name: String,
    private val containerImage: String,
    private val logicClass: Class<*>? = null,
    private val logicCommand: List<String> = listOf(),
    private val containerImagePullPolicy: String = "IfNotPresent",
    private val filter: String = "",
    private val minMemory: Int = 128,
    private val maxMemory: Int = 256,
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
    }

    private val command: List<String>
    private val args: List<String>
    init {
        val cmdList = if (logicClass != null) {
            listOf("/dumb-init", "--", "java", "-Xms${minMemory}m", "-Xmx${maxMemory}m", logicClass.canonicalName)
        } else {
            logicCommand
        }
        command = listOf(cmdList[0])
        args = cmdList.subList(1, cmdList.size)
    }

    val templates = mapOf(
        "daemon" to SETTINGS,
    )

    val vertices = listOf(
        // Initial source
        source("atlas-source", "ATLAS_ENTITIES", "nf-$name", 2),
        // User-defined processing
        udf("process", 5),
        // Retry of user-defined processing
        retry(),
        // Sink messages for DLQ
        sink("dlq", "nf-$name-dlq"),
    )

    // Define the routing of events across the vertices
    val edges = listOf(
        Edge("atlas-source", "process"),
        Edge("process", "retry", RoutingCondition("retry")),
        Edge("retry", "process"),
        Edge("process", "dlq", RoutingCondition("dlq")),
    )

    val lifecycle = mapOf(
        "deleteGracePeriodSeconds" to 30,
        "desiredPhase" to "Running",
        "pauseGracePeriodSeconds" to 30,
    )

    val limits = mapOf(
        "readBatchSize" to 500,
        "bufferMaxLength" to 30000,
        "bufferUSageLimit" to 80,
        "readTimeout" to "1s",
    )

    val watermark = mapOf(
        "maxDelay" to "0s",
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
        map["scale"] = Scale(max = maxScale)
        return map
    }

    private fun udf(udfName: String, maxScale: Int): Map<String, Any> {
        val map = SETTINGS.toMutableMap()
        map["name"] = udfName
        map["udf"] = mapOf(
            "container" to mapOf(
                "image" to containerImage,
                "command" to command,
                "args" to args,
                "env" to listOf(
                    NameValuePair("ATLAN_BASE_URL", "INTERNAL"),
                    NameValuePair("CONFIG_PREFIX", name),
                    NamedSecret("CLIENT_ID", "argo-client-creds", "login"),
                    NamedSecret("CLIENT_SECRET", "argo-client-creds", "password"),
                    ConfigMapEntry("AWS_S3_BUCKET_NAME", "atlan-defaults", "bucket"),
                    ConfigMapEntry("AWS_S3_REGION", "atlan-defaults", "region"),
                    NamedSecret("SMTP_HOST", "support-smtp-creds", "host"),
                    NamedSecret("SMTP_PORT", "support-smtp-creds", "port"),
                    NamedSecret("SMTP_FROM", "support-smtp-creds", "from"),
                    NamedSecret("SMTP_USER", "support-smtp-creds", "login"),
                    NamedSecret("SMTP_PASS", "workflow-parameter-store", "smtp_password"),
                    ConfigMapEntry("DOMAIN", "atlan-defaults", "domain"),
                ),
            ),
        )
        map["scale"] = Scale(max = maxScale)
        return map
    }

    private fun retry(): Map<String, Any> {
        val map = SETTINGS.toMutableMap()
        map["name"] = "retry"
        map["udf"] = mapOf(
            "builtin" to mapOf("name" to "cat"),
        )
        // TODO: replace this entire UDF with a reusable retry container
        //  that embeds exponential back-off + jitter based on a retry counter
        map["scale"] = Scale(min = 0, max = 1, zeroReplicaSleepSeconds = 5)
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
        val lookbackSeconds: Int = 120,
        val scaleUpCooldownSeconds: Int = 90,
        val scaleDownCooldownSeconds: Int = 90,
        val zeroReplicaSleepSeconds: Int = 120,
        val targetProcessingSeconds: Int = 20,
        val targetBufferAvailability: Int = 50,
        val replicasPerScale: Int = 2,
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
