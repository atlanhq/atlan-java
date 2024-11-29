/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.PackageContext
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
        Utils.initializeContext(config).use { ctx ->
            export(ctx, outputDirectory)
        }
    }

    fun export(
        ctx: PackageContext<AssetExportBasicCfg>,
        outputDirectory: String,
    ) {
        val batchSize = 300
        val cmFields = getAllCustomMetadataFields(ctx)

        val exportedFiles = mutableListOf<File>()
        val glossaryFile = "$outputDirectory${File.separator}glossary-export.csv"
        if ("GLOSSARIES_ONLY" == ctx.config.exportScope || ctx.config.includeGlossaries!!) {
            val glossaryExporter = GlossaryExporter(ctx, glossaryFile, batchSize, cmFields)
            glossaryExporter.export()
            exportedFiles.add(File(glossaryFile))
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            File(glossaryFile).createNewFile()
        }
        val meshFile = "$outputDirectory${File.separator}products-export.csv"
        if ("PRODUCTS_ONLY" == ctx.config.exportScope || ctx.config.includeProducts!!) {
            val meshExporter = MeshExporter(ctx, meshFile, batchSize, cmFields)
            meshExporter.export()
            exportedFiles.add(File(meshFile))
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            File(meshFile).createNewFile()
        }
        val assetsFile = "$outputDirectory${File.separator}asset-export.csv"
        if ("GLOSSARIES_ONLY" != ctx.config.exportScope && "PRODUCTS_ONLY" != ctx.config.exportScope) {
            val assetExporter = AssetExporter(ctx, assetsFile, batchSize, cmFields)
            assetExporter.export()
            exportedFiles.add(File(assetsFile))
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            File(assetsFile).createNewFile()
        }

        when (ctx.config.deliveryType) {
            "EMAIL" -> {
                val emails = Utils.getAsList(ctx.config.emailAddresses)
                if (emails.isNotEmpty()) {
                    Utils.sendEmail(
                        "[Atlan] Asset Export (basic) results",
                        emails,
                        "Hi there! As requested, please find attached the results of the Asset Export (basic) package.\n\nAll the best!\nAtlan",
                        exportedFiles,
                    )
                }
            }
            "CLOUD" -> {
                for (exportFile in exportedFiles) {
                    Utils.uploadOutputFile(
                        exportFile.path,
                        Utils.getOrDefault(ctx.config.targetPrefix, ""),
                        "",
                    )
                }
            }
        }
    }

    fun getAllCustomMetadataFields(ctx: PackageContext<AssetExportBasicCfg>): List<CustomMetadataField> {
        val customMetadataDefs =
            ctx.client.customMetadataCache
                .getAllCustomAttributes(false, true)
        val cmFields = mutableListOf<CustomMetadataField>()
        for ((setName, attributes) in customMetadataDefs) {
            for (attribute in attributes) {
                cmFields.add(CustomMetadataField(ctx.client, setName, attribute.displayName))
            }
        }
        return cmFields
    }
}
