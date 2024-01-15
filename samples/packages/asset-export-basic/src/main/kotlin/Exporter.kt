/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.Utils
import java.io.File

/**
 * Actually run the export, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object Exporter {
    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<AssetExportBasicCfg>()

        val batchSize = 20
        val assetsExportScope = Utils.getOrDefault(config.exportScope, "ENRICHED_ONLY")
        val assetsQualifiedNamePrefix = Utils.getOrDefault(config.qnPrefix, "default")
        val includeDescription = Utils.getOrDefault(config.includeDescription, true)

        val glossaryFile = "$outputDirectory${File.separator}glossary-export.csv"
        if ("GLOSSARIES_ONLY" == assetsExportScope || Utils.getOrDefault(config.includeGlossaries, false)) {
            val glossaryExporter = GlossaryExporter(
                glossaryFile,
                batchSize,
            )
            glossaryExporter.export()
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            File(glossaryFile).createNewFile()
        }
        val assetsFile = "$outputDirectory${File.separator}asset-export.csv"
        if ("GLOSSARIES_ONLY" != assetsExportScope) {
            val assetExporter = AssetExporter(
                assetsFile,
                assetsExportScope,
                assetsQualifiedNamePrefix,
                batchSize,
                includeDescription,
            )
            assetExporter.export()
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            File(assetsFile).createNewFile()
        }
    }
}
