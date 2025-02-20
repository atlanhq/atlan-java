/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import mu.KLogger

/**
 * Preprocess a CSV file before loading anything to Atlan.
 *
 * This is for merely scanning through the CSV file and doing any pre-calculations or validations
 * across the entire file before attempting to load anything from it.
 *
 * @param filename name of the file to import
 * @param logger through which to record progress and any errors
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 * @param producesFile (optional) name of the output file into which to write preprocessed row values, if specified becomes the default {@code outputFile} for preprocess method
 * @param usingHeaders (optional) header column names to output into the file containing preprocessed row values, if specified becomes the default {@code outputHeaders} for the preprocess method
 */
abstract class CSVPreprocessor(
    val filename: String,
    val logger: KLogger,
    val fieldSeparator: Char = ',',
    val producesFile: String? = null,
    val usingHeaders: List<String>? = null,
) : RowPreprocessor {
    /**
     * Preprocess the CSV file.
     *
     * @param outputFile (optional) name of the output file into which to write preprocessed row values, if specified overrides whatever was set as {@code producesFile} on the preprocessor itself
     * @param outputHeaders (optional) header column names to output into the file containing preprocessed row values, if specified overrides whatever was set as {@code usingHeaders} on the preprocessor itself
     * @return any resulting details captured during the preprocessing
     */
    inline fun <reified T : RowPreprocessor.Results> preprocess(
        outputFile: String? = producesFile,
        outputHeaders: List<String>? = usingHeaders,
    ): T =
        CSVReader(
            filename,
            updateOnly = true,
            trackBatches = false,
            caseSensitive = true,
            CustomMetadataHandling.IGNORE,
            AtlanTagHandling.IGNORE,
            AssetCreationHandling.NONE,
            tableViewAgnostic = false,
            fieldSeparator,
        ).use { csv ->
            val start = System.currentTimeMillis()
            val results = csv.preprocess(this, logger, outputFile, outputHeaders) as T
            logger.info { "Total time taken: ${System.currentTimeMillis() - start} ms" }
            results
        }
}
