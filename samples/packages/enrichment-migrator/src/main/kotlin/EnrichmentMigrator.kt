/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.model.fields.CustomMetadataField
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.RowSerde
import mu.KotlinLogging
import java.io.File

/**
 * Actually run the migrator, taking all settings from environment variables.
 * Note: all parameters should be passed through environment variables.
 */
object EnrichmentMigrator {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<EnrichmentMigratorCfg>()
        val batchSize = Utils.getOrDefault(config.batchSize, 20)
        val fieldSeparator = Utils.getOrDefault(config.fieldSeparator, ",")[0]
        val sourceConnectionQN = Utils.getOrDefault(config.sourceConnection, "")
        val targetConnectionQN = Utils.getOrDefault(config.targetConnection, "")
        val sourcePrefix = Utils.getOrDefault(config.sourceQnPrefix, "")
        val sourceQN = if (sourcePrefix.isBlank()) sourceConnectionQN else "$sourceConnectionQN/$sourcePrefix"

        // 1. Extract the enriched metadata
        val extractConfig = AssetExportBasicCfg(
            exportScope = "ENRICHED_ONLY",
            qnPrefix = sourceQN,
            includeGlossaries = false,
        )
        Exporter.export(extractConfig, outputDirectory)
        val extractFile = "$outputDirectory${File.separator}asset-export.csv"

        // 2. Transform to the target metadata assets (limiting attributes as requested)
        val attributeLimits = Utils.getOrDefault(config.attributesList, listOf())
        val cmLimits = Utils.getOrDefault(config.customMetadata, "").split("|")
        val includeOOTB = Utils.getOrDefault(config.limitType, "EXCLUDE") == "INCLUDE"
        val includeCM = Utils.getOrDefault(config.cmLimitType, "EXCLUDE") == "INCLUDE"
        val start = mutableListOf(Asset.QUALIFIED_NAME.atlanFieldName, Asset.TYPE_NAME.atlanFieldName)
        val defaultAttrsToExtract = AssetExporter.getAttributesToExtract(true)
        if (includeOOTB) {
            start.add(Asset.NAME.atlanFieldName)
            attributeLimits.forEach {
                start.add(it)
            }
        } else {
            defaultAttrsToExtract.forEach {
                if (it !is CustomMetadataField) {
                    val fieldName = RowSerde.getHeaderForField(it)
                    start.add(fieldName)
                }
            }
            attributeLimits.forEach {
                start.remove(it)
            }
        }
        val header = if (includeCM) {
            cmLimits.forEach {
                start.add(it)
            }
            start.toList()
        } else {
            defaultAttrsToExtract.forEach {
                if (it is CustomMetadataField) {
                    val fieldName = RowSerde.getHeaderForField(it)
                    start.add(fieldName)
                }
            }
            cmLimits.forEach {
                start.remove(it)
            }
            start.toList()
        }
        val ctx = MigratorContext(
            sourceConnectionQN = sourceConnectionQN,
            targetConnectionQN = targetConnectionQN,
        )
        val transformedFile = "$outputDirectory${File.separator}CSA_EM_transformed.csv"
        val transformer = Transformer(
            ctx,
            extractFile,
            header.toList(),
            logger,
            fieldSeparator,
        )
        transformer.transform(transformedFile)

        // 3. Import the transformed file
        val importConfig = AssetImportCfg(
            assetsFile = transformedFile,
            assetsUpsertSemantic = "update",
            assetsFailOnErrors = Utils.getOrDefault(config.failOnErrors, true),
            assetsBatchSize = batchSize,
            assetsFieldSeparator = fieldSeparator.toString(),
        )
        Importer.import(importConfig, outputDirectory)
    }

    data class MigratorContext(
        val sourceConnectionQN: String,
        val targetConnectionQN: String,
    )
}
