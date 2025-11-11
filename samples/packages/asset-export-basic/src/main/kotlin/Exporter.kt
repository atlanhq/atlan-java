/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.AtlanClient
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.Utils.validatePathIsSafe
import java.io.File

/**
 * Actually run the export, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object Exporter {
    @JvmStatic
    fun main(args: Array<String>) {
        val od = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<AssetExportBasicCfg>().use { ctx ->
            export(ctx, od)
        }
    }

    fun export(
        ctx: PackageContext<AssetExportBasicCfg>,
        od: String,
    ) {
        val batchSize = 300
        val cmFields = getAllCustomMetadataFields(ctx.client)

        val outputDirectory = validatePathIsSafe(od)
        outputDirectory.toFile().mkdirs()

        val exportedFiles = mutableListOf<File>()
        val glossaryFile = validatePathIsSafe(outputDirectory, "glossary-export.csv")
        if ("GLOSSARIES_ONLY" == ctx.config.exportScope || ctx.config.includeGlossaries) {
            val glossaryExporter = GlossaryExporter(ctx, glossaryFile.toString(), batchSize, cmFields)
            glossaryExporter.export()
            exportedFiles.add(glossaryFile.toFile())
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            glossaryFile.toFile().createNewFile()
        }
        val meshFile = validatePathIsSafe(outputDirectory, "products-export.csv")
        if ("PRODUCTS_ONLY" == ctx.config.exportScope || ctx.config.includeProducts) {
            val meshExporter = MeshExporter(ctx, meshFile.toString(), batchSize, cmFields)
            meshExporter.export()
            exportedFiles.add(meshFile.toFile())
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            meshFile.toFile().createNewFile()
        }
        val assetsFile = validatePathIsSafe(outputDirectory, "asset-export.csv")
        if ("GLOSSARIES_ONLY" != ctx.config.exportScope && "PRODUCTS_ONLY" != ctx.config.exportScope) {
            val assetExporter = AssetExporter(ctx, assetsFile.toString(), batchSize, cmFields)
            assetExporter.export()
            exportedFiles.add(assetsFile.toFile())
        } else {
            // Still create an (empty) output file, to avoid errors in Argo
            assetsFile.toFile().createNewFile()
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

    fun getAllCustomMetadataFields(client: AtlanClient): List<CustomMetadataField> {
        val customMetadataDefs =
            client.customMetadataCache
                .getAllCustomAttributes(false, true)
        val cmFields = mutableListOf<CustomMetadataField>()
        for ((setName, attributes) in customMetadataDefs) {
            for (attribute in attributes) {
                cmFields.add(CustomMetadataField(client, setName, attribute.displayName))
            }
        }
        return cmFields
    }
}
