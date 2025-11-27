/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Column
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.rab.AssetXformer.Companion.BASE_OUTPUT_HEADERS
import com.atlan.pkg.serde.FieldSerde
import com.atlan.pkg.serde.csv.CSVXformer.Companion.getHeader
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import java.io.File
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
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
        if (ctx.config.assetsFieldSeparator.length > 1) {
            logger.error { "Field separator must be only a single character. The provided value is too long: ${ctx.config.assetsFieldSeparator}" }
            exitProcess(2)
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

        FieldSerde.FAIL_ON_ERRORS.set(ctx.config.assetsFailOnErrors)
        logger.info { "=== Importing assets... ===" }

        transform(ctx, fieldSeparator, assetsInput, "$outputDirectory${File.separator}current-file-transformed.csv")

        if (ctx.config.previousFileDirect.isNotBlank()) {
            transform(ctx, fieldSeparator, ctx.config.previousFileDirect, "$outputDirectory${File.separator}previous-file-transformed.csv")
        }
    }

    private fun transform(
        ctx: PackageContext<RelationalAssetsBuilderCfg>,
        fieldSeparator: Char,
        inputFile: String,
        outputFile: String,
    ) {
        val targetHeaders = getHeader(inputFile, fieldSeparator).toMutableList()
        // Inject two columns at the end that we need for column assets
        targetHeaders.add(Column.ORDER.atlanFieldName)
        targetHeaders.add(ColumnXformer.COLUMN_PARENT_QN)
        val revisedFile = Paths.get("$inputFile.CSA_RAB.csv")
        val preprocessedDetails =
            ColumnXformer
                .Preprocessor(inputFile, fieldSeparator, logger)
                .preprocess<ColumnXformer.Results>(
                    outputFile = revisedFile.toString(),
                    outputHeaders = targetHeaders,
                )
        // Note: only do validations via the preprocessor, allow the delegation to
        // asset import to do any cache preloading (unnecessary for the transformation work)

        val completeHeaders = BASE_OUTPUT_HEADERS.toMutableList()
        // Determine any non-standard RAB fields in the header and append them to the end of
        // the list of standard header fields, so they're passed-through to asset import
        val inputHeaders = getHeader(preprocessedDetails.preprocessedFile, fieldSeparator = fieldSeparator).toMutableList()
        inputHeaders.removeAll(BASE_OUTPUT_HEADERS)
        inputHeaders.removeAll(
            listOf(
                ColumnXformer.COLUMN_NAME,
                ColumnXformer.COLUMN_PARENT_QN,
                ConnectionXformer.CONNECTOR_TYPE,
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
                Paths.get(outputFile),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE,
            ).use { writer ->
                writer.writeRecord(completeHeaders)

                logger.info { " --- Transforming connections... ---" }
                val connectionXformer = ConnectionXformer(ctx, completeHeaders, preprocessedDetails, logger)
                connectionXformer.transform(writer)

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
    }
}
