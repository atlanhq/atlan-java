/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.events

import com.atlan.AtlanClient
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.events.config.S3ConfigSync
import io.numaproj.numaflow.mapper.Server
import mu.KotlinLogging

/**
 * Utilities for working with event-processing pipelines.
 */
object EventUtils {
    val logger = KotlinLogging.logger {}

    /**
     * Set up the event-processing options, and start up the event processor.
     *
     * @return the configuration used to set up the event-processing handler, or null if no configuration was found
     */
    inline fun <reified T : CustomConfig> setEventOps(): T? {
        logger.info("Looking for configuration in S3...")
        val config = S3ConfigSync().sync<T>()
        if (config == null) {
            logger.info("... no configuration found, will timeout pod and retry ...")
        } else {
            logger.info("Configuration found - synced to: /tmp/config.json")
            // TODO: needs to be replaced -- Utils.setClient(config.runtime.userId ?: "")
            // TODO: needs to be replaced -- Utils.setWorkflowOpts(config.runtime)
        }
        return config
    }

    /**
     * Update the configuration for the event-processing handler to run using the provided API token.
     *
     * @param client connectivity to the Atlan tenant
     * @param apiTokenId unique identifier (GUID) of the API token
     */
    fun useApiToken(
        client: AtlanClient,
        apiTokenId: String?,
    ) {
        if (apiTokenId != null) {
            val token =
                client.apiTokens.getByGuid(apiTokenId)
            if (token != null) {
                logger.info("Setting pipeline to run with token: {}", token.displayName)
                // TODO: needs to be replaced -- Utils.setClient("service-account-" + token.clientId)
            }
        }
    }

    /**
     * Start the event-processing handler.
     *
     * @param handler the event-processing handler to start running
     */
    fun startHandler(handler: AbstractNumaflowHandler) {
        Server(handler).start()
    }

    /**
     * Set up logging for the event-processing handler.
     * Note: this MUST be called before doing any other logging, or the pipeline configuration
     * will fail due to creating the debug.log in an unknown location.
     */
    fun setLogging() {
        System.getProperty("logDirectory") ?: System.setProperty("logDirectory", "tmp")
    }
}
