/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

/**
 * Generic interface through which to write out tabular content.
 */
interface TabularWriter {
    /**
     * Create a header row for the tabular output.
     *
     * @param headers ordered map of header names and descriptions
     */
    fun writeHeader(headers: Map<String, String>)

    /**
     * Create a header row for the tabular output.
     *
     * @param values ordered list of header column names
     */
    fun writeHeader(values: Iterable<String>)

    /**
     * Write a row of data into the tabular output, where key of the map is the column name and the value
     * is the value to write for that column of the row of data.
     * Note: be sure you have first called {@code writeHeader} to output the header row.
     *
     * @param values map keyed by column name with values for the row of data
     */
    fun writeRecord(values: Map<String, Any?>?)

    /**
     * Add a row of data to the end of the tabular output.
     *
     * @param data the row of data to add
     */
    fun writeRecord(data: Iterable<Any?>?)
}
