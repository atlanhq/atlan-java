/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.xls

import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.Closeable
import java.io.FileOutputStream

/**
 * Utility class for writing to Excel files, using Apache POI.
 *
 * @param path location and filename of the Excel file to write
 */
class ExcelWriter
    @JvmOverloads
    constructor(
        val path: String,
        private val rowAccessWindowSize: Int = 100,
    ) : Closeable {
        private val workbook = SXSSFWorkbook(rowAccessWindowSize)

        /**
         * Create a new worksheet within the workbook.
         *
         * @param name of the sheet to create
         * @return the worksheet
         */
        fun createSheet(name: String): ExcelSheetWriter = ExcelSheetWriter(workbook, name)

        /** {@inheritDoc} */
        override fun close() {
            val fos = FileOutputStream(path)
            workbook.write(fos)
            workbook.close() // should also clean up temporary files
            fos.close()
        }
    }
