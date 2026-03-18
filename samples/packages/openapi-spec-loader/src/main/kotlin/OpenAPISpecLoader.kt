/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.exception.AtlanException
import com.atlan.model.assets.APIField
import com.atlan.model.assets.APIMethod
import com.atlan.model.assets.APIObject
import com.atlan.model.assets.APIPath
import com.atlan.model.assets.APISpec
import com.atlan.model.core.AssetMutationResponse
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.parser.OpenAPIV3Parser
import java.nio.file.Paths
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.exitProcess

object OpenAPISpecLoader {
    private val logger = Utils.getLogger(OpenAPISpecLoader.javaClass.name)
    private val jsonMapper = ObjectMapper()

    /**
     * Actually run the loader, taking all settings from environment variables.
     * Note: all parameters should be passed through environment variables.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        Utils.initializeContext<OpenAPISpecLoaderCfg>().use { ctx ->
            val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
            val batchSize = 20

            val inputQN =
                ctx.config.connectionQualifiedName?.let {
                    if (it.isNotEmpty()) it[0] else null
                }
            val connectionQN = Utils.createOrReuseConnection(ctx.client, ctx.config.connectionUsage, inputQN, ctx.config.connection)

            val specFileProvided = Utils.isFileProvided(ctx.config.importType, ctx.config.specFile, ctx.config.specKey)
            if (!specFileProvided && ((ctx.config.importType == "URL" && ctx.config.specUrl.isBlank()) || (ctx.config.importType == "CLOUD" && ctx.config.specPrefix.isBlank()))) {
                logger.error { "No input file was provided for the OpenAPI spec." }
                exitProcess(1)
            }

            if (connectionQN.isBlank()) {
                logger.error { "Missing required parameter - you must provide BOTH a connection name and specification URL." }
                exitProcess(4)
            }

            val sourceFiles =
                when (ctx.config.importType) {
                    "DIRECT" -> {
                        listOf(Paths.get(outputDirectory, ctx.config.specFile).toString())
                    }

                    "CLOUD" -> {
                        when {
                            ctx.config.specKey.isBlank() && ctx.config.specPrefix.isNotBlank() -> {
                                Utils.getInputFiles(
                                    ctx.config.specFile,
                                    outputDirectory,
                                    false,
                                    ctx.config.specPrefix,
                                )
                            }

                            else -> {
                                listOf(
                                    Utils.getInputFile(
                                        ctx.config.specFile,
                                        outputDirectory,
                                        false,
                                        ctx.config.specPrefix,
                                        ctx.config.specKey,
                                    ),
                                )
                            }
                        }
                    }

                    "URL" -> {
                        listOf(ctx.config.specUrl)
                    }

                    else -> {
                        logger.error { "Unsupported import type: ${ctx.config.importType}" }
                        exitProcess(5)
                    }
                }
            for (sourceFile in sourceFiles) {
                processFile(ctx, connectionQN, sourceFile, batchSize, outputDirectory)
            }
        }
    }

    private fun processFile(
        ctx: PackageContext<OpenAPISpecLoaderCfg>,
        connectionQN: String,
        sourceFile: String,
        batchSize: Int,
        outputDirectory: String,
    ) {
        val fileType =
            Paths
                .get(sourceFile)
                .toFile()
                .extension
                .lowercase()
        when (fileType) {
            "json", "yaml", "yml" -> {
                logger.info { "Loading OpenAPI specification from $sourceFile into: $connectionQN" }
                loadOpenAPISpec(ctx.client, connectionQN, OpenAPISpecReader(sourceFile), batchSize)
            }

            "zip" -> {
                logger.info { "Extracting and processing ZIP file: $sourceFile" }
                val extractedFiles = Utils.unzipFiles(sourceFile, outputDirectory)
                processExtractedFiles(ctx, connectionQN, extractedFiles, batchSize)
            }

            else -> {
                logger.error { "Invalid file type. Please provide a JSON, YAML or ZIP file." }
                exitProcess(1)
            }
        }
    }

    private fun processExtractedFiles(
        ctx: PackageContext<OpenAPISpecLoaderCfg>,
        connectionQN: String,
        extractedFiles: List<String>,
        batchSize: Int,
    ) {
        extractedFiles.filter { it.endsWith(".json") || it.endsWith(".yaml") || it.endsWith(".yml") }.forEach { file ->
            logger.info { "Loading OpenAPI specification from extracted file: $file" }
            loadOpenAPISpec(ctx.client, connectionQN, OpenAPISpecReader(file), batchSize)
        }
    }

    /**
     * Process the OpenAPI spec and create relevant assets in Atlan.
     *
     * @param client connectivity to the Atlan tenant
     * @param connectionQN qualifiedName of the connection in which to create the assets
     * @param spec object for reading from the OpenAPI spec itself
     * @param batchSize maximum number of assets to save per API request
     */
    fun loadOpenAPISpec(
        client: AtlanClient,
        connectionQN: String,
        spec: OpenAPISpecReader,
        batchSize: Int,
    ) {
        // --- Step 1: Save the APISpec (unchanged from original) ---
        val toCreate =
            APISpec
                .creator(spec.title, connectionQN)
                .sourceURL(spec.sourceURL)
                .apiSpecType(spec.openAPIVersion)
                .description(spec.description)
                .apiSpecTermsOfServiceURL(spec.termsOfServiceURL)
                .apiSpecContactEmail(spec.contactEmail)
                .apiSpecContactName(spec.contactName)
                .apiSpecContactURL(spec.contactURL)
                .apiSpecLicenseName(spec.licenseName)
                .apiSpecLicenseURL(spec.licenseURL)
                .apiSpecVersion(spec.version)
                .apiExternalDoc("url", spec.externalDocsURL)
                .apiExternalDoc("description", spec.externalDocsDescription)
                .build()
        val specQN = toCreate.qualifiedName
        logger.info { "Saving APISpec: $specQN" }
        try {
            val response = toCreate.save(client)
            val mutation = response.getMutation(toCreate)
            if (mutation in
                listOf(
                    AssetMutationResponse.MutationType.NOOP,
                    AssetMutationResponse.MutationType.UNKNOWN,
                )
            ) {
                logger.info { " ... reusing existing APISpec: ${toCreate.qualifiedName}" }
            } else {
                logger.info { " ... ${mutation.name} APISpec: ${toCreate.qualifiedName}" }
            }
        } catch (e: AtlanException) {
            logger.error("Unable to save the APISpec.", e)
            exitProcess(5)
        }

        // --- Step 2: Create APIObjects and APIFields from components/schemas ---
        val objectQNs = mutableMapOf<String, String>() // schemaName -> qualifiedName
        val schemas = spec.schemas
        if (!schemas.isNullOrEmpty()) {
            logger.info { "Creating APIObjects for ${schemas.size} component schema(s)" }
            AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { objectBatch ->
                AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { fieldBatch ->
                    try {
                        for ((schemaName, schema) in schemas) {
                            val objectQN = "$specQN/schemas/$schemaName"
                            objectQNs[schemaName] = objectQN
                            val properties = schema.properties ?: emptyMap()
                            val apiObject =
                                APIObject
                                    ._internal()
                                    .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                                    .qualifiedName(objectQN)
                                    .name(schemaName)
                                    .connectionQualifiedName(connectionQN)
                                    .apiSpecQualifiedName(specQN)
                                    .apiSpecName(spec.title)
                                    .apiSpecType(spec.openAPIVersion)
                                    .apiFieldCount(properties.size.toLong())
                                    .build()
                            objectBatch.add(apiObject)
                        }
                        objectBatch.flush()

                        // Second pass: create APIFields (after all APIObjects exist)
                        for ((schemaName, schema) in schemas) {
                            val objectQN = objectQNs[schemaName]!!
                            val properties = schema.properties ?: emptyMap()
                            for ((fieldName, fieldSchema) in properties) {
                                val fieldQN = "$objectQN/$fieldName"
                                val refSchemaName = extractRefSchemaName(fieldSchema)
                                val isObjectRef = refSchemaName != null
                                val fieldType = resolveFieldType(fieldSchema)
                                val fieldTypeSecondary = resolveFieldTypeSecondary(fieldSchema)
                                val fieldBuilder =
                                    APIField
                                        ._internal()
                                        .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                                        .qualifiedName(fieldQN)
                                        .name(fieldName)
                                        .connectionQualifiedName(connectionQN)
                                        .apiSpecQualifiedName(specQN)
                                        .apiSpecName(spec.title)
                                        .apiSpecType(spec.openAPIVersion)
                                        .apiFieldType(fieldType)
                                        .apiObject(APIObject.refByQualifiedName(objectQN))
                                if (fieldTypeSecondary != null) {
                                    fieldBuilder.apiFieldTypeSecondary(fieldTypeSecondary)
                                }
                                if (isObjectRef) {
                                    fieldBuilder.apiIsObjectReference(true)
                                    val refQN = objectQNs[refSchemaName]
                                    if (refQN != null) {
                                        fieldBuilder.apiObjectQualifiedName(refQN)
                                    }
                                }
                                fieldBatch.add(fieldBuilder.build())
                            }
                        }
                        fieldBatch.flush()
                    } catch (e: AtlanException) {
                        logger.error("Unable to bulk-save API objects/fields.", e)
                    }
                }
            }
            logger.info { "Created ${objectQNs.size} APIObject(s) with their APIField children" }
        }

        // --- Step 3: Create APIPaths (unchanged) and APIMethod per operation ---
        val totalCount = spec.paths?.size!!.toLong()
        if (totalCount > 0) {
            logger.info { "Creating an APIPath for each path defined within the spec (total: $totalCount)" }
            AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { pathBatch ->
                AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { methodBatch ->
                    try {
                        val assetCount = AtomicLong(0)
                        for (apiPath in spec.paths.entries) {
                            val pathUrl = apiPath.key
                            val pathDetails = apiPath.value

                            // --- APIPath creation (unchanged from original) ---
                            val operations = mutableListOf<String>()
                            val desc = StringBuilder()
                            desc.append("| Method | Summary|\n|---|---|\n")
                            addOperationDetails(pathDetails.get, "GET", operations, desc)
                            addOperationDetails(pathDetails.post, "POST", operations, desc)
                            addOperationDetails(pathDetails.put, "PUT", operations, desc)
                            addOperationDetails(pathDetails.patch, "PATCH", operations, desc)
                            addOperationDetails(pathDetails.delete, "DELETE", operations, desc)
                            val path =
                                APIPath
                                    .creator(pathUrl, specQN)
                                    .description(desc.toString())
                                    .apiPathRawURI(pathUrl)
                                    .apiPathSummary(pathDetails.summary)
                                    .apiPathAvailableOperations(operations)
                                    .apiPathIsTemplated(pathUrl.contains("{") && pathUrl.contains("}"))
                                    .build()
                            pathBatch.add(path)
                            val pathQN = path.qualifiedName

                            // --- APIMethod creation per operation ---
                            createMethodIfPresent(pathDetails.get, "GET", pathQN, pathUrl, specQN, connectionQN, spec, objectQNs, methodBatch)
                            createMethodIfPresent(pathDetails.post, "POST", pathQN, pathUrl, specQN, connectionQN, spec, objectQNs, methodBatch)
                            createMethodIfPresent(pathDetails.put, "PUT", pathQN, pathUrl, specQN, connectionQN, spec, objectQNs, methodBatch)
                            createMethodIfPresent(pathDetails.patch, "PATCH", pathQN, pathUrl, specQN, connectionQN, spec, objectQNs, methodBatch)
                            createMethodIfPresent(pathDetails.delete, "DELETE", pathQN, pathUrl, specQN, connectionQN, spec, objectQNs, methodBatch)

                            Utils.logProgress(assetCount, totalCount, logger, batchSize)
                        }
                        pathBatch.flush()
                        methodBatch.flush()
                        Utils.logProgress(assetCount, totalCount, logger, batchSize)
                    } catch (e: AtlanException) {
                        logger.error("Unable to bulk-save API paths/methods.", e)
                    }
                }
            }
        }
    }

    /**
     * Create an APIMethod asset for a single HTTP operation on a path, if the operation exists.
     *
     * @param operation the Swagger operation (may be null if this HTTP method is not defined on the path)
     * @param httpMethod the HTTP method name (GET, POST, PUT, PATCH, DELETE)
     * @param pathQN qualified name of the parent APIPath
     * @param pathUrl the raw path URL (e.g., "/pet/{petId}")
     * @param specQN qualified name of the parent APISpec
     * @param connectionQN qualified name of the connection
     * @param spec the OpenAPISpecReader for spec-level metadata
     * @param objectQNs map of schema name to APIObject qualified name
     * @param batch the AssetBatch to add the method to
     */
    private fun createMethodIfPresent(
        operation: Operation?,
        httpMethod: String,
        pathQN: String,
        pathUrl: String,
        specQN: String,
        connectionQN: String,
        spec: OpenAPISpecReader,
        objectQNs: Map<String, String>,
        batch: AssetBatch,
    ) {
        if (operation == null) return

        val methodName = "$httpMethod $pathUrl"
        val methodQN = "$pathQN/$httpMethod"

        val methodBuilder =
            APIMethod
                ._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(methodQN)
                .name(methodName)
                .connectionQualifiedName(connectionQN)
                .apiSpecQualifiedName(specQN)
                .apiSpecName(spec.title)
                .apiSpecType(spec.openAPIVersion)
                .description(operation.summary ?: operation.description ?: "")
                .apiPath(APIPath.refByQualifiedName(pathQN))

        // Request body blob + schema relationship
        val requestSchema = extractRequestSchema(operation)
        if (requestSchema != null) {
            methodBuilder.apiMethodRequest(serializeSchema(requestSchema))
            val requestRefName = extractRefSchemaName(requestSchema)
            if (requestRefName != null) {
                val requestObjectQN = objectQNs[requestRefName]
                if (requestObjectQN != null) {
                    methodBuilder.apiMethodRequestSchema(APIObject.refByQualifiedName(requestObjectQN))
                }
            }
        }

        // Response bodies: blob for primary response + schema relationships for all
        val responseCodes = mutableMapOf<String, String>()
        val responseSchemas = mutableListOf<String>() // QNs for relationship set
        var primaryResponseBlob: String? = null
        if (operation.responses != null) {
            for ((statusCode, apiResponse) in operation.responses) {
                val responseSchema = extractResponseSchema(apiResponse)
                if (responseSchema != null) {
                    // Use the first success response (2xx) as the primary blob
                    if (primaryResponseBlob == null && statusCode.startsWith("2")) {
                        primaryResponseBlob = serializeSchema(responseSchema)
                    }
                    val refName = extractRefSchemaName(responseSchema)
                    if (refName != null) {
                        val responseObjectQN = objectQNs[refName]
                        if (responseObjectQN != null) {
                            responseCodes[statusCode] = responseObjectQN
                            responseSchemas.add(responseObjectQN)
                        }
                    } else {
                        // Inline schema: create a synthetic APIObject name for the response codes map
                        val inlineName = "${httpMethod}_${pathUrl.replace("/", "_").replace("{", "").replace("}", "")}_$statusCode"
                        responseCodes[statusCode] = inlineName
                    }
                }
            }
        }
        if (primaryResponseBlob != null) {
            methodBuilder.apiMethodResponse(primaryResponseBlob)
        }
        if (responseCodes.isNotEmpty()) {
            methodBuilder.apiMethodResponseCodes(responseCodes)
        }
        for (responseQN in responseSchemas) {
            methodBuilder.apiMethodResponseSchema(APIObject.refByQualifiedName(responseQN))
        }

        batch.add(methodBuilder.build())
    }

    /**
     * Extract the request body schema from an operation, preferring application/json.
     */
    private fun extractRequestSchema(operation: Operation): Schema<*>? {
        val content = operation.requestBody?.content ?: return null
        return content["application/json"]?.schema
            ?: content["application/xml"]?.schema
            ?: content.values.firstOrNull()?.schema
    }

    /**
     * Extract the response body schema from an API response, preferring application/json.
     */
    private fun extractResponseSchema(response: io.swagger.v3.oas.models.responses.ApiResponse): Schema<*>? {
        val content = response.content ?: return null
        return content["application/json"]?.schema
            ?: content["application/xml"]?.schema
            ?: content.values.firstOrNull()?.schema
    }

    /**
     * Extract the schema name from a $ref string like "#/components/schemas/Pet".
     * Returns null if the schema is inline (no $ref).
     */
    private fun extractRefSchemaName(schema: Schema<*>): String? {
        val ref = schema.`$ref`
        if (ref != null && ref.startsWith("#/components/schemas/")) {
            return ref.removePrefix("#/components/schemas/")
        }
        // Check if this is an array of $ref items
        if (schema.type == "array" && schema.items?.`$ref` != null) {
            val itemRef = schema.items.`$ref`
            if (itemRef.startsWith("#/components/schemas/")) {
                return itemRef.removePrefix("#/components/schemas/")
            }
        }
        return null
    }

    /**
     * Resolve the primary type of a field from its schema.
     */
    private fun resolveFieldType(schema: Schema<*>): String {
        // If it's a $ref, resolve to "object"
        if (schema.`$ref` != null) return "object"
        val type = schema.type ?: "object"
        val format = schema.format
        return if (format != null) "$type/$format" else type
    }

    /**
     * Resolve the secondary type of a field (e.g., for array types, the secondary is "array").
     */
    private fun resolveFieldTypeSecondary(schema: Schema<*>): String? {
        if (schema.type == "array") {
            val itemType = schema.items?.type ?: if (schema.items?.`$ref` != null) "object" else "string"
            return "array"
        }
        return null
    }

    /**
     * Serialize a schema to a JSON string for the blob attributes.
     */
    private fun serializeSchema(schema: Schema<*>): String =
        try {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema)
        } catch (e: Exception) {
            schema.toString()
        }

    /**
     * Add the details of the provided operation to the details captured for the APIPath.
     * (Unchanged from original — preserved for backward compatibility.)
     *
     * @param operation the operation to include (if non-null) as one that exists for the path
     * @param name the name of the operation
     * @param operations the overall list of operations to which to append
     * @param description the overall description of the APIPath to which to append
     */
    fun addOperationDetails(
        operation: Operation?,
        name: String,
        operations: MutableList<String>,
        description: StringBuilder,
    ) {
        if (operation != null) {
            operations.add(name)
            description
                .append("| `")
                .append(name)
                .append("` |")
                .append(operation.summary)
                .append(" |\n")
        }
    }

    /**
     * Utility class for parsing and reading the contents of an OpenAPI spec file,
     * using the Swagger parser.
     */
    class OpenAPISpecReader(
        url: String,
    ) {
        private val spec: OpenAPI

        val sourceURL: String
        val openAPIVersion: String
        val paths: io.swagger.v3.oas.models.Paths?
        val schemas: Map<String, Schema<*>>?
        val title: String
        val description: String
        val termsOfServiceURL: String
        val version: String
        val contactEmail: String
        val contactName: String
        val contactURL: String
        val licenseName: String
        val licenseURL: String
        val externalDocsURL: String
        val externalDocsDescription: String

        init {
            spec = OpenAPIV3Parser().read(url)
            sourceURL = url
            openAPIVersion = spec.openapi
            paths = spec.paths
            schemas = spec.components?.schemas
            title = spec.info?.title ?: ""
            description = spec.info?.description ?: ""
            termsOfServiceURL = spec.info?.termsOfService ?: ""
            version = spec.info?.version ?: ""
            contactEmail = spec.info?.contact?.email ?: ""
            contactName = spec.info?.contact?.name ?: ""
            contactURL = spec.info?.contact?.url ?: ""
            licenseName = spec.info?.license?.name ?: ""
            licenseURL = spec.info?.license?.url ?: ""
            externalDocsURL = spec.externalDocs?.url ?: ""
            externalDocsDescription = spec.externalDocs?.description ?: ""
        }
    }
}
