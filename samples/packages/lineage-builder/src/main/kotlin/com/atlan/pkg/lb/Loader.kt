/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import AssetImportCfg
import LineageBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.LineageProcess
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.aim.Importer
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVXformer.Companion.getHeader
import com.atlan.util.AssetBatch.AssetIdentity
import java.io.File
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Loader {
    private val logger = Utils.getLogger(Loader.javaClass.name)

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<LineageBuilderCfg>().use { ctx ->
            import(ctx, outputDirectory)
        }
    }

    fun import(
        ctx: PackageContext<LineageBuilderCfg>,
        outputDirectory: String = "tmp",
    ) {
        val lineageFileProvided = Utils.isFileProvided(ctx.config.lineageImportType, ctx.config.lineageFile, ctx.config.lineageKey)
        if (!lineageFileProvided) {
            logger.error { "No input file was provided for lineage." }
            exitProcess(1)
        }
        if (ctx.config.fieldSeparator.length > 1) {
            logger.error { "Field separator must be only a single character. The provided value is too long: ${ctx.config.fieldSeparator}" }
            exitProcess(2)
        }

        val lineageInput =
            Utils.getInputFile(
                ctx.config.lineageFile,
                outputDirectory,
                ctx.config.lineageImportType == "DIRECT",
                ctx.config.lineagePrefix,
                ctx.config.lineageKey,
            )
        if (lineageInput.isNotBlank()) {
            FieldSerde.FAIL_ON_ERRORS.set(ctx.config.lineageFailOnErrors)
            ctx.connectionCache.preload()

            // 1. Transform the assets, so we can load them prior to creating any lineage relationships
            logger.info { "=== Processing assets... ===" }
            val assetsFile = "$outputDirectory${File.separator}CSA_LB_assets.csv"
            val assetXform =
                AssetTransformer(
                    ctx,
                    lineageInput,
                    logger,
                )
            assetXform.transform(assetsFile)

            // 2. Create the assets
            val importConfig =
                AssetImportCfg(
                    assetsFile = assetsFile,
                    assetsConfig = "advanced",
                    assetsUpsertSemantic = ctx.config.lineageUpsertSemantic,
                    assetsFailOnErrors = ctx.config.lineageFailOnErrors,
                    assetsCaseSensitive = ctx.config.lineageCaseSensitive,
                    assetsBatchSize = ctx.config.batchSize,
                    assetsFieldSeparator = ctx.config.fieldSeparator,
                    assetsCmHandling = CustomMetadataHandling.IGNORE.value,
                    assetsTagHandling = AtlanTagHandling.IGNORE.value,
                    relaxedCqn = ctx.config.relaxedCqn,
                )
            lateinit var qualifiedNameMap: Map<AssetIdentity, String>
            Utils.initializeContext(importConfig, ctx).use { iCtx ->
                val results = Importer.import(iCtx, outputDirectory)
                qualifiedNameMap = results?.primary?.qualifiedNames?.toMap() ?: mapOf()
                results?.close()
            }

            // 3. Transform the lineage, only keeping any rows that have both input and output assets in Atlan
            logger.info { "=== Processing lineage... ===" }
            val lineageHeaders =
                mutableListOf(
                    Asset.TYPE_NAME.atlanFieldName,
                    Asset.QUALIFIED_NAME.atlanFieldName,
                    Asset.NAME.atlanFieldName,
                    Asset.CONNECTION_QUALIFIED_NAME.atlanFieldName,
                    Asset.CONNECTOR_NAME.atlanFieldName,
                    LineageProcess.INPUTS.atlanFieldName,
                    LineageProcess.OUTPUTS.atlanFieldName,
                )
            val lineageFile = "$outputDirectory${File.separator}CSA_LB_lineage.csv"
            // Determine any non-standard lineage fields in the header and append them to the end of
            // the list of standard header fields, so they're passed-through to be used as part of
            // defining the lineage process itself
            val inputHeaders = getHeader(lineageInput, fieldSeparator = ctx.config.fieldSeparator[0]).toMutableList()
            inputHeaders.removeAll(AssetTransformer.INPUT_HEADERS)
            inputHeaders.removeAll(LineageTransformer.INPUT_HEADERS)
            inputHeaders.forEach { lineageHeaders.add(it) }
            val lineageXform =
                LineageTransformer(
                    ctx,
                    lineageInput,
                    lineageHeaders,
                    qualifiedNameMap,
                    logger,
                )
            lineageXform.transform(lineageFile)

            // 4. Load the lineage processes (note that for these we want a full, not partial, create)
            val lineageConfig =
                AssetImportCfg(
                    assetsFile = lineageFile,
                    assetsUpsertSemantic = "upsert",
                    assetsConfig = "advanced",
                    assetsFailOnErrors = ctx.config.lineageFailOnErrors,
                    assetsCaseSensitive = ctx.config.lineageCaseSensitive,
                    assetsBatchSize = ctx.config.batchSize,
                    assetsFieldSeparator = ctx.config.fieldSeparator,
                    assetsCmHandling = ctx.config.cmHandling,
                    assetsTagHandling = ctx.config.tagHandling,
                    relaxedCqn = ctx.config.relaxedCqn,
                )
            Utils.initializeContext(lineageConfig, ctx).use { iCtx ->
                Importer.import(iCtx, outputDirectory)?.close()
            }

            if (ctx.config.lineageFailOnErrors && (assetXform.anyFailures || lineageXform.anyFailures)) {
                logger.error { "Errors detected during loading -- failing the workflow." }
                exitProcess(1)
            }
        }
    }
}
