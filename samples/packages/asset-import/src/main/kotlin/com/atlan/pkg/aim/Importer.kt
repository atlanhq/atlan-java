/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.LinkCache
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVImporter.Companion.attributesToClear
import mu.KotlinLogging
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val config = Utils.setPackageOps<AssetImportCfg>()
        import(config)
    }

    fun import(config: AssetImportCfg) {
        val batchSize = 20
        val assetsFilename = Utils.getOrDefault(config.assetsFile, "")
        val glossariesFilename = Utils.getOrDefault(config.glossariesFile, "")
        val assetAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.assetsAttrToOverwrite, listOf()).toMutableList(), "assets", logger)
        val assetsFailOnErrors = Utils.getOrDefault(config.assetsFailOnErrors, true)
        val glossaryAttrsToOverwrite =
            attributesToClear(Utils.getOrDefault(config.glossariesAttrToOverwrite, listOf()).toMutableList(), "glossaries", logger)
        val assetsUpdateOnly = Utils.getOrDefault(config.assetsUpsertSemantic, "update") == "update"
        val glossariesUpdateOnly = Utils.getOrDefault(config.glossariesUpsertSemantic, "update") == "update"
        val glossariesFailOnErrors = Utils.getOrDefault(config.glossariesFailOnErrors, true)

        if (glossariesFilename.isBlank() && assetsFilename.isBlank()) {
            logger.error { "No input file was provided for either glossaries or assets." }
            exitProcess(1)
        }

        LinkCache.preload()

        if (glossariesFilename.isNotBlank()) {
            FieldSerde.FAIL_ON_ERRORS.set(glossariesFailOnErrors)
            logger.info { "=== Importing glossaries... ===" }
            val glossaryImporter =
                GlossaryImporter(glossariesFilename, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize)
            glossaryImporter.import()
            logger.info { "=== Importing categories... ===" }
            val categoryImporter =
                CategoryImporter(glossariesFilename, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize)
            categoryImporter.import()
            logger.info { "=== Importing terms... ===" }
            val termImporter =
                TermImporter(glossariesFilename, glossaryAttrsToOverwrite, glossariesUpdateOnly, batchSize)
            termImporter.import()
        }
        if (assetsFilename.isNotBlank()) {
            FieldSerde.FAIL_ON_ERRORS.set(assetsFailOnErrors)
            logger.info { "=== Importing assets... ===" }
            val assetImporter = AssetImporter(assetsFilename, assetAttrsToOverwrite, assetsUpdateOnly, batchSize)
            assetImporter.import()
        }
    }
}
