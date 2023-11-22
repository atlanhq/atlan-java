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

        if (Utils.getOrDefault(config.includeGlossaries, false)) {
            val glossaryExporter = GlossaryExporter(
                "$outputDirectory${File.separator}glossary-export.csv",
                batchSize,
            )
            glossaryExporter.export()
        }
        val assetExporter = AssetExporter(
            "$outputDirectory${File.separator}asset-export.csv",
            assetsExportScope,
            assetsQualifiedNamePrefix,
            batchSize,
        )
        assetExporter.export()
    }
}
