/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

/**
 * Interface to preprocess a CSV file without actually creating any assets.
 */
interface CSVPreprocessor {

    /**
     * Preprocess the provided row of CSV.
     *
     * @param row of values
     * @param header column names
     * @param typeIdx index of the typeName
     * @param qnIdx index of the qualifiedName
     * @return the preprocessed row of values for the row of CSV
     */
    fun preprocessRow(row: List<String>, header: List<String>, typeIdx: Int, qnIdx: Int): List<String>
}
