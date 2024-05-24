/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.pkg.model.Credential
import com.atlan.pkg.objectstore.ADLSCredential
import com.atlan.pkg.objectstore.ADLSSync
import com.atlan.pkg.objectstore.GCSCredential
import com.atlan.pkg.objectstore.GCSSync
import com.atlan.pkg.objectstore.ObjectStorageSyncer
import com.atlan.pkg.objectstore.S3Credential
import com.atlan.pkg.objectstore.S3Sync
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.activation.FileDataSource
import jakarta.mail.Message
import mu.KLogger
import mu.KotlinLogging
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicLong
import kotlin.io.path.isDirectory
import kotlin.io.path.readText
import kotlin.math.round
import kotlin.system.exitProcess

/**
 * Common utilities for using the Atlan SDK within Kotlin.
 */
object Utils {
    val logger = KotlinLogging.logger {}
    val MAPPER = jacksonObjectMapper()

    // Note: this default value is necessary to avoid internal Argo errors if the
    // file is actually optional (only value that seems likely to be in all tenants' S3 buckets)
    const val DEFAULT_FILE = "argo-artifacts/atlan-update/@atlan-packages-last-safe-run.txt"

    /**
     * Set up the event-processing options, and start up the event processor.
     *
     * @return the configuration used to set up the event-processing handler, or null if no configuration was found
     */
    inline fun <reified T : CustomConfig> setPackageOps(): T {
        System.getProperty("logDirectory") ?: System.setProperty("logDirectory", "tmp")
        logDiagnostics()
        logger.info { "Looking for configuration in environment variables..." }
        val config = parseConfigFromEnv<T>()
        setClient(config.runtime.userId ?: "")
        setWorkflowOpts(config.runtime)
        return config
    }

    /**
     * Output details about the running container to the logs (debug-level) to be able to
     * assist in case any deeper troubleshooting needs to be done.
     */
    fun logDiagnostics() {
        logger.debug { "SDK version: ${Atlan.VERSION}" }
        logger.debug { "Java properties:" }
        System.getProperties().forEach { (k, v) ->
            logger.debug { " ... $k = $v" }
        }
        val classpath = System.getProperty("java.class.path")
        var count = 1
        classpath.split(File.pathSeparator).forEach { p ->
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
                    logger.info { "Using provided API token for authentication." }
                    apiToken
                }
                userId.isNotEmpty() -> {
                    logger.info { "No API token found, attempting to impersonate user: $userId" }
                    Atlan.getDefaultClient().impersonate.user(userId)
                }
                else -> {
                    logger.info { "No API token or impersonation user, attempting short-lived escalation." }
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
        logger.info { "Constructing configuration from environment variables..." }
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
        logger.info { "Parsing configuration..." }
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
    fun getOrDefault(configValue: List<String>?, default: List<String>): List<String> {
        return if (configValue.isNullOrEmpty()) default else configValue
    }

    /**
     * Return the provided configuration value only if it is non-null and not empty,
     * otherwise return the provided default value instead.
     *
     * @param configValue to return if there is a non-null, non-empty value
     * @param default to return if the configValue is either null or empty
     * @return the actual value or a default, if the actual is null or empty
     */
    fun getOrDefault(configValue: Number?, default: Number): Number {
        return if (configValue == null || configValue == -1) {
            default
        } else {
            configValue
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
    fun getOrDefault(configValue: Boolean?, default: Boolean): Boolean {
        return configValue ?: default
    }

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
        return configValue.split(",")
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
            logger.info { "Attempting to create new connection..." }
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
     * Calculate the creation handling semantic from a string semantic.
     *
     * @param semantic string input for the semantic from the workflow setup
     * @param default default semantic to use if no value was specified
     * @return enumerated semantic
     */
    fun getCreationHandling(semantic: String?, default: AssetCreationHandling): AssetCreationHandling {
        return if (semantic == null) {
            default
        } else {
            AssetCreationHandling.fromValue(semantic)
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
                logger.info { "Attempting to reuse connection: $providedConnectionQN" }
                Connection.get(Atlan.getDefaultClient(), providedConnectionQN, false)
                providedConnectionQN
            } catch (e: NotFoundException) {
                logger.error("Unable to find connection with the provided qualifiedName: {}", providedConnectionQN, e)
                ""
            }
        } ?: ""
    }

    /**
     * Send an email using the tenant's internal SMTP server.
     *
     * @param subject subject line for the email
     * @param recipients collection of email addresses to send the email to
     * @param body content of the email (plain text)
     * @param attachments (optional) attachments to include in the email
     */
    fun sendEmail(
        subject: String,
        recipients: Collection<String>,
        body: String,
        attachments: Collection<File>? = null,
    ) {
        val builder = EmailBuilder.startingBlank()
            .from("support@atlan.app")
            .withRecipients(null, false, recipients, Message.RecipientType.TO)
            .withSubject(subject)
            .withPlainText("$body\n\n")
        attachments?.forEach {
            builder.withAttachment(it.name, FileDataSource(it))
        }
        val email = builder.buildEmail()
        MailerBuilder.withSMTPServer(
            getEnvVar("SMTP_HOST", "smtp.sendgrid.net"),
            getEnvVar("SMTP_PORT", "587").toInt(),
            getEnvVar("SMTP_USER"),
            getEnvVar("SMTP_PASS"),
        ).buildMailer().sendMail(email).get()
    }

    /**
     * Return a URL that will link directly to an asset in Atlan.
     *
     * @param guid of the asset for which to produce a link
     */
    fun getAssetLink(guid: String): String {
        return getLink(guid, "assets")
    }

    /**
     * Return a URL that will link directly to a data product or data domain in Atlan.
     *
     * @param guid of the asset for which to produce a link
     */
    fun getProductLink(guid: String): String {
        return getLink(guid, "products")
    }

    private fun getLink(guid: String, prefix: String): String {
        val base = if (Atlan.getDefaultClient().isInternal || Atlan.getBaseUrl() == null) {
            "https://${getEnvVar("DOMAIN", "atlan.com")}"
        } else {
            Atlan.getBaseUrl()
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
     * @param cloudDetails filename containing details about the cloud storage to use
     * @param outputDirectory local directory where any object storage-downloaded file should be placed
     * @param preferUpload if true, take the directly-uploaded file; otherwise use the object store details to download the file
     * @return the name of the file that is on local container storage from which we can read information
     */
    fun getInputFile(
        uploadResult: String,
        cloudDetails: String,
        outputDirectory: String,
        preferUpload: Boolean = true,
    ): String {
        return if (preferUpload) {
            uploadResult
        } else {
            val contents = Paths.get("/tmp", "credentials", "success", "result-0.json").readText()
            val cred = MAPPER.readValue<Credential>(contents)
            when (cred.authType) {
                "s3" -> {
                    val s3 = S3Credential(cred)
                    val sync = S3Sync(s3.bucket, s3.region, logger, s3.accessKey, s3.secretKey)
                    getInputFile(sync, "${s3.objectPrefix}/${s3.objectKey}", outputDirectory)
                }
                "gcs" -> {
                    val gcs = GCSCredential(cred)
                    val sync = GCSSync(gcs.projectId, gcs.bucket, logger, gcs.serviceAccountJson)
                    getInputFile(sync, "${gcs.objectPrefix}/${gcs.objectKey}", outputDirectory)
                }
                "adls" -> {
                    val adls = ADLSCredential(cred)
                    val sync = ADLSSync(adls.storageAccount, adls.containerName, logger, adls.tenantId, adls.clientId, adls.clientSecret)
                    getInputFile(sync, "${adls.objectPrefix}/${adls.objectKey}", outputDirectory)
                }
                else -> {
                    logger.warn { "Unknown source ${cred.authType} -- skipping." }
                    ""
                }
            }
        }
    }

    /**
     * Return the (container-)local input file name whenever a user is given the
     * choice of how to provide an input file (either by uploading directly or through an object store).
     * If using the object store details, the object will be downloaded from the object store and placed into local
     * storage as part of this method.
     *
     * @param uploadResult filename from a direct upload
     * @param outputDirectory local directory where any object storage-downloaded file should be placed
     * @param s3Region name of the S3 region for an S3 download
     * @param s3Bucket name of the S3 bucket for an S3 download
     * @param s3ObjectKey full path to the S3 object within the bucket
     * @param gcsProjectId project ID for GCP
     * @param gcsBucket name of the GCS bucket for a GCS download
     * @param gcsObjectKey full path to the GCS object within the bucket
     * @param gcsCredentials location of JSON file containing GCS credentials
     * @param adlsAccountName name of the Azure account
     * @param adlsContainerName fname of the ADLS container for an ADLS download
     * @param adlsObjectKey full path to the ADLS object within the container
     * @param adlsSasToken shared access secret (SAS) token for ADLS
     * @param preferUpload if true, take the directly-uploaded file; otherwise use the object store details to download the file
     * @return the name of the file that is on local container storage from which we can read information
     */
    fun getInputFile(
        uploadResult: String,
        outputDirectory: String,
        s3Region: String = "",
        s3Bucket: String = "",
        s3ObjectKey: String = "",
        gcsProjectId: String = "",
        gcsBucket: String = "",
        gcsObjectKey: String = "",
        gcsCredentials: String = "",
        adlsAccountName: String = "",
        adlsContainerName: String = "",
        adlsObjectKey: String = "",
        adlsSasToken: String = "",
        preferUpload: Boolean = true,
    ): String {
        return if (preferUpload) {
            uploadResult
        } else {
            if (s3ObjectKey.isNotBlank()) {
                val sync = S3Sync(s3Bucket, s3Region, logger)
                getInputFile(sync, s3ObjectKey, outputDirectory)
            } else if (gcsObjectKey.isNotBlank()) {
                val sync = GCSSync(gcsProjectId, gcsBucket, logger, gcsCredentials)
                getInputFile(sync, gcsObjectKey, outputDirectory)
            } else if (adlsObjectKey.isNotBlank()) {
                val sync = ADLSSync(adlsAccountName, adlsContainerName, logger, "", "", "")
                getInputFile(sync, adlsObjectKey, outputDirectory)
            } else {
                ""
            }
        }
    }

    /**
     * Given an object storage syncer, download the specified file to the local outputDirectory.
     *
     * @param syncer object storage syncer
     * @param remote object key in object storage
     * @param outputDirectory local directory into which to download the file
     */
    private fun getInputFile(syncer: ObjectStorageSyncer, remote: String, outputDirectory: String): String {
        val filename = File(remote).name
        val path = "$outputDirectory${File.separator}$filename"
        syncer.downloadFrom(remote, path)
        return path
    }

    /**
     * Return the (container-)local input file names whenever a user is given the
     * choice of how to provide an input file (either by uploading directly or through object storage).
     * If using the object store details, the objects will be downloaded from object storage and placed into local
     * storage as part of this method.
     *
     * @param uploadResult filename from a direct upload
     * @param outputDirectory local directory where any object store-downloaded files should be placed
     * @param s3Region name of the S3 region for an S3 download
     * @param s3Bucket name of the S3 bucket for an S3 download
     * @param s3ObjectKey full path to the S3 object within the bucket (if it ends with a /, all objects in that prefix will be downloaded)
     * @param gcsProjectId project ID for GCP
     * @param gcsBucket name of the GCS bucket for a GCS download
     * @param gcsObjectKey full path to the GCS object within the bucket
     * @param gcsCredentials location of JSON file containing GCS credentials
     * @param adlsAccountName name of the Azure account
     * @param adlsContainerName fname of the ADLS container for an ADLS download
     * @param adlsObjectKey full path to the ADLS object within the container
     * @param adlsSasToken shared access secret (SAS) token for ADLS
     * @param preferUpload if true, take the directly-uploaded file; otherwise use the object store details to download the file(s)
     * @return the name(s) of the file(s) that are on local container storage from which we can read information
     */
    fun getInputFiles(
        uploadResult: String,
        outputDirectory: String,
        s3Region: String = "",
        s3Bucket: String = "",
        s3ObjectKey: String = "",
        gcsProjectId: String = "",
        gcsBucket: String = "",
        gcsObjectKey: String = "",
        gcsCredentials: String = "",
        adlsAccountName: String = "",
        adlsContainerName: String = "",
        adlsObjectKey: String = "",
        adlsSasToken: String = "",
        preferUpload: Boolean = true,
    ): List<String> {
        return if (preferUpload) {
            listOf(uploadResult)
        } else {
            if (s3ObjectKey.isNotBlank() && s3ObjectKey.endsWith("/")) {
                val sync = S3Sync(s3Bucket, s3Region, logger)
                sync.copyFrom(s3ObjectKey, outputDirectory)
            } else if (gcsObjectKey.isNotBlank() && gcsObjectKey.endsWith("/")) {
                val sync = GCSSync(gcsProjectId, gcsBucket, logger, gcsCredentials)
                sync.copyFrom(gcsObjectKey, outputDirectory)
            } else if (adlsObjectKey.isNotBlank() && adlsObjectKey.endsWith("/")) {
                val sync = ADLSSync(adlsAccountName, adlsContainerName, logger, "", "", "")
                sync.copyFrom(adlsObjectKey, outputDirectory)
            } else if (s3ObjectKey.isNotBlank() || gcsObjectKey.isNotBlank() || adlsObjectKey.isNotBlank()) {
                listOf(
                    getInputFile(
                        uploadResult,
                        outputDirectory,
                        s3Region,
                        s3Bucket,
                        s3ObjectKey,
                        gcsProjectId,
                        gcsBucket,
                        gcsObjectKey,
                        gcsCredentials,
                        adlsAccountName,
                        adlsContainerName,
                        adlsObjectKey,
                        adlsSasToken,
                        false,
                    ),
                )
            } else {
                emptyList()
            }
        }
    }
}
