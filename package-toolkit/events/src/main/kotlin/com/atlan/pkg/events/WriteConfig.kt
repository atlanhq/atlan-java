/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.events

import com.atlan.pkg.Utils
import mu.KotlinLogging
import java.io.File

/**
 * Utility to write out configuration files received as workflow input, so that they can
 * be synced across to S3 and made available to the container that will run pipeline processing
 * (to feed configuration into that container).
 * Note: all configuration is received through environment variables.
 */
object WriteConfig {

    @JvmStatic
    fun main(args: Array<String>) {
        val logger = KotlinLogging.logger {}

        val nestedConfig = Utils.getEnvVar("NESTED_CONFIG", "")
        logger.info("Saving main configuration to /tmp/config.json: {}", nestedConfig)
        File("/tmp", "config.json").writeText(nestedConfig)

        val runtimeConfig = buildRuntimeConfig()
        logger.info("Saving runtime configuration to /tmp/runtime.json: {}", runtimeConfig)
        File("/tmp", "runtime.json").writeText(runtimeConfig)

        logger.info(
            "The {} pipeline will now begin running in the background at all times, using this configuration.",
            Utils.getEnvVar("X_ATLAN_AGENT_PACKAGE_NAME", ""),
        )
    }

    /**
     * Construct a JSON representation of the runtime configuration of the workflow, drawn from
     * a standard set of environment variables about the workflow.
     */
    fun buildRuntimeConfig(): String {
        val userId = Utils.getEnvVar("ATLAN_USER_ID", "")
        val agent = Utils.getEnvVar("X_ATLAN_AGENT", "")
        val agentId = Utils.getEnvVar("X_ATLAN_AGENT_ID", "")
        val agentPkg = Utils.getEnvVar("X_ATLAN_AGENT_PACKAGE_NAME", "")
        val agentWfl = Utils.getEnvVar("X_ATLAN_AGENT_WORKFLOW_ID", "")
        return """
    {
        "user-id": "$userId",
        "x-atlan-agent": "$agent",
        "x-atlan-agent-id": "$agentId",
        "x-atlan-agent-package-name": "$agentPkg",
        "x-atlan-agent-workflow-id": "$agentWfl"
    }
        """.trimIndent()
    }
}
