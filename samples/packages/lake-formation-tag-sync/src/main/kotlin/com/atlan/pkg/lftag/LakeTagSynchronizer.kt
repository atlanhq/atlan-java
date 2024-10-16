/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import AssetImportCfg
import LakeFormationTagSyncCfg
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.lftag.model.LFTagData
import com.atlan.pkg.serde.csv.ImportResults
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import java.io.File
import kotlin.system.exitProcess

/**
 * Actually run the migrator, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object LakeTagSynchronizer {
    private val logger = KotlinLogging.logger {}

    const val CONNECTION_MAP_JSON = "connection_map.json"
    const val METADATA_MAP_JSON = "metadata_map.json"
    const val TAG_FILE_NAME_PREFIX = "lftag_association"
    const val OUTPUT_SUBDIR = "s3"

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<LakeFormationTagSyncCfg>()
        val failOnErrors = Utils.getOrDefault(config.failOnErrors, true)
        val removeSchema = Utils.getOrDefault(config.removeSchema, false)
        val results = sync(config, outputDirectory, failOnErrors, removeSchema)
        if (!results && failOnErrors) {
            logger.error { "Some errors detected, failing the workflow." }
            exitProcess(1)
        }
    }

    private fun sync(
        config: LakeFormationTagSyncCfg,
        outputDirectory: String,
        failOnErrors: Boolean,
        removeSchema: Boolean,
    ): Boolean {
        val skipObjectStore = Utils.getOrDefault(config.importType, "CLOUD") == "DIRECT"
        val batchSize = Utils.getOrDefault(config.batchSize, 20)
        var combinedResults: ImportResults? = null

        val mapper = jacksonObjectMapper()

        val files =
            Utils.getInputFiles(
                "$outputDirectory/$OUTPUT_SUBDIR",
                outputDirectory,
                skipObjectStore,
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
            val csvFileName = "$outputDirectory${File.separator}${File(tagFileName).nameWithoutExtension}.csv"
            val lfTagData = createMissingEnums(tagFileName, mapper, metadataMap)
            csvProducer.transform(lfTagData, csvFileName, removeSchema)
            val importConfig =
                AssetImportCfg(
                    assetsFile = csvFileName,
                    assetsUpsertSemantic = "update",
                    assetsFailOnErrors = failOnErrors,
                    assetsBatchSize = batchSize,
                    assetsFieldSeparator = ",",
                )
            val result = Importer.import(importConfig, outputDirectory)
            combinedResults = combinedResults?.combinedWith(result) ?: result
        }
        return !(combinedResults?.anyFailures ?: false)
    }

    private fun createMissingEnums(
        tagFileName: String,
        mapper: ObjectMapper,
        metadataMap: MutableMap<String, String>,
    ): LFTagData {
        val jsonString: String = File(tagFileName).readText(Charsets.UTF_8)
        val tagData = mapper.readValue(jsonString, LFTagData::class.java)
        val tagToMetadataMapper = TagToMetadataMapper(metadataMap)
        val enumCreator = EnumCreator(tagToMetadataMapper)
        tagData.tagValuesByTagKey.forEach { entry ->
            enumCreator.createOptions(entry.key, entry.value)
        }
        return tagData
    }
}
