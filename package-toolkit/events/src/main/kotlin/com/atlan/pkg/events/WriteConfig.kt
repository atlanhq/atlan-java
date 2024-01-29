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
        EventUtils.setLogging()
        val logger = KotlinLogging.logger {}

        val nestedConfig = Utils.getEnvVar("NESTED_CONFIG", "")
        logger.info { "Saving main configuration to /tmp/config.json: $nestedConfig" }
        File("/tmp", "config.json").writeText(nestedConfig)

        val runtimeConfig = Utils.buildRuntimeConfig()
        logger.info { "Saving runtime configuration to /tmp/runtime.json: $runtimeConfig" }
        File("/tmp", "runtime.json").writeText(runtimeConfig)

        logger.info {
            "The ${Utils.getEnvVar("X_ATLAN_AGENT_PACKAGE_NAME", "")} pipeline will now begin running in the background at all times, using this configuration."
        }
    }
}
