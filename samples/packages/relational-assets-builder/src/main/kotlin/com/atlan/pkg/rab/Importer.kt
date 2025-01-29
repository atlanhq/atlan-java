/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import AssetImportCfg
import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.rab.AssetXformer.Companion.BASE_OUTPUT_HEADERS
import com.atlan.pkg.rab.AssetXformer.Companion.getSQLHierarchyDetails
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVPreprocessor
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.pkg.serde.csv.CSVXformer.Companion.getHeader
import com.atlan.pkg.util.DeltaProcessor
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.exitProcess

/**
 * Actually run the importer.
 * Note: all parameters should be passed through environment variables.
 */
object Importer {
    private val logger = Utils.getLogger(Importer.javaClass.name)

    const val PREVIOUS_FILES_PREFIX = "csa-relational-assets-builder"

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        Utils.initializeContext<RelationalAssetsBuilderCfg>().use { ctx ->
            import(ctx, outputDirectory)
        }
    }

    /**
     * Actually run the import.
     *
     * @param ctx context in which this package is running
     * @param outputDirectory in which to do any data processing
     */
    fun import(
        ctx: PackageContext<RelationalAssetsBuilderCfg>,
        outputDirectory: String = "tmp",
    ) {
        val assetsUpload = ctx.config.importType == "DIRECT"
        val assetsFilename = ctx.config.assetsFile
        val assetsKey = ctx.config.assetsKey
        val fieldSeparator = ctx.config.assetsFieldSeparator[0]

        val assetsFileProvided =
            (
                assetsUpload && assetsFilename.isNotBlank()
            ) ||
                (
                    !assetsUpload && assetsKey.isNotBlank()
                )
        if (!assetsFileProvided) {
            logger.error { "No input file was provided for assets." }
            exitProcess(1)
        }

        // Preprocess the CSV file in an initial pass to inject key details,
        // to allow subsequent out-of-order parallel processing
        val assetsInput =
            Utils.getInputFile(
                assetsFilename,
                outputDirectory,
                assetsUpload,
                ctx.config.assetsPrefix,
                assetsKey,
            )

        ctx.connectionCache.preload()

        FieldSerde.FAIL_ON_ERRORS.set(ctx.config.assetsFailOnErrors)
        logger.info { "=== Importing assets... ===" }

        logger.info { " --- Importing connections... ---" }
        // Note: we force-track the batches here to ensure any created connections are cached
        // (without tracking, any connections created will NOT be cached, either, which will then cause issues
        // with the subsequent processing steps.)
        // We also need to load these connections first, irrespective of any delta calculation, so that
        // we can be certain we will be able to resolve the assets' qualifiedNames (for subsequent processing)
        val connectionImporter = ConnectionImporter(ctx, assetsInput, logger)
        connectionImporter.import()?.close()

        val transformedFile = transform(ctx, fieldSeparator, assetsInput)

        val previousFileXformed =
            if (ctx.config.previousFileDirect.isNotBlank()) {
                transform(ctx, fieldSeparator, ctx.config.previousFileDirect)
            } else {
                ""
            }

        val importConfig =
            AssetImportCfg(
                assetsFile = transformedFile,
                assetsUpsertSemantic = ctx.config.assetsUpsertSemantic,
                assetsDeltaSemantic = ctx.config.deltaSemantic,
                assetsDeltaRemovalType = ctx.config.deltaRemovalType,
                assetsDeltaReloadCalculation = ctx.config.deltaReloadCalculation,
                assetsPreviousFileDirect = previousFileXformed,
                assetsPreviousFilePrefix = PREVIOUS_FILES_PREFIX,
                assetsAttrToOverwrite = ctx.config.assetsAttrToOverwrite,
                assetsFailOnErrors = ctx.config.assetsFailOnErrors,
                assetsFieldSeparator = ctx.config.assetsFieldSeparator,
                assetsBatchSize = ctx.config.assetsBatchSize,
                trackBatches = ctx.config.trackBatches,
            )
        Utils.initializeContext(importConfig, ctx).use { iCtx ->
            com.atlan.pkg.aim.Importer
                .import(iCtx, outputDirectory)
                ?.close()
        }
    }

    private fun transform(
        ctx: PackageContext<RelationalAssetsBuilderCfg>,
        fieldSeparator: Char,
        inputFile: String,
    ): String {
        val targetHeaders = getHeader(inputFile, fieldSeparator).toMutableList()
        // Inject two columns at the end that we need for column assets
        targetHeaders.add(Column.ORDER.atlanFieldName)
        targetHeaders.add(ColumnXformer.COLUMN_PARENT_QN)
        val revisedFile = Paths.get("$inputFile.CSA_RAB.csv")
        val preprocessedDetails =
            Preprocessor(inputFile, fieldSeparator)
                .preprocess<Results>(
                    outputFile = revisedFile.toString(),
                    outputHeaders = targetHeaders,
                )

        // Only cache links and terms if there are any in the CSV, otherwise this
        // will be unnecessary work
        if (preprocessedDetails.hasLinks) {
            ctx.linkCache.preload()
        }
        if (preprocessedDetails.hasTermAssignments) {
            ctx.termCache.preload()
        }

        val completeHeaders = BASE_OUTPUT_HEADERS.toMutableList()
        val transformedFile = "$inputFile.transformed.csv"
        // Determine any non-standard RAB fields in the header and append them to the end of
        // the list of standard header fields, so they're passed-through to asset import
        val inputHeaders = getHeader(preprocessedDetails.preprocessedFile, fieldSeparator = fieldSeparator).toMutableList()
        inputHeaders.removeAll(BASE_OUTPUT_HEADERS)
        inputHeaders.removeAll(
            listOf(
                ColumnXformer.COLUMN_NAME,
                ColumnXformer.COLUMN_PARENT_QN,
                ConnectionImporter.CONNECTOR_TYPE,
                AssetXformer.ENTITY_NAME,
            ),
        )
        inputHeaders.forEach { completeHeaders.add(it) }

        CsvWriter
            .builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter('"')
            .quoteStrategy(QuoteStrategies.NON_EMPTY)
            .lineDelimiter(LineDelimiter.PLATFORM)
            .build(
                Paths.get(transformedFile),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE,
            ).use { writer ->
                writer.writeRecord(completeHeaders)

                logger.info { " --- Transforming databases... ---" }
                val databaseXformer = DatabaseXformer(ctx, completeHeaders, preprocessedDetails, logger)
                databaseXformer.transform(writer)

                logger.info { " --- Transforming schemas... ---" }
                val schemaXformer = SchemaXformer(ctx, completeHeaders, preprocessedDetails, logger)
                schemaXformer.transform(writer)

                logger.info { " --- Transforming tables... ---" }
                val tableXformer = TableXformer(ctx, completeHeaders, preprocessedDetails, logger)
                tableXformer.transform(writer)

                logger.info { " --- Transforming views... ---" }
                val viewXformer = ViewXformer(ctx, completeHeaders, preprocessedDetails, logger)
                viewXformer.transform(writer)

                logger.info { " --- Transforming materialized views... ---" }
                val materializedViewXformer = MaterializedViewXformer(ctx, completeHeaders, preprocessedDetails, logger)
                materializedViewXformer.transform(writer)

                logger.info { " --- Transforming columns... ---" }
                val columnXformer = ColumnXformer(ctx, completeHeaders, preprocessedDetails, logger)
                columnXformer.transform(writer)
            }
        return transformedFile
    }

    private class Preprocessor(
        originalFile: String,
        fieldSeparator: Char,
        outputFile: String? = null,
        outputHeaders: List<String>? = null,
    ) : CSVPreprocessor(
            filename = originalFile,
            logger = logger,
            fieldSeparator = fieldSeparator,
            producesFile = outputFile,
            usingHeaders = outputHeaders,
        ) {
        val entityQualifiedNameToType = mutableMapOf<String, String>()
        val qualifiedNameToChildCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToTableCount = mutableMapOf<String, AtomicInteger>()
        val qualifiedNameToViewCount = mutableMapOf<String, AtomicInteger>()

        var lastParentQN = ""
        var columnOrder = 1

        override fun preprocessRow(
            row: List<String>,
            header: List<String>,
            typeIdx: Int,
            qnIdx: Int,
        ): List<String> {
            val values = row.toMutableList()
            val typeName = CSVXformer.trimWhitespace(values.getOrElse(typeIdx) { "" })
            val qnDetails = getSQLHierarchyDetails(CSVXformer.getRowByHeader(header, values), typeName)
            if (typeName !in setOf(Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME)) {
                if (!qualifiedNameToChildCount.containsKey(qnDetails.parentUniqueQN)) {
                    qualifiedNameToChildCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                }
                qualifiedNameToChildCount[qnDetails.parentUniqueQN]?.incrementAndGet()
            }
            when (typeName) {
                Connection.TYPE_NAME, Database.TYPE_NAME, Schema.TYPE_NAME -> {
                    values.add("")
                    values.add("")
                }
                Table.TYPE_NAME -> {
                    if (!qualifiedNameToTableCount.containsKey(qnDetails.parentUniqueQN)) {
                        qualifiedNameToTableCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                    }
                    qualifiedNameToTableCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                    entityQualifiedNameToType[qnDetails.uniqueQN] = typeName
                    values.add("")
                    values.add("")
                }
                View.TYPE_NAME, MaterializedView.TYPE_NAME -> {
                    if (!qualifiedNameToViewCount.containsKey(qnDetails.parentUniqueQN)) {
                        qualifiedNameToViewCount[qnDetails.parentUniqueQN] = AtomicInteger(0)
                    }
                    qualifiedNameToViewCount[qnDetails.parentUniqueQN]?.incrementAndGet()
                    entityQualifiedNameToType[qnDetails.uniqueQN] = typeName
                    values.add("")
                    values.add("")
                }
                Column.TYPE_NAME -> {
                    // If it is a column, calculate the order and parent qualifiedName and inject them
                    if (qnDetails.parentUniqueQN == lastParentQN) {
                        columnOrder += 1
                    } else {
                        lastParentQN = qnDetails.parentUniqueQN
                        columnOrder = 1
                    }
                    values.add("$columnOrder")
                    values.add(qnDetails.parentPartialQN)
                }
            }
            return values
        }

        override fun finalize(
            header: List<String>,
            outputFile: String?,
        ): Results {
            val results = super.finalize(header, outputFile)
            return Results(
                results.hasLinks,
                results.hasTermAssignments,
                results.outputFile!!,
                entityQualifiedNameToType,
                qualifiedNameToChildCount,
                qualifiedNameToTableCount,
                qualifiedNameToViewCount,
            )
        }
    }

    class Results(
        hasLinks: Boolean,
        hasTermAssignments: Boolean,
        preprocessedFile: String,
        val entityQualifiedNameToType: Map<String, String>,
        val qualifiedNameToChildCount: Map<String, AtomicInteger>,
        val qualifiedNameToTableCount: Map<String, AtomicInteger>,
        val qualifiedNameToViewCount: Map<String, AtomicInteger>,
    ) : DeltaProcessor.Results(
            assetRootName = "",
            hasLinks = hasLinks,
            hasTermAssignments = hasTermAssignments,
            preprocessedFile = preprocessedFile,
        )
}
