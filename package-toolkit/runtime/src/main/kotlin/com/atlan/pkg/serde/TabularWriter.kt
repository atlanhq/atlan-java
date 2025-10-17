/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde

import java.io.Closeable
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Generic interface through which to write out tabular content.
 */
abstract class TabularWriter : Closeable {
    protected val header = mutableListOf<String>()
    protected val headerWritten = AtomicBoolean(false)

    /**
     * Create a header row for the tabular output.
     *
     * @param headers ordered map of header names and descriptions
     */
    fun writeHeader(headers: Map<String, String>) {
        if (headers.isNotEmpty()) {
            writeHeader(headers.keys)
        }
    }

    /**
     * Create a header row for the tabular output.
     *
     * @param values ordered list of header column names
     * @throws IOException if a multiple attempts are made to write a header (can be done only once)
     */
    @Throws(IOException::class)
    open fun writeHeader(values: Iterable<String>) {
        if (headerWritten.compareAndSet(false, true)) {
            header.addAll(values)
            writeRecord(values)
        } else {
            throw IOException("Header can only be written once (multiple attempts made to write a header).")
        }
    }

    /**
     * Write a row of data into the tabular output, where key of the map is the column name and the value
     * is the value to write for that column of the row of data.
     * Note: be sure you have first called {@code writeHeader} to output the header row.
     *
     * @param values map keyed by column name with values for the row of data
     * @throws IOException if the data cannot be written
     */
    @Throws(IOException::class)
    fun writeRecord(values: Map<String, Any?>?) {
        if (values != null) {
            val list = mutableListOf<Any>()
            header.forEach { name ->
                list.add(values.getOrDefault(name, "") ?: "")
            }
            writeRecord(list)
        }
    }

    /**
     * Add a row of data to the end of the tabular output.
     *
     * @param data the row of data to add
     * @throws IOException if the data cannot be written
     */
    @Throws(IOException::class)
    abstract fun writeRecord(data: Iterable<Any?>?)
}
