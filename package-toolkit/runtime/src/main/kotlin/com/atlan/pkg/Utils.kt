/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.AtlanClient
import com.atlan.BuildInfo
import com.atlan.cache.OffHeapAssetCache
import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.pkg.cache.PersistentConnectionCache
import com.atlan.pkg.model.Credential
import com.atlan.pkg.objectstore.ADLSCredential
import com.atlan.pkg.objectstore.ADLSSync
import com.atlan.pkg.objectstore.GCSCredential
import com.atlan.pkg.objectstore.GCSSync
import com.atlan.pkg.objectstore.LocalSync
import com.atlan.pkg.objectstore.ObjectStorageSyncer
import com.atlan.pkg.objectstore.S3Credential
import com.atlan.pkg.objectstore.S3Sync
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.autoconfigure.ResourceConfiguration
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor
import io.opentelemetry.sdk.resources.Resource
import jakarta.activation.FileDataSource
import jakarta.mail.Message
import mu.KLogger
import mu.KotlinLogging
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import java.io.Closeable
import java.io.File
import java.io.File.separator
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Paths
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicLong
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlin.math.round
import kotlin.system.exitProcess

/**
 * Common utilities for using the Atlan SDK within Kotlin.
 */
object Utils {
    // Note: this default value is necessary to avoid internal Argo errors if the
    // file is actually optional (only value that seems likely to be in all tenants' S3 buckets)
    const val DEFAULT_FILE = "argo-artifacts/atlan-update/@atlan-packages-last-safe-run.txt"
    val ISO_8601_FORMATTER: DateTimeFormatter =
        DateTimeFormatter
            .ofPattern("yyyyMMdd-HHmmssSSS")
            .withZone(ZoneId.of("UTC"))

    class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(
            thread: Thread,
            throwable: Throwable,
        ) {
            val msg = getStackTraceAsString(throwable)
            try {
                val logger = getLogger(this.javaClass.name)
                logger.error("Uncaught exception in thread '${thread.name}': '$msg'")
            } catch (e: Exception) {
                print("Uncaught exception in thread '${thread.name}': '$msg'")
                print("An unexpected error occurred in uncaughtException: '${e.message}'")
                e.printStackTrace()
            }
        }
    }

    private fun getStackTraceAsString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        return sw.toString()
    }

    init {
        val openTelemetryEndpoint = System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT")
        val openTelemetryResourceAttributes = System.getenv("OTEL_RESOURCE_ATTRIBUTES")
        if (!openTelemetryEndpoint.isNullOrBlank() && !openTelemetryResourceAttributes.isNullOrBlank()) {
            setupOpenTelemetry(openTelemetryEndpoint)
        }
    }

    private fun appendCustomEnvResource(
        resource: Resource,
        envNames: Map<String, String>,
    ): Resource {
        var outputResource = Resource.empty().merge(resource)
        envNames.forEach { entry ->
            val envValue = System.getenv(entry.value) ?: ""
            if (envValue != "") {
                val tempResource =
                    Resource.create(
                        Attributes
                            .builder()
                            .put(
                                entry.key,
                                envValue,
                            ).build(),
                    )
                outputResource = outputResource.merge(tempResource)
            }
        }
        return outputResource
    }

    private fun setupOpenTelemetry(endpoint: String) {
        val defaultResource: Resource = ResourceConfiguration.createEnvironmentResource()
        val customResourceEnvNames =
            mapOf(
                "k8s.workflow.node.name" to "OTEL_WF_NODE_NAME",
            )
        val resource = appendCustomEnvResource(defaultResource, customResourceEnvNames)
        val logExporter: OtlpGrpcLogRecordExporter =
            OtlpGrpcLogRecordExporter
                .builder()
                .setEndpoint(endpoint)
                .build()
        val logEmitterProvider: SdkLoggerProvider =
            SdkLoggerProvider
                .builder()
                .setResource(resource)
                .addLogRecordProcessor(BatchLogRecordProcessor.builder(logExporter).build())
                .build()
        val openTelemetry =
            OpenTelemetrySdk.builder().setLoggerProvider(logEmitterProvider).buildAndRegisterGlobal()
        Runtime.getRuntime().addShutdownHook(Thread { openTelemetry.close() })
        OpenTelemetryAppender.install(openTelemetry)
    }

    val logger = getLogger(Utils.javaClass.name)
    val MAPPER = jacksonObjectMapper().apply { configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) }

    /**
     * Set up a logger.
     *
     * @param name of the logger
     * @return the logger
     */
    fun getLogger(name: String): KLogger {
        System.getProperty("logDirectory") ?: System.setProperty("logDirectory", "tmp")
        return KotlinLogging.logger(name)
    }

    /**
     * Set up the event-processing options, and start up the event processor.
     *
     * @return the configuration used to set up the event-processing handler, or null if no configuration was found
     */
    inline fun <reified T : CustomConfig<T>> setPackageOps(): T {
        logDiagnostics()
        logger.info { "Looking for configuration in environment variables..." }
        val config = parseConfigFromEnv<T>()
        return config
    }

    /**
     * Output details about the running container to the logs (debug-level) to be able to
     * assist in case any deeper troubleshooting needs to be done.
     */
    fun logDiagnostics() {
        logger.debug { "SDK version: ${BuildInfo.VERSION}" }
        logger.debug { "Java properties:" }
        System.getProperties().forEach { (k, v) ->
            logger.debug { " ... $k = $v" }
        }
        // logClasspathDetails()
    }

    /**
     * Output details about the class path (including ordering) of the executing JVM.
     * (Should rarely be necessary, but here in case it is in the future.)
     */
    fun logClasspathDetails() {
        val classpath = System.getProperty("java.class.path")
        var count = 1
        classpath.split(separator).forEach { p ->
            val cp = if (p.endsWith("*")) Paths.get(p.substringBefore("*")) else Paths.get(p)
            if (cp.isDirectory()) {
                logger.debug { "Classpath ($count) $cp (contains):" }
                cp.toFile().listFiles()?.forEach {
                    logger.debug { " ... ${it.name}" }
                }
            } else {
                logger.debug { "Classpath ($count) $cp (file)" }
            }
            count++
        }
        logger.debug { "System class loader:" }
        val systemCL = ClassLoader.getSystemClassLoader()
        systemCL.definedPackages.forEach {
            logger.debug { " ... $it" }
        }
    }

    /**
     * Set up the custom package's context.
     * This will use an API token if found in ATLAN_API_KEY, and will fallback to attempting
     * to impersonate a user if ATLAN_API_KEY is empty.
     *
     * @param config (optional) configuration for the custom package (will be pulled through {@code setPackageOps} if not provided)
     * @param reuseCtx (optional) existing context of a running package to reuse
     * @return connectivity to the Atlan tenant
     */
    inline fun <reified T : CustomConfig<T>> initializeContext(
        config: T = setPackageOps<T>(),
        reuseCtx: PackageContext<*>? = null,
    ): PackageContext<T> {
        Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler())
        if (reuseCtx != null) config.runtime = reuseCtx.config.runtime
        val impersonateUserId = config.runtime.userId ?: ""
        val baseUrl = getEnvVar("ATLAN_BASE_URL", "INTERNAL")
        val apiToken = getEnvVar("ATLAN_API_KEY", "")
        val userId = getEnvVar("ATLAN_USER_ID", impersonateUserId)
        val client = reuseCtx?.client ?: AtlanClient(baseUrl, apiToken)
        if (reuseCtx?.client == null) {
            when {
                apiToken.isNotEmpty() -> {
                    logger.info { "Using provided API token for authentication." }
                }

                userId.isNotEmpty() -> {
                    logger.info { "No API token found, attempting to impersonate user: $userId" }
                    client.userId = userId
                    client.apiToken = client.impersonate.user(userId)
                }

                else -> {
                    logger.info { "No API token or impersonation user, attempting short-lived escalation." }
                    client.apiToken = client.impersonate.escalate()
                }
            }
            setWorkflowOpts(client, config.runtime)
        }
        return PackageContext(config, client, reuseCtx?.client != null)
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
                logger.info {
                    " ... processed $localCount/$total (${round((localCount.toDouble() / total) * 100)}%)"
                }
            }
        } else {
            logger.info {
                " ... processed $localCount/$total (${round((localCount.toDouble() / total) * 100)}%)"
            }
        }
    }

    /**
     * Check if the utility is being run through a workflow, and if it is set up the various
     * workflow headers from the relevant environment variables against the default client.
     *
     * @param client connectivity to the Atlan tenant
     * @param config parameters received through means other than environment variables to use as a fallback
     */
    fun setWorkflowOpts(
        client: AtlanClient,
        config: RuntimeConfig? = null,
    ) {
        val atlanAgent = getEnvVar("X_ATLAN_AGENT", config?.agent ?: "")
        if (atlanAgent == "workflow") {
            val headers = client.extraHeaders
            headers["x-atlan-agent"] = listOf("workflow")
            headers["x-atlan-agent-package-name"] = listOf(getEnvVar("X_ATLAN_AGENT_PACKAGE_NAME", config?.agentPackageName ?: ""))
            headers["x-atlan-agent-workflow-id"] = listOf(getEnvVar("X_ATLAN_AGENT_WORKFLOW_ID", config?.agentWorkflowId ?: ""))
            headers["x-atlan-agent-id"] = listOf(getEnvVar("X_ATLAN_AGENT_ID", config?.agentId ?: ""))
            client.extraHeaders = headers
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
    inline fun <reified T : CustomConfig<T>> parseConfigFromEnv(): T {
        logger.info { "Constructing configuration from environment variables..." }
        val envVar = getEnvVar("NESTED_CONFIG")
        logger.debug { "Raw config from environment variable: $envVar" }
        val runtime = buildRuntimeConfig()
        logger.debug { "Raw runtime config: $runtime" }
        return parseConfig(getEnvVar("NESTED_CONFIG"), runtime)
    }

    /**
     * Parse configuration from the provided input strings.
     *
     * @param config the event-processing-specific configuration
     * @param runtime the general runtime configuration from the workflow
     * @return the complete configuration for the event-handling pipeline
     */
    inline fun <reified T : CustomConfig<T>> parseConfig(
        config: String,
        runtime: String,
    ): T {
        logger.info { "Parsing configuration..." }
        val type = MAPPER.typeFactory.constructType(T::class.java)
        val cfg = MAPPER.readValue<T>(config, type)
        cfg.runtime = MAPPER.readValue(runtime, RuntimeConfig::class.java)
        logger.debug { "Parsed configuration: ${MAPPER.writeValueAsString(cfg)}" }
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
    inline fun <reified T> getOrDefault(
        configValue: String?,
        default: T,
    ): T {
        if (configValue.isNullOrEmpty()) {
            return default
        }
        return when (default) {
            // TODO: likely need to extend to other types
            is List<*> -> getOrDefault(null, default as List<String>) as T
            else -> {
                // Recognize the default file location, and if found treat it as
                // a blank value
                if (configValue.startsWith("/tmp") && configValue.endsWith(DEFAULT_FILE)) {
                    default
                } else {
                    configValue as T
                }
            }
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
    fun getOrDefault(
        configValue: List<String>?,
        default: List<String>,
    ): List<String> = if (configValue.isNullOrEmpty()) default else configValue

    /**
     * Return the provided configuration value only if it is non-null and not empty,
     * otherwise return the provided default value instead.
     *
     * @param configValue to return if there is a non-null, non-empty value
     * @param default to return if the configValue is either null or empty
     * @return the actual value or a default, if the actual is null or empty
     */
    fun getOrDefault(
        configValue: Number?,
        default: Number,
    ): Number =
        if (configValue == null || configValue == -1) {
            default
        } else {
            configValue
        }

    /**
     * Return the provided configuration value only if it is non-null and not empty,
     * otherwise return the provided default value instead.
     *
     * @param configValue to return if there is a non-null, non-empty value
     * @param default to return if the configValue is either null or empty
     * @return the actual value or a default, if the actual is null or empty
     */
    fun getOrDefault(
        configValue: Boolean?,
        default: Boolean,
    ): Boolean = configValue ?: default

    /**
     * Returns the provided comma-separated configuration value as a list of strings.
     *
     * @param configValue value pulled from the configuration (could be null)
     * @return list of strings, split from the configuration value (or an empty list if there were no values)
     */
    fun getAsList(configValue: String?): List<String> {
        if (configValue == null) {
            return listOf()
        }
        return configValue
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toList()
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
     * @param client connectivity to the Atlan tenant
     * @param action to take: CREATE to create a new connection, or REUSE to reuse an existing connection (default if empty)
     * @param connectionQN qualifiedName of the connection to reuse (only applies when action is REUSE)
     * @param connection connection object to use to create a new connection (only applies when action is CREATE)
     * @return the qualifiedName of the connection to use, whether reusing or creating, or an empty string if neither variable has any data in it
     */
    fun createOrReuseConnection(
        client: AtlanClient,
        action: String?,
        connectionQN: String?,
        connection: Connection?,
    ): String =
        if (getOrDefault(action, "REUSE") == "REUSE") {
            reuseConnection(client, connectionQN)
        } else {
            createConnection(client, connection)
        }

    /**
     * Create a connection using the details provided.
     *
     * @param client connectivity to the Atlan tenant
     * @param connection a connection object, defining the connection to be created
     * @return the qualifiedName of the connection that is created, or an empty string if no connection details were provided
     */
    fun createConnection(
        client: AtlanClient,
        connection: Connection?,
    ): String =
        if (connection != null) {
            logger.info { "Attempting to create new connection..." }
            try {
                val toCreate =
                    connection
                        .toBuilder()
                        .guid("-${ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1)}")
                        .build()
                val response = toCreate.save(client).block()
                response?.getResult(toCreate)?.qualifiedName ?: toCreate.qualifiedName
            } catch (e: AtlanException) {
                logger.error("Unable to create connection: {}", connection, e)
                exitProcess(3)
            }
        } else {
            ""
        }

    /**
     * Calculate the creation handling semantic from a string semantic.
     *
     * @param semantic string input for the semantic from the workflow setup
     * @param default default semantic to use if no value was specified
     * @return enumerated semantic
     */
    fun getCreationHandling(
        semantic: String?,
        default: AssetCreationHandling,
    ): AssetCreationHandling =
        if (semantic == null) {
            default
        } else {
            AssetCreationHandling.fromValue(semantic.lowercase())
        }

    /**
     * Calculate the custom metadata handling semantic from a string semantic.
     *
     * @param semantic string input for the semantic from the workflow setup
     * @param default default semantic to use if no value was specified
     * @return enumerated semantic
     */
    fun getCustomMetadataHandling(
        semantic: String?,
        default: CustomMetadataHandling,
    ): CustomMetadataHandling =
        if (semantic == null) {
            default
        } else {
            CustomMetadataHandling.fromValue(semantic.lowercase()) ?: default
        }

    /**
     * Calculate the Atlan tag association handling semantic from a string semantic.
     *
     * @param semantic string input for the semantic from the workflow setup
     * @param default default semantic to use if no value was specified
     * @return enumerated semantic
     */
    fun getAtlanTagHandling(
        semantic: String?,
        default: AtlanTagHandling,
    ): AtlanTagHandling =
        if (semantic == null) {
            default
        } else {
            AtlanTagHandling.fromValue(semantic.lowercase()) ?: default
        }

    /**
     * Calculate the link idempotency invariant from a string.
     *
     * @param invariant string input for the invariant from the workflow setup
     * @param default default invariant to use if no value was specified
     * @return enumerated invariant
     */
    fun getLinkIdempotency(
        invariant: String?,
        default: LinkIdempotencyInvariant,
    ): LinkIdempotencyInvariant =
        if (invariant == null) {
            default
        } else {
            LinkIdempotencyInvariant.fromValue(invariant.lowercase()) ?: default
        }

    /**
     * Validate the provided connection exists, and if so return its qualifiedName.
     *
     * @param client connectivity to the Atlan tenant
     * @param providedConnectionQN qualifiedName of connection to reuse
     * @return the qualifiedName of the connection, so long as it exists, otherwise an empty string
     */
    fun reuseConnection(
        client: AtlanClient,
        providedConnectionQN: String?,
    ): String =
        providedConnectionQN?.let {
            try {
                logger.info { "Attempting to reuse connection: $providedConnectionQN" }
                Connection.get(client, providedConnectionQN, false)
                providedConnectionQN
            } catch (e: NotFoundException) {
                logger.error("Unable to find connection with the provided qualifiedName: {}", providedConnectionQN, e)
                ""
            }
        } ?: ""

    /**
     * Send an email using the tenant's internal SMTP server.
     *
     * @param subject subject line for the email
     * @param recipients collection of email addresses to send the email to
     * @param body content of the email (plain text)
     * @param attachments (optional) attachments to include in the email
     * @param html (optional) an HTML version of the content of the email
     */
    fun sendEmail(
        subject: String,
        recipients: Collection<String>,
        body: String,
        attachments: Collection<File>? = null,
        html: String? = null,
    ) {
        val builder =
            EmailBuilder
                .startingBlank()
                .from("support@atlan.app")
                .withRecipients(null, false, recipients, Message.RecipientType.TO)
                .withSubject(subject)
                .withPlainText("$body\n\n")
        if (!html.isNullOrBlank()) {
            builder.withHTMLText("$html\n\n")
        }
        val overallAttachmentSize = attachments?.sumOf { it.length() } ?: 0L
        if (overallAttachmentSize >= (30 * 1024 * 1024)) {
            logger.warn { "Maximum attachment size for email distribution is 30 MB, which is exceeded - files will NOT be attached (total size: ${overallAttachmentSize / 1024 / 1024} MB)." }
        } else {
            attachments?.forEach {
                builder.withAttachment(it.name, FileDataSource(it))
            }
        }
        val email = builder.buildEmail()
        MailerBuilder
            .withSMTPServer(
                getEnvVar("SMTP_HOST", "smtp.sendgrid.net"),
                getEnvVar("SMTP_PORT", "587").toInt(),
                getEnvVar("SMTP_USER"),
                getEnvVar("SMTP_PASS"),
            ).buildMailer()
            .sendMail(email)
            .get()
    }

    /**
     * Return a URL that will link directly to an asset in Atlan.
     *
     * @param client connectivity to the Atlan tenant
     * @param guid of the asset for which to produce a link
     * @return a URL that links directly to an asset in Atlan
     */
    fun getAssetLink(
        client: AtlanClient,
        guid: String,
    ): String = getLink(client, guid, "assets")

    /**
     * Return a URL that will link directly to a data product or data domain in Atlan.
     *
     * @param client connectivity to the Atlan tenant
     * @param guid of the asset for which to produce a link
     * @return a URL that links directly to a data product or data domain in Atlan
     */
    fun getProductLink(
        client: AtlanClient,
        guid: String,
    ): String = getLink(client, guid, "products")

    private fun getLink(
        client: AtlanClient,
        guid: String,
        prefix: String,
    ): String {
        val base =
            if (client.isInternal || client.baseUrl == null) {
                "https://${getEnvVar("DOMAIN", "atlan.com")}"
            } else {
                client.baseUrl
            }
        return "$base/$prefix/$guid/overview"
    }

    /**
     * Return the (container-)local input file name whenever a user is given the
     * choice of how to provide an input file (either by uploading directly or through an object store).
     * If using the object store details, the object will be downloaded from the object store and placed into local
     * storage as part of this method.
     *
     * @param uploadResult filename from a direct upload
     * @param outputDirectory local directory where any object storage-downloaded file should be placed
     * @param preferUpload if true, take the directly-uploaded file; otherwise use the object store details to download the file
     * @param prefix (path / directory) where the file is located in object store
     * @param key (filename) of the file in object store
     * @return the name of the file that is on local container storage from which we can read information
     */
    fun getInputFile(
        uploadResult: String,
        outputDirectory: String,
        preferUpload: Boolean = true,
        prefix: String? = null,
        key: String? = null,
    ): String {
        return if (preferUpload) {
            uploadResult
        } else {
            val preppedPrefix = getOrDefault(prefix?.trimEnd('/'), "")
            val preppedKey = getOrDefault(key?.trimStart('/'), "")
            val credFile = Paths.get("/tmp", "credentials", "success", "result-0.json")
            val preppedPath = if (preppedPrefix.isBlank()) preppedKey else "$preppedPrefix/$preppedKey"
            return if (credFile.exists()) {
                val contents = credFile.readText()
                val cred = MAPPER.readValue<Credential>(contents)
                if (preppedKey.isBlank()) {
                    return getInputFiles(uploadResult, outputDirectory, preferUpload, prefix)[0]
                }
                when (cred.authType) {
                    "s3" -> {
                        val s3 = S3Credential(cred)
                        val sync = S3Sync(s3.bucket, s3.region, logger, s3.accessKey, s3.secretKey, s3.roleArn)
                        getInputFile(sync, outputDirectory, preppedPath)
                    }

                    "gcs" -> {
                        val gcs = GCSCredential(cred)
                        val sync = GCSSync(gcs.projectId, gcs.bucket, logger, gcs.serviceAccountJson)
                        getInputFile(sync, outputDirectory, preppedPath)
                    }

                    "adls" -> {
                        val adls = ADLSCredential(cred)
                        val sync =
                            ADLSSync(
                                adls.storageAccount,
                                adls.containerName,
                                logger,
                                adls.tenantId,
                                adls.clientId,
                                adls.clientSecret,
                            )
                        getInputFile(sync, outputDirectory, preppedPath)
                    }

                    else -> {
                        logger.warn { "Unknown source ${cred.authType} -- skipping." }
                        ""
                    }
                }
            } else {
                val sync = getBackingStore()
                getInputFile(sync, outputDirectory, preppedPath)
            }
        }
    }

    /**
     * Return the (container-)local input file names whenever a user is given the
     * choice of how to provide an input file (either by uploading directly or through an object store).
     * If using the object store details, the objects will be downloaded from the object store and placed into local
     * storage as part of this method.
     *
     * @param uploadResult filename from a direct upload
     * @param outputDirectory local directory where any object storage-downloaded file should be placed
     * @param preferUpload if true, take the directly-uploaded file; otherwise use the object store details to download the files
     * @param prefix (path / directory) where the files are located in object store
     * @return the names of the files that are on local container storage from which we can read information
     */
    @OptIn(ExperimentalPathApi::class)
    fun getInputFiles(
        uploadResult: String,
        outputDirectory: String,
        preferUpload: Boolean = true,
        prefix: String? = null,
    ): List<String> {
        return if (preferUpload) {
            val path = Paths.get(uploadResult)
            val files = mutableListOf<String>()
            return if (path.isDirectory()) {
                path.walk().forEach {
                    if (it.isRegularFile()) {
                        files.add(it.pathString)
                    }
                }
                files
            } else {
                listOf(uploadResult)
            }
        } else {
            val credFile = Paths.get("/tmp", "credentials", "success", "result-0.json")
            return if (credFile.exists()) {
                val contents = credFile.readText()
                val cred = MAPPER.readValue<Credential>(contents)
                when (cred.authType) {
                    "s3" -> {
                        val s3 = S3Credential(cred)
                        val sync = S3Sync(s3.bucket, s3.region, logger, s3.accessKey, s3.secretKey, s3.roleArn)
                        getInputFiles(sync, outputDirectory, prefix)
                    }

                    // Note: these are specific to the csa-connectors-s3 connector credential config
                    "iam", "role" -> {
                        val s3 = S3Credential(cred)
                        val sync = S3Sync(s3.bucket, s3.region, logger, s3.accessKey, s3.secretKey, s3.roleArn)
                        getInputFiles(sync, outputDirectory, s3.prefix)
                    }

                    "gcs" -> {
                        val gcs = GCSCredential(cred)
                        val sync = GCSSync(gcs.projectId, gcs.bucket, logger, gcs.serviceAccountJson)
                        getInputFiles(sync, outputDirectory, prefix)
                    }

                    "adls" -> {
                        val adls = ADLSCredential(cred)
                        val sync =
                            ADLSSync(
                                adls.storageAccount,
                                adls.containerName,
                                logger,
                                adls.tenantId,
                                adls.clientId,
                                adls.clientSecret,
                            )
                        getInputFiles(sync, outputDirectory, prefix)
                    }

                    else -> {
                        logger.warn { "Unknown source ${cred.authType} -- skipping." }
                        listOf()
                    }
                }
            } else {
                val sync = getBackingStore()
                getInputFiles(sync, outputDirectory, prefix)
            }
        }
    }

    /**
     * Given an object storage syncer, download the specified file to the local outputDirectory.
     *
     * @param syncer object storage syncer
     * @param outputDirectory local directory into which to download the file
     * @param prefix object prefix in object storage
     * @return a list of paths of the input files
     */
    fun getInputFiles(
        syncer: ObjectStorageSyncer,
        outputDirectory: String,
        prefix: String? = null,
    ): List<String> {
        val preppedPrefix = getOrDefault(prefix?.trimEnd('/'), "")
        return syncer.copyFrom("$preppedPrefix/", outputDirectory)
    }

    /**
     * Given an object storage syncer, download the specified file to the local outputDirectory.
     *
     * @param syncer object storage syncer
     * @param outputDirectory local directory into which to download the file
     * @param remote object key in object storage
     * @return the path of the input file
     */
    fun getInputFile(
        syncer: ObjectStorageSyncer,
        outputDirectory: String,
        remote: String,
    ): String {
        val filename = File(remote).name
        val path = "$outputDirectory${separator}$filename"
        syncer.downloadFrom(remote, path)
        return path
    }

    /**
     * Determines whether a particular input file has been provided or not.
     *
     * @param importType details of the type of import expected (direct vs object store-based)
     * @param directFile details of the directly-provided file
     * @param objectStoreKey details of the object store-provided file
     * @return true if the file has been provided through either means, otherwise false
     */
    fun isFileProvided(
        importType: String,
        directFile: String,
        objectStoreKey: String,
    ): Boolean {
        val directUpload = importType == "DIRECT"
        return (directUpload && directFile.isNotBlank() && !directFile.endsWith(DEFAULT_FILE)) ||
            (!directUpload && objectStoreKey.isNotBlank())
    }

    /**
     * Unzip the provided zip file into the specified directory.
     *
     * @param zipFilePath path to the zip file to unzip
     * @param destDirPath path to the directory where the files should be unzipped
     * @return list of absolute paths of the unzipped files
     */
    fun unzipFiles(
        zipFilePath: String,
        destDirPath: String,
    ): List<String> {
        val destDir = File(destDirPath)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }

        ZipInputStream(FileInputStream(zipFilePath)).use { zipInputStream ->
            var entry: ZipEntry? = zipInputStream.nextEntry
            while (entry != null) {
                val newFile = File(destDir, entry.name)
                if (entry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile?.mkdirs()
                    FileOutputStream(newFile).use { outputStream ->
                        zipInputStream.copyTo(outputStream)
                    }
                }
                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }
        }

        return destDir.listFiles()?.map { it.absolutePath } ?: emptyList()
    }

    /**
     * Upload the provided output file to the object store defined by the credentials available.
     * Note: if no credentials are provided, the default (in-tenant) object store will be used.
     *
     * @param outputFile path and filename of the file to upload
     * @param prefix (path / directory) where the file should be uploaded in object store
     * @param key (filename) of the file in object store
     * @param timestamp (optional) timestamp to inject (wherever {{ts}} appears)
     */
    fun uploadOutputFile(
        outputFile: String,
        prefix: String? = null,
        key: String? = null,
        timestamp: String? = null,
    ) {
        val credFile = Paths.get("/tmp", "credentials", "success", "result-0.json")
        val sync: ObjectStorageSyncer? =
            if (credFile.exists()) {
                val contents = credFile.readText()
                val cred = MAPPER.readValue<Credential>(contents)
                when (cred.authType) {
                    "s3" -> {
                        val s3 = S3Credential(cred)
                        S3Sync(s3.bucket, s3.region, logger, s3.accessKey, s3.secretKey, s3.roleArn)
                    }

                    "gcs" -> {
                        val gcs = GCSCredential(cred)
                        GCSSync(gcs.projectId, gcs.bucket, logger, gcs.serviceAccountJson)
                    }

                    "adls" -> {
                        val adls = ADLSCredential(cred)
                        ADLSSync(
                            adls.storageAccount,
                            adls.containerName,
                            logger,
                            adls.tenantId,
                            adls.clientId,
                            adls.clientSecret,
                        )
                    }

                    else -> {
                        logger.warn { "Unknown target ${cred.authType} -- skipping." }
                        null
                    }
                }
            } else {
                getBackingStore()
            }
        sync?.let { uploadOutputFile(it, outputFile, prefix, key, timestamp) } ?: {
            throw IllegalStateException("No valid target to upload output file found.")
        }
    }

    /**
     * Upload the provided output file to the object store via the provided syncer.
     *
     * @param syncer through which to upload the file
     * @param outputFile path and filename of the file to upload
     * @param prefix (path / directory) where the file should be uploaded in object store
     * @param key (filename) of the file in object store
     * @param timestamp (optional) timestamp to inject (wherever {{ts}} appears)
     */
    fun uploadOutputFile(
        syncer: ObjectStorageSyncer,
        outputFile: String,
        prefix: String? = null,
        key: String? = null,
        timestamp: String? = null,
    ) {
        val preppedPrefix = getOrDefault(prefix?.trimEnd('/'), "")
        val preppedKey = getOrDefault(key?.trimStart('/'), File(outputFile).name) // default to filename from the output file
        val injectedPrefix = injectTimestampIfRequested(preppedPrefix, timestamp)
        val injectedKey = injectTimestampIfRequested(preppedKey, timestamp)
        val preppedPath = if (injectedPrefix.isBlank()) injectedKey else "$injectedPrefix/$injectedKey"
        syncer.uploadTo(outputFile, preppedPath)
    }

    /**
     * Calculate an ISO-8601-formatted string representing the precise current time.
     * For example: 20250829-180810891
     *
     * @return the ISO-8601-formatted string for the current date and time
     */
    fun getNowAsISO8601(): String = ISO_8601_FORMATTER.format(Instant.now())

    /**
     * If the special characters {{ts}} appear in the string, replace them with the current timestamp.
     *
     * @param path in which to look for the string
     * @param timestamp (optional) the timestamp to inject (if not provided, will generate one for the time the file is uploaded)
     * @return the timestamp-injected string (if {{ts}} appeared), or the original string (if not)
     */
    private fun injectTimestampIfRequested(
        path: String,
        timestamp: String? = null,
    ): String {
        if (!path.contains("{{ts}}")) return path
        val toInject = timestamp ?: getNowAsISO8601()
        return path.replace("{{ts}}", toInject)
    }

    /**
     * Return the backing store of the Atlan tenant.
     *
     * @param directory (optional) fallback directory to use on local filesystem if no object store is detected
     * @return object storage syncer for Atlan's backing store
     */
    fun getBackingStore(directory: String = Paths.get(separator, "tmp").toString()): ObjectStorageSyncer =
        when (val cloud = getEnvVar("CLOUD_PROVIDER", "local")) {
            "aws" -> S3Sync(getEnvVar("AWS_S3_BUCKET_NAME"), getEnvVar("AWS_S3_REGION"), logger)
            "gcp" -> GCSSync(getEnvVar("GCP_PROJECT_ID"), getEnvVar("GCP_STORAGE_BUCKET"), logger, "")
            "azure" -> ADLSSync(getEnvVar("AZURE_STORAGE_ACCOUNT"), getEnvVar("AZURE_STORAGE_CONTAINER_NAME"), logger, "", "", getEnvVar("AZURE_STORAGE_ACCESS_KEY"))
            "local" -> {
                if (getEnvVar("AWS_S3_BUCKET_NAME").isNotBlank()) {
                    S3Sync(getEnvVar("AWS_S3_BUCKET_NAME"), getEnvVar("AWS_S3_REGION"), logger)
                } else {
                    LocalSync(directory, logger)
                }
            }
            else -> throw IllegalStateException("Unable to determine cloud provider: $cloud")
        }

    /**
     * Update the connection cache for the provided assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param added assets that were added
     * @param removed assets that were deleted
     * @param fallback directory to use for a fallback backing store for the cache
     */
    fun updateConnectionCache(
        client: AtlanClient,
        added: OffHeapAssetCache? = null,
        removed: OffHeapAssetCache? = null,
        fallback: String = Paths.get(separator, "tmp").toString(),
    ) {
        val map = CacheUpdates.build(client, added, removed)
        logger.info { "Updating connection caches for ${map.size} connections..." }
        val sync = getBackingStore(fallback)
        for ((connectionQN, cacheUpdates) in map) {
            cacheUpdates.use { assets ->
                logger.info { "Updating connection cache for: $connectionQN" }
                val paths = mutableListOf<String>()
                paths.addAll(fallback.split(separator))
                paths.add("cache")
                paths.addAll(connectionQN.split("/"))
                val tmpFile = paths.joinToString(separator = separator)
                // Retrieve any pre-existing cache first, so we can update it
                try {
                    sync.downloadFrom("connection-cache/$connectionQN.sqlite", tmpFile)
                    logger.info { " ... downloaded pre-existing cache to update" }
                } catch (e: IOException) {
                    logger.info { " ... unable to download pre-existing cache, creating a new one" }
                    logger.debug(e) { "Location attempted from backing store: connection-cache/$connectionQN.sqlite" }
                }
                val cache = PersistentConnectionCache(tmpFile)
                cache.addAssets(assets.added.values())
                cache.deleteAssets(assets.removed.values())
                // Replace the cache with the updated one
                try {
                    sync.uploadTo(tmpFile, "connection-cache/$connectionQN.sqlite")
                    logger.info { " ... uploaded updated cache" }
                } catch (e: IOException) {
                    logger.error(e) { " ... unable to upload updated cache: connection-cache/$connectionQN.sqlite" }
                }
            }
        }
    }

    /**
     * Validates that a user-provided path is safe, within the context
     * of a defined base directory.
     *
     * @param baseDirectory within which the path is to be validated
     * @param userProvided location the user has provided
     */
    @Throws(IllegalArgumentException::class)
    fun validatePathIsSafe(
        baseDirectory: String,
        userProvided: String,
    ) {
        val base = Paths.get(baseDirectory).toAbsolutePath().normalize()
        val resolved = base.resolve(userProvided).normalize()
        when {
            !resolved.startsWith(base) ->
                IllegalArgumentException("Path traversal attempt detected -- will not proceed due to security implications.")
            userProvided.contains('\u0000') ->
                IllegalArgumentException("Null bytes in the path or filename are not allowed.")
            resolved.toAbsolutePath().toString().length > 800 ->
                IllegalArgumentException("User-provided path and filename are too long (exceeds maximum length of 800 characters).")
        }
    }

    private data class CacheUpdates(
        val added: OffHeapAssetCache,
        val removed: OffHeapAssetCache,
    ) : Closeable {
        companion object {
            fun build(
                client: AtlanClient,
                add: OffHeapAssetCache?,
                remove: OffHeapAssetCache?,
            ): Map<String, CacheUpdates> {
                val map = mutableMapOf<String, CacheUpdates>()
                add?.values()?.forEach { asset ->
                    val connectionQN = asset.connectionQualifiedName
                    connectionQN?.let {
                        if (!map.containsKey(it)) {
                            map[it] =
                                CacheUpdates(
                                    added = OffHeapAssetCache(client, connectionQN.replace('/', '_')),
                                    removed = OffHeapAssetCache(client, connectionQN.replace('/', '_')),
                                )
                        }
                        map[it]!!.added.add(asset)
                    } ?: logger.debug { "No connection qualifiedName found for asset -- skipping: ${asset.toJson(client)}" }
                }
                remove?.values()?.forEach { asset ->
                    val connectionQN = asset.connectionQualifiedName
                    connectionQN?.let {
                        if (!map.containsKey(it)) {
                            map[it] =
                                CacheUpdates(
                                    added = OffHeapAssetCache(client, connectionQN.replace('/', '_')),
                                    removed = OffHeapAssetCache(client, connectionQN.replace('/', '_')),
                                )
                        }
                        map[it]!!.removed.add(asset)
                    } ?: logger.debug { "No connection qualifiedName found for asset -- skipping: ${asset.toJson(client)}" }
                }
                return map
            }
        }

        /** {@inheritDoc} */
        override fun close() {
            var exception: IOException? = null
            try {
                added.close()
            } catch (e: IOException) {
                exception = e
            }
            try {
                removed.close()
            } catch (e: IOException) {
                if (exception == null) exception = e else exception.addSuppressed(e)
            }
            if (exception != null) throw exception
        }
    }
}
