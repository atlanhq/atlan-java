/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import AssetImportCfg
import LineageBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.LineageProcess
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVXformer.Companion.getHeader
import com.atlan.util.AssetBatch.AssetCreationHandling
import mu.KotlinLogging
import java.io.File
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Loader {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<LineageBuilderCfg>()
        import(config, outputDirectory)
    }

    fun import(config: LineageBuilderCfg, outputDirectory: String = "tmp") {
        val batchSize = Utils.getOrDefault(config.batchSize, 20)
        val fieldSeparator = Utils.getOrDefault(config.fieldSeparator, ",")[0]
        val defaultRegion = Utils.getEnvVar("AWS_S3_REGION")
        val defaultBucket = Utils.getEnvVar("AWS_S3_BUCKET_NAME")
        val lineageUpload = Utils.getOrDefault(config.lineageImportType, "UPLOAD") == "UPLOAD"
        val lineageFilename = Utils.getOrDefault(config.lineageFile, "")
        val lineageS3Region = Utils.getOrDefault(config.lineageS3Region, defaultRegion)
        val lineageS3Bucket = Utils.getOrDefault(config.lineageS3Bucket, defaultBucket)
        val lineageS3ObjectKey = Utils.getOrDefault(config.lineageS3ObjectKey, "")
        val lineageFailOnErrors = Utils.getOrDefault(config.lineageFailOnErrors, true)
        val lineageAssetSemantic = Utils.getOrDefault(config.lineageUpsertSemantic, "partial")
        val lineageCaseSensitive = Utils.getOrDefault(config.lineageCaseSensitive, true)

        val lineageFileProvided = (lineageUpload && lineageFilename.isNotBlank()) || (!lineageUpload && lineageS3ObjectKey.isNotBlank())
        if (!lineageFileProvided) {
            logger.error { "No input file was provided for lineage." }
            exitProcess(1)
        }

        val lineageInput = Utils.getInputFile(
            lineageFilename,
            lineageS3Region,
            lineageS3Bucket,
            lineageS3ObjectKey,
            outputDirectory,
            lineageUpload,
        )
        if (lineageInput.isNotBlank()) {
            FieldSerde.FAIL_ON_ERRORS.set(lineageFailOnErrors)
            val connectionMap = preloadConnectionMap()

            val creationHandling = when (lineageAssetSemantic) {
                "update" -> AssetCreationHandling.NONE
                "partial" -> AssetCreationHandling.PARTIAL
                else -> AssetCreationHandling.FULL
            }
            val ctx = Context(
                connectionMap = connectionMap,
                assetSemantic = creationHandling,
            )

            // 1. Transform the assets, so we can load the prior to creating any lineage relationships
            logger.info { "=== Processing assets... ===" }
            val assetsFile = "$outputDirectory${File.separator}CSA_LB_assets.csv"
            val assetXform = AssetTransformer(
                ctx,
                lineageInput,
                logger,
                fieldSeparator,
            )
            assetXform.transform(assetsFile)

            // 2. Create the assets
            val importConfig = AssetImportCfg(
                assetsFile = assetsFile,
                assetsUpsertSemantic = lineageAssetSemantic,
                assetsFailOnErrors = lineageFailOnErrors,
                assetsCaseSensitive = lineageCaseSensitive,
                assetsBatchSize = batchSize,
                assetsFieldSeparator = fieldSeparator.toString(),
            )
            val assetResults = Importer.import(importConfig, outputDirectory)

            val qualifiedNameMap = assetResults?.primary?.qualifiedNames ?: mapOf()

            // 3. Transform the lineage, only keeping any rows that have both input and output assets in Atlan
            logger.info { "=== Processing lineage... ===" }
            val lineageHeaders = mutableListOf(
                Asset.TYPE_NAME.atlanFieldName,
                Asset.QUALIFIED_NAME.atlanFieldName,
                Asset.NAME.atlanFieldName,
                Asset.CONNECTION_QUALIFIED_NAME.atlanFieldName,
                "connectorType",
                LineageProcess.INPUTS.atlanFieldName,
                LineageProcess.OUTPUTS.atlanFieldName,
            )
            val lineageFile = "$outputDirectory${File.separator}CSA_LB_lineage.csv"
            // Determine any non-standard lineage fields in the header and append them to the end of
            // the list of standard header fields, so they're passed-through to be used as part of
            // defining the lineage process itself
            val inputHeaders = getHeader(lineageInput).toMutableList()
            inputHeaders.removeAll(AssetTransformer.INPUT_HEADERS)
            inputHeaders.removeAll(LineageTransformer.INPUT_HEADERS)
            inputHeaders.forEach { lineageHeaders.add(it) }
            val lineageXform = LineageTransformer(
                ctx,
                lineageInput,
                lineageHeaders,
                qualifiedNameMap,
                logger,
                fieldSeparator,
            )
            lineageXform.transform(lineageFile)

            // 4. Load the lineage processes
            val lineageConfig = AssetImportCfg(
                assetsFile = lineageFile,
                assetsUpsertSemantic = "upsert", // Note that for these we want a full, not partial, create
                assetsFailOnErrors = lineageFailOnErrors,
                assetsCaseSensitive = lineageCaseSensitive,
                assetsBatchSize = batchSize,
                assetsFieldSeparator = fieldSeparator.toString(),
            )
            Importer.import(lineageConfig, outputDirectory)
        }
    }

    data class Context(
        val connectionMap: Map<ConnectionId, String>,
        val assetSemantic: AssetCreationHandling,
    )
    data class ConnectionId(
        val type: String,
        val name: String,
    )

    private fun preloadConnectionMap(): Map<ConnectionId, String> {
        val map = mutableMapOf<ConnectionId, String>()
        Connection.select()
            .pageSize(50)
            .includeOnResults(Connection.CONNECTOR_TYPE)
            .stream()
            .forEach {
                if (it.connectorType != null) {
                    map[ConnectionId(it.connectorType.value, it.name)] = it.qualifiedName
                }
            }
        return map.toMap()
    }
}
