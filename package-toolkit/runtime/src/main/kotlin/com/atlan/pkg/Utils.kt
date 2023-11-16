/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Connection
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KLogger
import mu.KotlinLogging
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.round
import kotlin.system.exitProcess

/**
 * Common utilities for using the Atlan SDK within Kotlin.
 */
object Utils {
    val logger = KotlinLogging.logger {}
    val MAPPER = jacksonObjectMapper()

    /**
     * Set up the event-processing options, and start up the event processor.
     *
     * @return the configuration used to set up the event-processing handler, or null if no configuration was found
     */
    inline fun <reified T : CustomConfig> setPackageOps(): T {
        logger.info("Looking for configuration in environment variables...")
        val config = parseConfigFromEnv<T>()
        setClient(config.runtime.userId ?: "")
        setWorkflowOpts(config.runtime)
        return config
    }

    /**
     * Set up the default Atlan client, based on environment variables.
     * This will use an API token if found in ATLAN_API_KEY, and will fallback to attempting
     * to impersonate a user if ATLAN_API_KEY is empty.
     *
     * @param impersonateUserId unique identifier (GUID) of a user or API token to impersonate
     */
    fun setClient(impersonateUserId: String = "") {
        val baseUrl = getEnvVar("ATLAN_BASE_URL", "INTERNAL")
        val apiToken = getEnvVar("ATLAN_API_KEY", "")
        val userId = getEnvVar("ATLAN_USER_ID", impersonateUserId)
        Atlan.setBaseUrl(baseUrl)
        val tokenToUse =
            when {
                apiToken.isNotEmpty() -> {
                    logger.info("Using provided API token for authentication.")
                    apiToken
                }
                userId.isNotEmpty() -> {
                    logger.info("No API token found, attempting to impersonate user: {}", userId)
                    Atlan.getDefaultClient().impersonate.user(userId)
                }
                else -> {
                    logger.info("No API token or impersonation user, attempting short-lived escalation.")
                    Atlan.getDefaultClient().impersonate.escalate()
                }
            }
        Atlan.setApiToken(tokenToUse)
    }

    /**
     * Retrieves the value of an environment variable (if found), or if not found or empty
     * gives the specified default value instead.
     *
     * @param name of the environment variable
     * @param default value to give the variable, if it is not found or empty
     * @return the value (or default) of the environment variable
     */
    fun getEnvVar(
        name: String,
        default: String = "",
    ): String {
        val candidate = System.getenv(name)
        return if (candidate != null && candidate.isNotEmpty()) candidate else default
    }

    /**
     * Increments the provided counter by 1 and logs the progress of the job.
     * Note: if batchSize is provided, will only log progress in increments of the batchSize.
     *
     * @param counter an atomic counter indicating how many things have been processed
     * @param total total number of things to be done
     * @param logger through which to report the overall progress
     * @param batchSize number of things that are done per batch of operations
     */
    fun logProgress(
        counter: AtomicLong,
        total: Long,
        logger: KLogger,
        batchSize: Int = -1,
    ) {
        val localCount = counter.incrementAndGet()
        if (batchSize > 0) {
            if (localCount.mod(batchSize) == 0 || localCount == total) {
                logger.info(
                    " ... processed {}/{} ({}%)",
                    localCount,
                    total,
                    round((localCount.toDouble() / total) * 100),
                )
            }
        } else {
            logger.info(
                " ... processed {}/{} ({}%)",
                localCount,
                total,
                round((localCount.toDouble() / total) * 100),
            )
        }
    }

    /**
     * Check if the utility is being run through a workflow, and if it is set up the various
     * workflow headers from the relevant environment variables against the default client.
     *
     * @param config parameters received through means other than environment variables to use as a fallback
     */
    fun setWorkflowOpts(config: RuntimeConfig? = null) {
        val atlanAgent = getEnvVar("X_ATLAN_AGENT", config?.agent ?: "")
        if (atlanAgent == "workflow") {
            val headers = Atlan.getDefaultClient().extraHeaders
            headers["x-atlan-agent"] = listOf("workflow")
            headers["x-atlan-agent-package-name"] = listOf(getEnvVar("X_ATLAN_AGENT_PACKAGE_NAME", config?.agentPackageName ?: ""))
            headers["x-atlan-agent-workflow-id"] = listOf(getEnvVar("X_ATLAN_AGENT_WORKFLOW_ID", config?.agentWorkflowId ?: ""))
            headers["x-atlan-agent-id"] = listOf(getEnvVar("X_ATLAN_AGENT_ID", config?.agentId ?: ""))
            Atlan.getDefaultClient().extraHeaders = headers
        }
    }

    /**
     * Translates environment variables into a map of settings.
     * Sets defaults for:
     * - DELIMITER = |
     * - BATCH_SIZE = 50
     *
     * @return a map of settings drawn from environment variables
     */
    fun environmentVariables(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        System.getenv().forEach { (k, v) ->
            if (!v.isNullOrBlank()) {
                map[k] = v
            }
        }
        if (!map.containsKey("DELIMITER")) {
            map["DELIMITER"] = "|"
        }
        if (!map.containsKey("BATCH_SIZE")) {
            map["BATCH_SIZE"] = "50"
        }
        return map
    }

    /**
     * Parse configuration from environment variables.
     *
     * @return the complete configuration for the custom package
     */
    inline fun <reified T : CustomConfig> parseConfigFromEnv(): T {
        logger.info("Constructing configuration from environment variables...")
        val runtime = buildRuntimeConfig()
        return parseConfig(getEnvVar("NESTED_CONFIG"), runtime)
    }

    /**
     * Parse configuration from the provided input strings.
     *
     * @param config the event-processing-specific configuration
     * @param runtime the general runtime configuration from the workflow
     * @return the complete configuration for the event-handling pipeline
     */
    inline fun <reified T : CustomConfig> parseConfig(config: String, runtime: String): T {
        logger.info("Parsing configuration...")
        val type = MAPPER.typeFactory.constructType(T::class.java)
        val cfg = MAPPER.readValue<T>(config, type)
        cfg.runtime = MAPPER.readValue(runtime, RuntimeConfig::class.java)
        return cfg
    }

    /**
     * Return the provided configuration value only if it is non-null and not empty,
     * otherwise return the provided default value instead.
     *
     * @param configValue to return if there is a non-null, non-empty value
     * @param default to return if the configValue is either null or empty
     * @return the actual value or a default, if the actual is null or empty
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getOrDefault(configValue: String?, default: T): T {
        if (configValue.isNullOrEmpty()) {
            return default
        }
        return when (default) {
            // TODO: likely need to extend to other types
            is Int -> configValue.toInt() as T
            is List<*> -> getOrDefault(null, default as List<String>) as T
            else -> configValue as T
        }
    }

    /**
     * Return the provided configuration value only if it is non-null and not empty,
     * otherwise return the provided default value instead.
     *
     * @param configValue to return if there is a non-null, non-empty value
     * @param default to return if the configValue is either null or empty
     * @return the actual value or a default, if the actual is null or empty
     */
    fun getOrDefault(configValue: List<String>?, default: List<String>): List<String> {
        return if (configValue.isNullOrEmpty()) default else configValue
    }

    /**
     * Construct a JSON representation of the runtime configuration of the workflow, drawn from
     * a standard set of environment variables about the workflow.
     */
    fun buildRuntimeConfig(): String {
        val userId = getEnvVar("ATLAN_USER_ID", "")
        val agent = getEnvVar("X_ATLAN_AGENT", "")
        val agentId = getEnvVar("X_ATLAN_AGENT_ID", "")
        val agentPkg = getEnvVar("X_ATLAN_AGENT_PACKAGE_NAME", "")
        val agentWfl = getEnvVar("X_ATLAN_AGENT_WORKFLOW_ID", "")
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

    /**
     * Either reuse (top priority) or create a new connection, based on the parameters provided.
     * Note that this method will exit if:
     * - it is unable to find a connection with the specified qualifiedName (rc = 1)
     * - it is unable to even parse the specified connection details (rc = 2)
     * - it is unable to create a new connection with the specified details (rc = 3)
     *
     * @param action to take: CREATE to create a new connection, or REUSE to reuse an existing connection (default if empty)
     * @param connectionQN qualifiedName of the connection to reuse (only applies when action is REUSE)
     * @param connection connection object to use to create a new connection (only applies when action is CREATE)
     * @return the qualifiedName of the connection to use, whether reusing or creating, or an empty string if neither variable has any data in it
     */
    fun createOrReuseConnection(
        action: String?,
        connectionQN: String?,
        connection: Connection?,
    ): String {
        return if (getOrDefault(action, "REUSE") == "REUSE") {
            reuseConnection(connectionQN)
        } else {
            createConnection(connection)
        }
    }

    /**
     * Create a connection using the details provided.
     *
     * @param connection a connection object, defining the connection to be created
     * @return the qualifiedName of the connection that is created, or an empty string if no connection details were provided
     */
    fun createConnection(connection: Connection?): String {
        return if (connection != null) {
            logger.info("Attempting to create new connection...")
            try {
                val toCreate = connection
                    .toBuilder()
                    .guid("-${ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1)}")
                    .build()
                val response = toCreate.save().block()
                response.getResult(toCreate).qualifiedName
            } catch (e: AtlanException) {
                logger.error("Unable to create connection: {}", connection, e)
                exitProcess(3)
            }
        } else {
            ""
        }
    }

    /**
     * Validate the provided connection exists, and if so return its qualifiedName.
     *
     * @param providedConnectionQN qualifiedName of connection to reuse
     * @return the qualifiedName of the connection, so long as it exists, otherwise an empty string
     */
    fun reuseConnection(providedConnectionQN: String?): String {
        return providedConnectionQN?.let {
            try {
                logger.info("Attempting to reuse connection: {}", providedConnectionQN)
                Connection.get(Atlan.getDefaultClient(), providedConnectionQN, false)
                providedConnectionQN
            } catch (e: NotFoundException) {
                logger.error("Unable to find connection with the provided qualifiedName: {}", providedConnectionQN, e)
                ""
            }
        } ?: ""
    }
}
