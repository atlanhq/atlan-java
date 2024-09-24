/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.enums.AssetCreationHandling
import com.atlan.util.AssetBatch
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
 */
abstract class CSVPreprocessor(
    val filename: String,
    val logger: KLogger,
    val fieldSeparator: Char = ',',
) : RowPreprocessor {
    /**
     * Preprocess the CSV file.
     *
     * @param outputFile (optional) name of the output file into which to write preprocessed row values
     * @param outputHeaders (optional) header column names to output into the file containing preprocessed row values
     * @return any resulting details captured during the preprocessing
     */
    inline fun <reified T : RowPreprocessor.Results> preprocess(
        outputFile: String? = null,
        outputHeaders: List<String>? = null,
    ): T {
        return CSVReader(
            filename,
            updateOnly = true,
            trackBatches = false,
            caseSensitive = true,
            AssetBatch.CustomMetadataHandling.IGNORE,
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
}
