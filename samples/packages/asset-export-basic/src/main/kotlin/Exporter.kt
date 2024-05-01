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
        export(config, outputDirectory)
    }

    fun export(config: AssetExportBasicCfg, outputDirectory: String) {
        val batchSize = 20
        val assetsExportScope = Utils.getOrDefault(config.exportScope, "ENRICHED_ONLY")
        val limitToAssets = Utils.getAsList(config.assetTypesToInclude)
        val limitToAttributes = Utils.getAsList(config.attributesToInclude)
        val assetsQualifiedNamePrefix = Utils.getOrDefault(config.qnPrefix, "default")
        val includeDescription = Utils.getOrDefault(config.includeDescription, true)
        val includeArchived = Utils.getOrDefault(config.includeArchived, false)
        val emails = Utils.getAsList(config.emailAddresses)

        val ctx = Context(
            assetsExportScope,
            limitToAssets,
            limitToAttributes,
            assetsQualifiedNamePrefix,
            includeDescription,
            includeArchived,
        )

        val exportedFiles = mutableListOf<File>()
        val glossaryFile = "$outputDirectory${File.separator}glossary-export.csv"
        if ("GLOSSARIES_ONLY" == assetsExportScope || Utils.getOrDefault(config.includeGlossaries, false)) {
            val glossaryExporter = GlossaryExporter(ctx, glossaryFile, batchSize)
            glossaryExporter.export()
            exportedFiles.add(File(glossaryFile))
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            File(glossaryFile).createNewFile()
        }
        val assetsFile = "$outputDirectory${File.separator}asset-export.csv"
        if ("GLOSSARIES_ONLY" != assetsExportScope) {
            val assetExporter = AssetExporter(ctx, assetsFile, batchSize)
            assetExporter.export()
            exportedFiles.add(File(assetsFile))
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            File(assetsFile).createNewFile()
        }

        if (emails.isNotEmpty()) {
            Utils.sendEmail(
                "[Atlan] Asset Export (basic) results",
                emails,
                "Hi there! As requested, please find attached the results of the Asset Export (basic) package.\n\nAll the best!\nAtlan",
                exportedFiles,
            )
        }
    }

    data class Context(
        val assetsExportScope: String,
        val limitToAssets: List<String>,
        val limitToAttributes: List<String>,
        val assetsQualifiedNamePrefix: String,
        val includeDescription: Boolean,
        val includeArchived: Boolean,
    )
}
