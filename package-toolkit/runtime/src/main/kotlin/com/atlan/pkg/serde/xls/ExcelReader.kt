/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.xls

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.Closeable
import java.io.FileInputStream
import java.io.IOException
import java.math.BigDecimal

/**
 * Utility class for parsing and reading the contents of Excel files, using Apache POI.
 *
 * @param fileLocation location of the Excel file
 */
class ExcelReader(
    private val fileLocation: String,
) : Closeable {
    private val workbook: Workbook

    init {
        val file = FileInputStream(fileLocation)
        workbook = XSSFWorkbook(file)
    }

    /**
     * Indicates whether the file has the specified sheet within it (true) or not (false).
     *
     * @return boolean indicating whether the sheet is present (true) or not (false)
     */
    fun hasSheet(name: String): Boolean = workbook.getSheet(name) != null

    /**
     * Retrieve all rows from the specified sheet of the Excel workbook, by default using the
     * very first row (0) as the header row.
     *
     * @param index the index (0-based) of the worksheet within the workbook
     * @param headerRow index of the row containing headers (0-based)
     * @return a list of rows, each being a mapping from column name to its value
     */
    fun getRowsFromSheet(
        index: Int,
        headerRow: Int = 0,
    ): List<Map<String, String>> = getRowsFromSheet(workbook.getSheetAt(index), headerRow)

    /**
     * Retrieve all rows from the specified sheet of the Excel workbook, by default using the
     * very first row (0) as the header row.
     *
     * @param name of the worksheet from which to retrieve the data
     * @param headerRow index of the row containing headers (0-based)
     * @return a list of rows, each being a mapping from column name to its value
     * @throws IOException if the requested sheet cannot be found in the provided Excel file
     */
    @Throws(IOException::class)
    fun getRowsFromSheet(
        name: String,
        headerRow: Int = 0,
    ): List<Map<String, String>> {
        val sheet =
            workbook.getSheet(name)
                ?: throw IOException("Could not find sheet with name '$name' in the provided Excel file.")
        return getRowsFromSheet(sheet, headerRow)
    }

    /**
     * Retrieve all rows from the specified sheet of the Excel workbook.
     *
     * @param data the worksheet from which to retrieve the data
     * @param headerRow index of the row containing headers (0-based)
     * @return a list of rows, each being a mapping from column name to its value
     */
    private fun getRowsFromSheet(
        data: Sheet,
        headerRow: Int,
    ): List<Map<String, String>> {
        val allRows = mutableListOf<Map<String, String>>()
        val header = getHeaders(data.getRow(headerRow))
        for (row in data) {
            val rowIdx = row.rowNum
            if (rowIdx > headerRow) {
                val rowMapping = mutableMapOf<String, String>()
                for (cell in row) {
                    val colIdx = cell.columnIndex
                    val colName = header[colIdx]
                    val value =
                        when (cell.cellType) {
                            CellType.NUMERIC -> BigDecimal("" + cell.numericCellValue).toPlainString()
                            CellType.BOOLEAN -> "" + cell.booleanCellValue
                            CellType.FORMULA -> cell.cellFormula
                            CellType.STRING -> cell.richStringCellValue.string
                            else -> cell.richStringCellValue.string
                        }
                    rowMapping[colName] = value
                }
                if (rowMapping.isNotEmpty()) {
                    allRows.add(rowMapping)
                }
            }
        }
        return allRows
    }

    /**
     * Create a list of header names. The position of the name in the list is the column index of the header
     * in the spreadsheet itself.
     *
     * @param header row of header data
     * @return the list of header names, positional based on the column in which they appear in the spreadsheet
     */
    private fun getHeaders(header: Row): List<String> {
        val columns = mutableListOf<String>()
        for (cell in header) {
            val name = cell.richStringCellValue.string
            if (name != null) {
                columns.add(name)
            }
        }
        return columns
    }

    /** {@inheritDoc}  */
    @Throws(IOException::class)
    override fun close() {
        workbook.close()
    }
}
