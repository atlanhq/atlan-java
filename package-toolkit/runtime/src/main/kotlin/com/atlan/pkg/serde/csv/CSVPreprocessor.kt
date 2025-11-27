/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.assets.Asset
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import mu.KLogger
import kotlin.jvm.Throws

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
    open val requiredHeaders: Map<String, Set<String>> = emptyMap(),
) {
    val header = CSVXformer.getHeader(filename, fieldSeparator)

    /**
     * Preprocess the provided row of CSV.
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @param qnIdx index of the qualifiedName
     * @return the preprocessed row of values for the row of CSV
     */
    abstract fun preprocessRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): List<String>

    /**
     * Preprocess the CSV file.
     *
     * @param outputFile (optional) name of the output file into which to write preprocessed row values, if specified overrides whatever was set as {@code producesFile} on the preprocessor itself
     * @param outputHeaders (optional) header column names to output into the file containing preprocessed row values, if specified overrides whatever was set as {@code usingHeaders} on the preprocessor itself
     * @return any resulting details captured during the preprocessing
     */
    inline fun <reified T : Results> preprocess(
        outputFile: String? = producesFile,
        outputHeaders: List<String>? = usingHeaders,
    ): T {
        validate()
        return CSVReader(
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

    /**
     * Validate that the format of the input file matches the requirements of the package.
     *
     * @param header column names
     * @return the list of names of columns that are required but not present in the file
     */
    fun validateHeader(): List<String> {
        val missing = mutableListOf<String>()
        if (header.isEmpty()) {
            missing.addAll(requiredHeaders.keys)
        } else {
            requiredHeaders.forEach { (key, options) ->
                if (!header.contains(key) && options.none { header.contains(it) }) {
                    missing.add(key)
                }
            }
        }
        return missing
    }

    /**
     * Validate the contents of the file up-front for any obvious errors.
     *
     * @throws IllegalArgumentException on any detected errors that will prevent the file from being processed successfully
     */
    @Throws(IllegalArgumentException::class)
    open fun validate() {
        val missingColumns = validateHeader()
        if (missingColumns.isNotEmpty()) {
            throw IllegalArgumentException("Invalid input file received. Input CSV is missing required columns: $missingColumns")
        }
    }

    /**
     * Finalize the preprocessing of the CSV.
     *
     * @param header column names
     * @param (optional) name of the output file from the preprocessing (if any)
     * @return the finalized results of the preprocessing
     */
    open fun finalize(
        header: List<String>,
        outputFile: String? = null,
    ): Results =
        Results(
            hasLinks = header.contains(Asset.LINKS.atlanFieldName),
            hasTermAssignments = header.contains("assignedTerms"),
            hasDomainRelationship = header.contains(Asset.DOMAIN_GUIDS.atlanFieldName),
            hasProductRelationship = header.contains(Asset.PRODUCT_GUIDS.atlanFieldName),
            outputFile = outputFile,
        )

    /** Extensible class through which to capture details of the pre-processing of a file. */
    open class Results(
        val hasLinks: Boolean,
        val hasTermAssignments: Boolean,
        val hasDomainRelationship: Boolean,
        val hasProductRelationship: Boolean,
        val outputFile: String?,
    )
}
