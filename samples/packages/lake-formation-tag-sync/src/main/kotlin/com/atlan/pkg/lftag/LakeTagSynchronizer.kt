/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag
import LakeFormationTagSyncCfg
import com.atlan.pkg.Utils
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

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<LakeFormationTagSyncCfg>()
        val importType = Utils.getOrDefault(config.importType, "")
        val cloudDetails = Utils.getOrDefault(config.cloudSource, "")
        val assetPrefix = Utils.getOrDefault(config.assetsPrefix, "")
        val batchSize = Utils.getOrDefault(config.batchSize, 20)
        val failOnErrors = Utils.getOrDefault(config.failOnErrors, true)
        val results = Results(false)

        val mapper = jacksonObjectMapper()

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
                    tagFileNames.add(fileName)
                }
            }
        }
        if (connectionMap.isEmpty()) {
            logger.error { "The file connection_map.json must be provided." }
        }
        if (metadataMap.isEmpty()) {
            logger.error { "The file metadata_map.json must be provided." }
        }
        if (tagFileNames.isEmpty()) {
            logger.error { "You must provide at least one json file containing Lake Tag data." }
        }
        if (results.anyFailures && failOnErrors) {
            logger.error { "Some errors detected, failing the workflow." }
            exitProcess(1)
        }
    }

    data class MigratorContext(
        val sourceConnectionQN: String,
        val targetConnectionQN: String,
        val includeArchived: Boolean,
        val sourceDatabaseName: String,
        val targetDatabaseName: String,
    )
}
