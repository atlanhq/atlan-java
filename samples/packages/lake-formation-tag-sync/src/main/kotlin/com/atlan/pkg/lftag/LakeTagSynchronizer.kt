/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import AssetImportCfg
import LakeFormationTagSyncCfg
import com.atlan.AtlanClient
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.lftag.model.LFTagData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import kotlin.system.exitProcess

/**
 * Actually run the migrator, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object LakeTagSynchronizer {
    private val logger = Utils.getLogger(LakeTagSynchronizer.javaClass.name)

    const val CONNECTION_MAP_JSON = "connection_map.json"
    const val METADATA_MAP_JSON = "metadata_map.json"
    const val TAG_FILE_NAME_PREFIX = "lftag_association"
    const val OUTPUT_SUBDIR = "s3"

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info { "Starting Lake Tag Synchronization" }
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<LakeFormationTagSyncCfg>().use { ctx ->
            val results = sync(ctx, outputDirectory)
            if (!results && ctx.config.failOnErrors) {
                logger.error { "Some errors detected, failing the workflow." }
                exitProcess(1)
            }
        }
    }

    private fun sync(
        ctx: PackageContext<LakeFormationTagSyncCfg>,
        outputDirectory: String,
    ): Boolean {
        var anyFailure = false

        val mapper = jacksonObjectMapper()

        val files =
            Utils.getInputFiles(
                "$outputDirectory/$OUTPUT_SUBDIR",
                outputDirectory,
                ctx.config.importType == "DIRECT",
                "",
            )
        val tagFileNames = mutableListOf<String>()
        val connectionMap = mutableMapOf<String, String>()
        val metadataMap = mutableMapOf<String, String>()
        files.forEach { fileName ->
            val nameWithExt = File(fileName).name
            when {
                nameWithExt == CONNECTION_MAP_JSON -> connectionMap += mapper.readValue<Map<String, String>>(File(fileName))
                nameWithExt == METADATA_MAP_JSON -> metadataMap += mapper.readValue<Map<String, String>>(File(fileName))
                nameWithExt.startsWith(TAG_FILE_NAME_PREFIX) -> tagFileNames.add(fileName)
                else -> logger.warn { "Skipping $fileName because it doesn't start with $TAG_FILE_NAME_PREFIX." }
            }
        }
        if (connectionMap.isEmpty()) {
            logger.error { "The file $CONNECTION_MAP_JSON must be provided." }
            return false
        }
        if (metadataMap.isEmpty()) {
            logger.error { "The file $METADATA_MAP_JSON must be provided." }
            return false
        }
        if (tagFileNames.isEmpty()) {
            logger.error { "You must provide at least one json file containing Lake Tag data (name starting with $TAG_FILE_NAME_PREFIX)." }
            return false
        }
        val csvProducer = CSVProducer(connectionMap, metadataMap)
        tagFileNames.forEach { tagFileName ->
            logger.info { "Processing $tagFileName." }
            val csvFileName = "$outputDirectory${File.separator}${File(tagFileName).nameWithoutExtension}.csv"
            val lfTagData = createMissingEnums(ctx.client, tagFileName, mapper, metadataMap)
            csvProducer.transform(lfTagData, csvFileName, ctx.config.removeSchema)
            val importConfig =
                AssetImportCfg(
                    assetsFile = csvFileName,
                    assetsUpsertSemantic = "update",
                    assetsFailOnErrors = ctx.config.failOnErrors,
                    assetsBatchSize = ctx.config.batchSize,
                    assetsFieldSeparator = ",",
                )
            Utils.initializeContext(importConfig, ctx).use { iCtx ->
                val result = Importer.import(iCtx, outputDirectory)
                anyFailure = anyFailure || result?.anyFailures ?: false
                result?.close() // Clean up the results if we won't use them
            }
        }
        return !anyFailure
    }

    private fun createMissingEnums(
        client: AtlanClient,
        tagFileName: String,
        mapper: ObjectMapper,
        metadataMap: MutableMap<String, String>,
    ): LFTagData {
        val jsonString: String = File(tagFileName).readText(Charsets.UTF_8)
        val tagData = mapper.readValue(jsonString, LFTagData::class.java)
        val tagToMetadataMapper = TagToMetadataMapper(metadataMap)
        val enumCreator = EnumCreator(client, tagToMetadataMapper)
        tagData.tagValuesByTagKey.forEach { entry ->
            enumCreator.createOptions(entry.key, entry.value)
        }
        return tagData
    }
}
