/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag
import AssetImportCfg
import LakeFormationTagSyncCfg
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.csv.ImportResults
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import java.io.File
import kotlin.system.exitProcess

private const val TAG_FILE_NAME_PREFIX = "lftag_association"

/**
 * Actually run the migrator, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object LakeTagSynchronizer {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<LakeFormationTagSyncCfg>()
        val failOnErrors = Utils.getOrDefault(config.failOnErrors, true)
        val results = sync(config, outputDirectory, failOnErrors)
        if (!results && failOnErrors) {
            logger.error { "Some errors detected, failing the workflow." }
            exitProcess(1)
        }
    }

    private fun sync(config: LakeFormationTagSyncCfg, outputDirectory: String, failOnErrors: Boolean): Boolean {
        val importType = Utils.getOrDefault(config.importType, "")
        val assetPrefix = Utils.getOrDefault(config.assetsPrefix, "")
        val batchSize = Utils.getOrDefault(config.batchSize, 20)
        var results: ImportResults? = null

        val mapper = jacksonObjectMapper()

        if (importType != "CLOUD") {
            logger.error { "Direct file upload(s) are not supported at this time." }
            return false
        }
        val files = Utils.getInputFiles(
            "",
            outputDirectory,
            false,
            assetPrefix,
        )
        val tagFileNames = mutableListOf<String>()
        val connectionMap = mutableMapOf<String, String>()
        val metadataMap = mutableMapOf<String, String>()
        files.forEach { fileName ->
            when (fileName) {
                "$outputDirectory/connection_map.json" -> {
                    val jsonString: String = File(fileName).readText(Charsets.UTF_8)
                    connectionMap += mapper.readValue<Map<String, String>>(jsonString)
                }

                "$outputDirectory/metadata_map.json" -> {
                    val jsonString: String = File(fileName).readText(Charsets.UTF_8)
                    metadataMap += mapper.readValue<Map<String, String>>(jsonString)
                }
                else -> {
                    if (File(fileName).nameWithoutExtension.startsWith(TAG_FILE_NAME_PREFIX)) {
                        tagFileNames.add(fileName)
                    } else {
                        logger.warn { "Skipping $fileName because it doesn't start with $TAG_FILE_NAME_PREFIX." }
                    }
                }
            }
        }
        if (connectionMap.isEmpty()) {
            logger.error { "The file connection_map.json must be provided." }
            return false
        }
        if (metadataMap.isEmpty()) {
            logger.error { "The file metadata_map.json must be provided." }
            return false
        }
        if (tagFileNames.isEmpty()) {
            logger.error { "You must provide at least one json file containing Lake Tag data." }
            return false
        }
        val csvProducer = CSVProducer(connectionMap, metadataMap, outputDirectory)
        tagFileNames.forEach { tagFileName ->
            val csvFileName = tagFileName.replace("$outputDirectory/", "").replace(".json", ".csv")
            csvProducer.transform(tagFileName, csvFileName)
            val importConfig = AssetImportCfg(
                assetsFile = "$outputDirectory/$csvFileName",
                assetsUpsertSemantic = "update",
                assetsFailOnErrors = failOnErrors,
                assetsBatchSize = batchSize,
                assetsFieldSeparator = ",",
            )
            val result = Importer.import(importConfig, outputDirectory)?.combinedWith(results)
            if (result != null) {
                results = result.combinedWith(result)
            }
        }
        return !(results?.anyFailures ?: false)
    }
}
