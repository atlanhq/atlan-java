/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.lb

import LineageBuilderCfg
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
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
        val fieldSeparator = ctx.config.fieldSeparator[0]

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
            transform(ctx, fieldSeparator, lineageInput, "$outputDirectory${File.separator}transformed.csv")
        }
    }

    private fun transform(
        ctx: PackageContext<LineageBuilderCfg>,
        fieldSeparator: Char,
        inputFile: String,
        outputFile: String,
    ) {
        // Create the set of output headers to use:
        // 1. Start with the original input headers (to carry through any extras)
        val originalHeaders = getHeader(inputFile, fieldSeparator).toMutableList()
        // 2. Remove any headers that are lineage-builder specific (not passthrough to asset import)
        originalHeaders.removeAll(AssetXformer.INPUT_HEADERS)
        originalHeaders.removeAll(LineageXformer.INPUT_HEADERS)
        // 3. Start the target output headers with the base we'll need for all assets
        val completeHeaders = AssetXformer.BASE_OUTPUT_HEADERS.toMutableList()
        val leftOverLineage = LineageXformer.BASE_OUTPUT_HEADERS.toMutableList()
        leftOverLineage.removeAll(completeHeaders)
        // 4. Remove any overlapping base from the existing original headers
        originalHeaders.removeAll(completeHeaders)
        // 5. Then carry through any of the passthrough headers from the original input
        completeHeaders.addAll(originalHeaders)
        completeHeaders.addAll(leftOverLineage)

        // Actually write the transformed output into a singular CSV file
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

                logger.info { " --- Transforming assets... ---" }
                val assetXformer = AssetXformer(ctx, inputFile, completeHeaders, logger)
                assetXformer.transform(writer)

                logger.info { " --- Transforming lineage processes... ---" }
                val lineageXformer = LineageXformer(ctx, inputFile, completeHeaders, logger)
                lineageXformer.transform(writer)
            }
    }
}
