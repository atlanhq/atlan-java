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
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.parser.OpenAPIV3Parser
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.exitProcess

object OpenAPISpecLoader {
    private val logger = Utils.getLogger(OpenAPISpecLoader.javaClass.name)
    private val jsonMapper = ObjectMapper().apply {
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    /** Object creation mode: create physical per-method instances (default). */
    const val MODE_PHYSICAL = "PHYSICAL"

    /** Object creation mode: skip APIObject/APIField creation entirely (bodies-only). */
    const val MODE_NONE = "NONE"

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

            val objectMode = ctx.config.objectCreationMode?.ifBlank { MODE_PHYSICAL } ?: MODE_PHYSICAL
            logger.info { "Object creation mode: $objectMode" }

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
                processFile(ctx, connectionQN, sourceFile, batchSize, outputDirectory, objectMode)
            }
        }
    }

    private fun processFile(
        ctx: PackageContext<OpenAPISpecLoaderCfg>,
        connectionQN: String,
        sourceFile: String,
        batchSize: Int,
        outputDirectory: String,
        objectMode: String,
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
                loadOpenAPISpec(ctx.client, connectionQN, OpenAPISpecReader(sourceFile), batchSize, objectMode)
            }

            "zip" -> {
                logger.info { "Extracting and processing ZIP file: $sourceFile" }
                val extractedFiles = Utils.unzipFiles(sourceFile, outputDirectory)
                processExtractedFiles(ctx, connectionQN, extractedFiles, batchSize, objectMode)
            }

            else -> {
                // No extension — might be a URL
                if (sourceFile.startsWith("http://") || sourceFile.startsWith("https://")) {
                    logger.info { "Loading OpenAPI specification from URL $sourceFile into: $connectionQN" }
                    loadOpenAPISpec(ctx.client, connectionQN, OpenAPISpecReader(sourceFile), batchSize, objectMode)
                } else {
                    logger.error { "Invalid file type. Please provide a JSON, YAML or ZIP file." }
                    exitProcess(1)
                }
            }
        }
    }

    private fun processExtractedFiles(
        ctx: PackageContext<OpenAPISpecLoaderCfg>,
        connectionQN: String,
        extractedFiles: List<String>,
        batchSize: Int,
        objectMode: String,
    ) {
        extractedFiles.filter { it.endsWith(".json") || it.endsWith(".yaml") || it.endsWith(".yml") }.forEach { file ->
            logger.info { "Loading OpenAPI specification from extracted file: $file" }
            loadOpenAPISpec(ctx.client, connectionQN, OpenAPISpecReader(file), batchSize, objectMode)
        }
    }

    /**
     * Process the OpenAPI spec and create relevant assets in Atlan.
     *
     * @param client connectivity to the Atlan tenant
     * @param connectionQN qualifiedName of the connection in which to create the assets
     * @param spec object for reading from the OpenAPI spec itself
     * @param batchSize maximum number of assets to save per API request
     * @param objectMode how to create APIObject/APIField assets: PHYSICAL (per-body instances) or NONE (skip)
     */
    fun loadOpenAPISpec(
        client: AtlanClient,
        connectionQN: String,
        spec: OpenAPISpecReader,
        batchSize: Int,
        objectMode: String = MODE_PHYSICAL,
    ) {
        // --- Step 1: Save the APISpec with raw content ---
        val specBuilder =
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
        if (spec.rawContent.isNotEmpty()) {
            specBuilder.apiSpecRawContent(spec.rawContent)
        }
        val toCreate = specBuilder.build()
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

        // --- Step 2: Create APIPaths and APIMethod per operation ---
        // In PHYSICAL mode, APIObjects/APIFields are created per-body during method processing.
        // In NONE mode, only paths and methods are created (bodies-only).
        // Collect method-to-object relationship mappings (SDK attribute names don't match Atlas
        // typedef end names, so we create these via the REST API after entity creation)
        val methodRequestSchemaMap = mutableMapOf<String, String>()   // methodQN → requestObjectQN
        val methodResponseSchemaMap = mutableMapOf<String, MutableList<String>>() // methodQN → [responseObjectQNs]

        val totalCount = spec.paths?.size!!.toLong()
        if (totalCount > 0) {
            logger.info { "Creating an APIPath for each path defined within the spec (total: $totalCount)" }
            AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { pathBatch ->
                AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { methodBatch ->
                    AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { objectBatch ->
                        AssetBatch(client, batchSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.MERGE, true).use { fieldBatch ->
                            try {
                                val assetCount = AtomicLong(0)
                                for (apiPath in spec.paths.entries) {
                                    val pathUrl = apiPath.key
                                    val pathDetails = apiPath.value

                                    // --- APIPath creation ---
                                    val operations = mutableListOf<String>()
                                    val desc = StringBuilder()
                                    desc.append("| Method | Summary|\n|---|---|\n")
                                    addOperationDetails(pathDetails.get, "GET", operations, desc)
                                    addOperationDetails(pathDetails.post, "POST", operations, desc)
                                    addOperationDetails(pathDetails.put, "PUT", operations, desc)
                                    addOperationDetails(pathDetails.patch, "PATCH", operations, desc)
                                    addOperationDetails(pathDetails.delete, "DELETE", operations, desc)
                                    // Encode path slashes as ~ so each hierarchy level = 1 QN segment
                                    val encodedPath = pathUrl.replace("/", "~")
                                    val pathQN = "$specQN/$encodedPath"
                                    val path =
                                        APIPath
                                            ._internal()
                                            .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                                            .qualifiedName(pathQN)
                                            .name(pathUrl)
                                            .connectionQualifiedName(connectionQN)
                                            .apiSpec(APISpec.refByQualifiedName(specQN))
                                            .apiSpecQualifiedName(specQN)
                                            .apiPathQualifiedName(pathQN)
                                            .description(desc.toString())
                                            .apiPathRawURI(pathUrl)
                                            .apiPathSummary(pathDetails.summary)
                                            .apiPathAvailableOperations(operations)
                                            .apiPathIsTemplated(pathUrl.contains("{") && pathUrl.contains("}"))
                                            .build()
                                    pathBatch.add(path)

                                    // --- APIMethod creation per operation ---
                                    val methods = listOf("GET" to pathDetails.get, "POST" to pathDetails.post, "PUT" to pathDetails.put, "PATCH" to pathDetails.patch, "DELETE" to pathDetails.delete)
                                    for ((httpMethod, op) in methods) {
                                        createMethodIfPresent(op, httpMethod, pathQN, pathUrl, specQN, connectionQN, spec, objectMode, methodBatch, objectBatch, fieldBatch, methodRequestSchemaMap, methodResponseSchemaMap)
                                    }

                                    Utils.logProgress(assetCount, totalCount, logger, batchSize)
                                }
                                pathBatch.flush()
                                objectBatch.flush()
                                fieldBatch.flush()
                                methodBatch.flush()
                                Utils.logProgress(assetCount, totalCount, logger, batchSize)
                            } catch (e: AtlanException) {
                                logger.error("Unable to bulk-save API paths/methods.", e)
                            }
                        }
                    }
                }
            }
        }

        // --- Step 3: Create method↔object relationships via REST API ---
        // The SDK's relationship attribute names for method-object peer relationships are
        // swapped relative to the Atlas typedef end names, so we create these relationships
        // directly via the Atlas relationship REST API using qualified name references.
        if (objectMode == MODE_PHYSICAL) {
            val baseUrl = client.baseUrl
            val apiToken = System.getenv("ATLAN_API_KEY") ?: ""
            logger.info { "Creating method↔object relationships (${methodRequestSchemaMap.size} request, ${methodResponseSchemaMap.values.sumOf { it.size }} response)..." }

            for ((methodQN, objectQN) in methodRequestSchemaMap) {
                createRelationshipViaApi(
                    baseUrl, apiToken,
                    "api_method_request_schema_api_methods_requesting_this",
                    methodQN, "APIMethod", objectQN, "APIObject",
                )
            }
            for ((methodQN, objectQNs) in methodResponseSchemaMap) {
                for (objectQN in objectQNs) {
                    createRelationshipViaApi(
                        baseUrl, apiToken,
                        "api_method_response_schemas_api_methods_responding_with_this",
                        methodQN, "APIMethod", objectQN, "APIObject",
                    )
                }
            }
            logger.info { "Relationships created." }
        }
    }

    /**
     * Create an APIMethod asset for a single HTTP operation on a path, if the operation exists.
     * In PHYSICAL mode, also creates contextualized APIObject/APIField instances for each body.
     * In NONE mode, only sets the JSON blob attributes on the method (no APIObject/APIField).
     */
    private fun createMethodIfPresent(
        operation: Operation?,
        httpMethod: String,
        pathQN: String,
        pathUrl: String,
        specQN: String,
        connectionQN: String,
        spec: OpenAPISpecReader,
        objectMode: String,
        methodBatch: AssetBatch,
        objectBatch: AssetBatch,
        fieldBatch: AssetBatch,
        methodRequestSchemaMap: MutableMap<String, String>,
        methodResponseSchemaMap: MutableMap<String, MutableList<String>>,
    ) {
        if (operation == null) return

        val methodName = "$httpMethod $pathUrl"
        val methodQN = "$pathQN/$httpMethod"
        val methodContext = "${httpMethod}_${sanitizePath(pathUrl)}"

        val methodBuilder =
            APIMethod
                ._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(methodQN)
                .name(methodName)
                .connectionQualifiedName(connectionQN)
                .apiSpecQualifiedName(specQN)
                .apiPathQualifiedName(pathQN)
                .apiMethodQualifiedName(methodQN)
                .apiSpecName(spec.title)
                .apiSpecType(spec.openAPIVersion)
                .description(operation.summary ?: operation.description ?: "")
                .apiPath(APIPath.refByQualifiedName(pathQN))

        // --- Request body ---
        val pathQNForHierarchy = pathQN
        val requestSchema = extractRequestSchema(operation)
        if (requestSchema != null) {
            val requestExample = generateExample(requestSchema, spec.schemas)
            methodBuilder.apiMethodRequest(
                if (requestExample != null) jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestExample)
                else serializeSchema(requestSchema, spec.schemas)
            )

            if (objectMode == MODE_PHYSICAL) {
                val resolvedSchema = resolveSchema(requestSchema, spec.schemas)
                if (resolvedSchema != null && hasProperties(resolvedSchema)) {
                    // Object schema with properties → full APIObject + child APIFields
                    val schemaName = extractRefSchemaName(requestSchema) ?: "${methodContext}_Request"
                    val physicalName = "${methodContext}/request/$schemaName"
                    val physicalQN = createPhysicalSchemaObject(
                        physicalName, "request", schemaName, resolvedSchema,
                        specQN, connectionQN, pathQNForHierarchy, methodQN, "request",
                        spec, objectBatch, fieldBatch, mutableSetOf(schemaName),
                    )
                    methodRequestSchemaMap[methodQN] = physicalQN
                } else {
                    // Primitive or unresolved schema → shell APIObject (no child fields)
                    val schemaLabel = requestSchema.type ?: "inline"
                    val physicalName = "${methodContext}/request/$schemaLabel"
                    val objectQN = createShellResponseObject(
                        physicalName, "request", "request",
                        specQN, connectionQN, pathQNForHierarchy, methodQN,
                        spec, objectBatch,
                    )
                    methodRequestSchemaMap[methodQN] = objectQN
                }
            }
        }

        // --- Response bodies ---
        val responseCodes = mutableMapOf<String, String>()
        val responseSchemas = mutableListOf<String>()
        val allResponseBlobs = mutableMapOf<String, Any>()
        if (operation.responses != null) {
            for ((statusCode, apiResponse) in operation.responses) {
                val description = apiResponse.description ?: ""
                val responseSchema = extractResponseSchema(apiResponse)
                val bodyType = "response/$statusCode"
                val bodyDisplayName = "response $statusCode"

                if (responseSchema != null) {
                    val blobEntry = mutableMapOf<String, Any>()
                    blobEntry["description"] = description
                    val responseExample = generateExample(responseSchema, spec.schemas)
                    if (responseExample != null) {
                        blobEntry["example"] = responseExample
                    }

                    if (objectMode == MODE_PHYSICAL) {
                        val resolvedSchema = resolveSchema(responseSchema, spec.schemas)
                        if (resolvedSchema != null && hasProperties(resolvedSchema)) {
                            // Object schema with properties → full APIObject + child APIFields
                            val schemaName = extractRefSchemaName(responseSchema) ?: "${methodContext}_${statusCode}_Response"
                            val physicalName = "${methodContext}/response/${statusCode}/$schemaName"
                            val physicalQN = createPhysicalSchemaObject(
                                physicalName, bodyDisplayName, schemaName, resolvedSchema,
                                specQN, connectionQN, pathQNForHierarchy, methodQN, bodyType,
                                spec, objectBatch, fieldBatch, mutableSetOf(schemaName),
                            )
                            responseCodes[statusCode] = physicalQN
                            responseSchemas.add(physicalQN)
                        } else {
                            // Primitive or unresolved schema → shell APIObject (no child fields)
                            val schemaLabel = responseSchema.type ?: "inline"
                            val physicalName = "${methodContext}/response/${statusCode}/$schemaLabel"
                            val objectQN = createShellResponseObject(
                                physicalName, bodyDisplayName, bodyType,
                                specQN, connectionQN, pathQNForHierarchy, methodQN,
                                spec, objectBatch,
                            )
                            responseCodes[statusCode] = objectQN
                            responseSchemas.add(objectQN)
                        }
                    } else {
                        // NONE mode — no objects, just record a label
                        val inlineName = "${methodContext}_$statusCode"
                        responseCodes[statusCode] = inlineName
                    }
                    allResponseBlobs[statusCode] = blobEntry
                } else {
                    // No content block — description-only response
                    allResponseBlobs[statusCode] = mapOf("description" to description)

                    if (objectMode == MODE_PHYSICAL) {
                        // Still create a shell APIObject so it appears in the hierarchy
                        val physicalName = "${methodContext}/response/${statusCode}/no_content"
                        val objectQN = createShellResponseObject(
                            physicalName, bodyDisplayName, bodyType,
                            specQN, connectionQN, pathQNForHierarchy, methodQN,
                            spec, objectBatch,
                        )
                        responseCodes[statusCode] = objectQN
                        responseSchemas.add(objectQN)
                    }
                }
            }
        }
        if (allResponseBlobs.isNotEmpty()) {
            methodBuilder.apiMethodResponse(jsonMapper.writeValueAsString(allResponseBlobs))
        }
        if (responseCodes.isNotEmpty()) {
            methodBuilder.apiMethodResponseCodes(responseCodes)
        }
        // Collect response schema relationships (created via REST API after batch flush)
        if (responseSchemas.isNotEmpty()) {
            methodResponseSchemaMap.getOrPut(methodQN) { mutableListOf() }.addAll(responseSchemas)
        }

        methodBatch.add(methodBuilder.build())
    }

    /**
     * Create a physical (per-body) APIObject and its APIFields for a schema occurrence,
     * recursing into nested $ref sub-objects. Names use xpath-style paths (e.g., "Pet/category/name").
     *
     * @param contextPath QN path fragment like "POST_pet/request/Pet" that makes this instance unique
     * @param displayName xpath-style display name for the object (e.g., "Pet" or "Pet/category")
     * @param rootSchemaName the top-level schema name, used as prefix for all child names
     * @param schema the resolved schema with properties
     * @param specQN qualified name of the parent APISpec
     * @param connectionQN qualified name of the connection
     * @param pathQN qualified name of the parent APIPath (for hierarchy filters)
     * @param methodQN qualified name of the parent APIMethod (for hierarchy filters)
     * @param bodyType body context: "request", "response/200", etc. (for body filter)
     * @param spec the OpenAPISpecReader for spec-level metadata
     * @param objectBatch the AssetBatch for APIObjects
     * @param fieldBatch the AssetBatch for APIFields
     * @param visitedSchemas set of schema names visited in the current recursion path (cycle detection)
     * @return the qualified name of the created APIObject
     */
    private fun createPhysicalSchemaObject(
        contextPath: String,
        displayName: String,
        rootSchemaName: String,
        schema: Schema<*>,
        specQN: String,
        connectionQN: String,
        pathQN: String,
        methodQN: String,
        bodyType: String,
        spec: OpenAPISpecReader,
        objectBatch: AssetBatch,
        fieldBatch: AssetBatch,
        visitedSchemas: MutableSet<String>,
    ): String {
        val objectQN = "$specQN/schemas/$contextPath"
        val properties = schema.properties ?: emptyMap()
        val apiObject =
            APIObject
                ._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(objectQN)
                .name(displayName)
                .connectionQualifiedName(connectionQN)
                .apiSpecQualifiedName(specQN)
                .apiSpecName(spec.title)
                .apiSpecType(spec.openAPIVersion)
                .apiFieldCount(properties.size.toLong())
                .apiPathQualifiedName(pathQN)
                .apiMethodQualifiedName(methodQN)
                .apiBodyType(bodyType)
                .build()
        objectBatch.add(apiObject)

        for ((fieldName, fieldSchema) in properties) {
            val isArray = fieldSchema.type == "array"
            val arraySuffix = if (isArray) "[]" else ""
            val fieldDisplayName = "$displayName/$fieldName$arraySuffix"
            val fieldQN = "$objectQN/$fieldName$arraySuffix"

            val refSchemaName = extractRefSchemaName(fieldSchema)
            val isObjectRef = refSchemaName != null
            val fieldType = resolveFieldType(fieldSchema)
            val fieldTypeSecondary = resolveFieldTypeSecondary(fieldSchema)
            val fieldBuilder =
                APIField
                    ._internal()
                    .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                    .qualifiedName(fieldQN)
                    .name(fieldDisplayName)
                    .connectionQualifiedName(connectionQN)
                    .apiSpecQualifiedName(specQN)
                    .apiSpecName(spec.title)
                    .apiSpecType(spec.openAPIVersion)
                    .apiFieldType(fieldType)
                    .apiObject(APIObject.refByQualifiedName(objectQN))
                    .apiPathQualifiedName(pathQN)
                    .apiMethodQualifiedName(methodQN)
                    .apiBodyType(bodyType)
            if (fieldTypeSecondary != null) {
                fieldBuilder.apiFieldTypeSecondary(fieldTypeSecondary)
            }
            // Set description from the spec definition (always set to clear old values)
            fieldBuilder.description(fieldSchema.description ?: "")

            if (isObjectRef) {
                fieldBuilder.apiIsObjectReference(true)

                // Recurse into sub-objects if not a cycle
                if (refSchemaName != null && refSchemaName !in visitedSchemas) {
                    val resolvedSubSchema = resolveSchema(fieldSchema, spec.schemas)
                    if (resolvedSubSchema != null && hasProperties(resolvedSubSchema)) {
                        visitedSchemas.add(refSchemaName)
                        val childContextPath = "$contextPath/$fieldName$arraySuffix"
                        val childQN = createPhysicalSchemaObject(
                            childContextPath, fieldDisplayName, rootSchemaName, resolvedSubSchema,
                            specQN, connectionQN, pathQN, methodQN, bodyType,
                            spec, objectBatch, fieldBatch, visitedSchemas,
                        )
                        fieldBuilder.apiObjectQualifiedName(childQN)
                        visitedSchemas.remove(refSchemaName)
                    }
                }
            }
            fieldBatch.add(fieldBuilder.build())
        }
        return objectQN
    }

    /**
     * Create a shell APIObject for a response that has no object-type schema
     * (primitive type, no content, or unresolved). This ensures every defined
     * response appears in the hierarchy as a governable body entity.
     *
     * @return the qualified name of the created APIObject
     */
    private fun createShellResponseObject(
        contextPath: String,
        displayName: String,
        bodyType: String,
        specQN: String,
        connectionQN: String,
        pathQN: String,
        methodQN: String,
        spec: OpenAPISpecReader,
        objectBatch: AssetBatch,
    ): String {
        val objectQN = "$specQN/schemas/$contextPath"
        val apiObject =
            APIObject
                ._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(objectQN)
                .name(displayName)
                .connectionQualifiedName(connectionQN)
                .apiSpecQualifiedName(specQN)
                .apiSpecName(spec.title)
                .apiSpecType(spec.openAPIVersion)
                .apiFieldCount(0L)
                .apiPathQualifiedName(pathQN)
                .apiMethodQualifiedName(methodQN)
                .apiBodyType(bodyType)
                .build()
        objectBatch.add(apiObject)
        return objectQN
    }

    /**
     * Resolve a schema that may be a $ref to its full definition from components/schemas.
     * Returns null if the schema cannot be resolved.
     */
    private fun resolveSchema(
        schema: Schema<*>,
        schemas: Map<String, Schema<*>>?,
    ): Schema<*>? {
        val refName = extractRefSchemaName(schema)
        if (refName != null) {
            return schemas?.get(refName)
        }
        // If it's an array with $ref items, resolve the item schema
        if (schema.type == "array" && schema.items != null) {
            val itemRefName = extractRefSchemaName(schema.items)
            if (itemRefName != null) {
                return schemas?.get(itemRefName)
            }
        }
        // Inline schema — return it directly if it has properties
        return schema
    }

    /**
     * Check whether a schema has properties worth creating an APIObject for.
     */
    private fun hasProperties(schema: Schema<*>): Boolean =
        schema.properties != null && schema.properties.isNotEmpty()

    /**
     * Sanitize a path URL for use in synthetic schema names.
     */
    private fun sanitizePath(pathUrl: String): String =
        pathUrl
            .replace("/", "_")
            .replace("{", "")
            .replace("}", "")
            .trimStart('_')

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
        if (schema.`$ref` != null) return "object"
        return schema.type ?: "object"
    }

    /**
     * Resolve the secondary type of a field (e.g., for array types, the secondary is "array").
     */
    private fun resolveFieldTypeSecondary(schema: Schema<*>): String? {
        if (schema.type == "array") {
            return "array"
        }
        return null
    }

    /**
     * Serialize a schema to a JSON string for the blob attributes.
     * Resolves $ref references and omits null values for cleaner output.
     */
    private fun serializeSchema(schema: Schema<*>, schemas: Map<String, Schema<*>>? = null): String =
        try {
            val resolved = if (schemas != null) resolveSchema(schema, schemas) ?: schema else schema
            jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resolved)
        } catch (e: Exception) {
            schema.toString()
        }

    /**
     * Generate an example payload from a schema definition.
     * Uses the schema's `example` field when available, otherwise generates
     * a representative value based on type and format.
     * Arrays are limited to 1 item (or minItems if specified).
     */
    private fun generateExample(
        schema: Schema<*>,
        schemas: Map<String, Schema<*>>?,
        visited: MutableSet<String> = mutableSetOf(),
    ): Any? {
        // Resolve $ref
        val refName = extractRefSchemaName(schema)
        if (refName != null) {
            if (refName in visited) return emptyMap<String, Any>() // cycle guard
            visited.add(refName)
            val resolved = schemas?.get(refName)
            val result = if (resolved != null) generateExample(resolved, schemas, visited) else null
            visited.remove(refName)
            return result
        }

        // Schema-level example takes priority
        if (schema.example != null) return schema.example

        // Array → generate 1 item (or minItems)
        if (schema.type == "array" && schema.items != null) {
            val itemExample = generateExample(schema.items, schemas, visited)
            val count = schema.minItems ?: 1
            return (1..count).map { itemExample }
        }

        // Object with properties
        if (schema.properties != null && schema.properties.isNotEmpty()) {
            val obj = linkedMapOf<String, Any?>()
            for ((name, propSchema) in schema.properties) {
                obj[name] = generateExample(propSchema, schemas, visited)
            }
            return obj
        }

        // Primitive
        return generatePrimitiveExample(schema)
    }

    /**
     * Generate a representative primitive value based on type and format.
     */
    private fun generatePrimitiveExample(schema: Schema<*>): Any {
        if (schema.example != null) return schema.example

        // Enum: pick first value
        if (schema.enum != null && schema.enum.isNotEmpty()) return schema.enum[0]

        return when (schema.type) {
            "string" -> when (schema.format) {
                "date-time" -> "2024-01-15T09:30:00Z"
                "date" -> "2024-01-15"
                "email" -> "user@example.com"
                "uri", "url" -> "https://example.com"
                "uuid" -> "550e8400-e29b-41d4-a716-446655440000"
                "byte" -> "dGVzdA=="
                "binary" -> "<binary>"
                "password" -> "********"
                else -> "string"
            }
            "integer", "int" -> when (schema.format) {
                "int64" -> 100000L
                else -> 10
            }
            "number" -> when (schema.format) {
                "float" -> 3.14
                "double" -> 3.14159
                else -> 1.5
            }
            "boolean" -> true
            else -> "value"
        }
    }

    /**
     * Create a relationship between two entities via the Atlas REST API.
     * This bypasses the SDK's serialization which has swapped attribute names
     * for the method↔object peer relationships.
     */
    private fun createRelationshipViaApi(
        baseUrl: String,
        apiToken: String,
        relationshipTypeName: String,
        end1QN: String,
        end1Type: String,
        end2QN: String,
        end2Type: String,
    ) {
        try {
            val payload = """
                {
                    "typeName": "$relationshipTypeName",
                    "end1": {
                        "typeName": "$end1Type",
                        "uniqueAttributes": { "qualifiedName": "$end1QN" }
                    },
                    "end2": {
                        "typeName": "$end2Type",
                        "uniqueAttributes": { "qualifiedName": "$end2QN" }
                    }
                }
            """.trimIndent()
            val url = URL("$baseUrl/api/meta/relationship")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $apiToken")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            OutputStreamWriter(conn.outputStream).use { it.write(payload) }
            val responseCode = conn.responseCode
            if (responseCode !in 200..299) {
                val error = conn.errorStream?.bufferedReader()?.readText() ?: ""
                logger.warn { "Failed to create relationship $relationshipTypeName ($end1QN → $end2QN): HTTP $responseCode — $error" }
            }
            conn.disconnect()
        } catch (e: Exception) {
            logger.warn { "Error creating relationship $relationshipTypeName ($end1QN → $end2QN): ${e.message}" }
        }
    }

    /**
     * Add the details of the provided operation to the details captured for the APIPath.
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
     * using the Swagger parser. Also captures the raw file content for storage.
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
        val rawContent: String

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
            // Capture raw content of the spec file
            rawContent = try {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    URL(url).readText()
                } else {
                    Files.readString(Paths.get(url))
                }
            } catch (e: Exception) {
                logger.warn { "Could not read raw spec content from $url: ${e.message}" }
                ""
            }
        }
    }
}
