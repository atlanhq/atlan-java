/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.assets.Asset

/**
 * Interface to preprocess a CSV file without actually creating any assets.
 */
interface RowPreprocessor {
    /**
     * Preprocess the provided row of CSV.
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @param qnIdx index of the qualifiedName
     * @return the preprocessed row of values for the row of CSV
     */
    fun preprocessRow(
        row: List<String>,
        header: List<String>,
        typeIdx: Int,
        qnIdx: Int,
    ): List<String>

    /**
     * Finalize the preprocessing of the CSV.
     *
     * @param header column names
     * @param (optional) name of the output file from the preprocessing (if any)
     * @return the finalized results of the preprocessing
     */
    fun finalize(
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
