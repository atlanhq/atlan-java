/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

/**
 * Interface to transform a row of CSV values into a row of some other CSV values.
 */
interface RowTransformer {
    /**
     * Whether to include the provided row in the output (true), or skip it (false).
     *
     * @param inputRow map from column name to value of that column for the row being considered
     */
    fun includeRow(inputRow: Map<String, String>): Boolean

    /**
     * Actually translate the provided row into a row of output values.
     *
     * @param inputRow map from column name to value of that column for the row being processed
     * @return a list of values in the same order as headers for the output file
     */
    fun mapRow(inputRow: Map<String, String>): List<String>
}
