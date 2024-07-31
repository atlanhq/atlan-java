/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.exception.AtlanException
import com.atlan.model.assets.APIPath
import com.atlan.model.assets.APISpec
import com.atlan.model.core.AssetMutationResponse
import com.atlan.pkg.Utils
import com.atlan.util.AssetBatch
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.parser.OpenAPIV3Parser
import mu.KotlinLogging
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.exitProcess

object OpenAPISpecLoader {

    private val logger = KotlinLogging.logger {}

    /**
     * Actually run the loader, taking all settings from environment variables.
     * Note: all parameters should be passed through environment variables.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val config = Utils.setPackageOps<OpenAPISpecLoaderCfg>()

        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val importType = Utils.getOrDefault(config.importType, "URL")
        val specUrl = Utils.getOrDefault(config.specUrl, "")
        val specFilename = Utils.getOrDefault(config.specFile, "")
        val specKey = Utils.getOrDefault(config.specKey, "")
        val batchSize = 20

        val inputQN = config.connectionQualifiedName?.let {
            if (it.isNotEmpty()) it[0] else null
        }
        val connectionQN =
            Utils.createOrReuseConnection(config.connectionUsage, inputQN, config.connection)

        val specFileProvided = (importType == "DIRECT" && specFilename.isNotBlank()) || (importType == "CLOUD" && specKey.isNotBlank()) || (importType == "URL" && specUrl.isNotBlank())
        if (!specFileProvided) {
            logger.error { "No input file was provided for the OpenAPI spec." }
            exitProcess(1)
        }

        if (connectionQN.isBlank()) {
            logger.error { "Missing required parameter - you must provide BOTH a connection name and specification URL." }
            exitProcess(4)
        }

        val sourceUrl = when (importType) {
            "CLOUD" -> {
                Utils.getInputFile(
                    specFilename,
                    outputDirectory,
                    false,
                    Utils.getOrDefault(config.specPrefix, ""),
                    specKey,
                )
            }
            "DIRECT" -> specFilename
            else -> specUrl
        }

        logger.info { "Loading OpenAPI specification from $sourceUrl into: $connectionQN" }
        loadOpenAPISpec(connectionQN, OpenAPISpecReader(sourceUrl), batchSize)
    }

    /**
     * Process the OpenAPI spec and create relevant assets in Atlan.
     *
     * @param connectionQN qualifiedName of the connection in which to create the assets
     * @param spec object for reading from the OpenAPI spec itself
     * @param batchSize maximum number of assets to save per API request
     */
    fun loadOpenAPISpec(connectionQN: String, spec: OpenAPISpecReader, batchSize: Int) {
        val toCreate = APISpec.creator(spec.title, connectionQN)
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
            val response = toCreate.save()
            val mutation = response.getMutation(toCreate)
            if (mutation in listOf(
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
        val batch =
            AssetBatch(Atlan.getDefaultClient(), batchSize, false, AssetBatch.CustomMetadataHandling.MERGE, true)
        val totalCount = spec.paths?.size!!.toLong()
        if (totalCount > 0) {
            logger.info { "Creating an APIPath for each path defined within the spec (total: $totalCount)" }
            try {
                val assetCount = AtomicLong(0)
                for (apiPath in spec.paths.entries) {
                    val pathUrl = apiPath.key
                    val pathDetails = apiPath.value
                    val operations = mutableListOf<String>()
                    val desc = StringBuilder()
                    desc.append("| Method | Summary|\n|---|---|\n")
                    addOperationDetails(pathDetails.get, "GET", operations, desc)
                    addOperationDetails(pathDetails.post, "POST", operations, desc)
                    addOperationDetails(pathDetails.put, "PUT", operations, desc)
                    addOperationDetails(pathDetails.patch, "PATCH", operations, desc)
                    addOperationDetails(pathDetails.delete, "DELETE", operations, desc)
                    val path = APIPath.creator(pathUrl, specQN)
                        .description(desc.toString())
                        .apiPathRawURI(pathUrl)
                        .apiPathSummary(pathDetails.summary)
                        .apiPathAvailableOperations(operations)
                        .apiPathIsTemplated(pathUrl.contains("{") && pathUrl.contains("}"))
                        .build()
                    batch.add(path)
                    Utils.logProgress(assetCount, totalCount, logger, batchSize)
                }
                batch.flush()
                Utils.logProgress(assetCount, totalCount, logger, batchSize)
            } catch (e: AtlanException) {
                logger.error("Unable to bulk-save API paths.", e)
            }
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
            description.append("| `").append(name).append("` |").append(operation.summary).append(" |\n")
        }
    }

    /**
     * Utility class for parsing and reading the contents of an OpenAPI spec file,
     * using the Swagger parser.
     */
    class OpenAPISpecReader(url: String) {

        private val spec: OpenAPI

        val sourceURL: String
        val openAPIVersion: String
        val paths: Paths?
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
