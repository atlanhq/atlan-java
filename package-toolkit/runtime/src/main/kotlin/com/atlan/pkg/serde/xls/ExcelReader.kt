/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.xls

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable
import org.apache.poi.xssf.eventusermodel.XSSFReader
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler
import org.apache.poi.xssf.usermodel.XSSFComment
import org.xml.sax.InputSource
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.SAXParserFactory

/**
 * Utility class for parsing and reading the contents of Excel files, using Apache POI.
 *
 * @param fileLocation location of the Excel file
 */
class ExcelReader(
    private val fileLocation: String,
) : Closeable {
    private val pkg: OPCPackage = OPCPackage.open(fileLocation)
    private val reader: XSSFReader = XSSFReader(pkg)
    private val strings = ReadOnlySharedStringsTable(pkg)
    private val styles = reader.stylesTable
    private val formatter = DataFormatter()

    /**
     * Indicates whether the file has the specified sheet within it (true) or not (false).
     *
     * @return boolean indicating whether the sheet is present (true) or not (false)
     */
    fun hasSheet(name: String): Boolean {
        val sheets = reader.sheetsData as SheetIterator
        while (sheets.hasNext()) {
            sheets.next().use {
                if (name == sheets.sheetName) return true
            }
        }
        return false
    }

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
    ): List<Map<String, String>> {
        var currentIdx = 0
        var content: List<Map<String, String>>? = null
        val sheets = reader.sheetsData as SheetIterator
        while (sheets.hasNext() && currentIdx <= index) {
            sheets.next().use {
                if (currentIdx == index) {
                    content = getRowsFromSheet(it, headerRow)
                }
                currentIdx++
            }
        }
        return content ?: emptyList()
    }

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
        var content: List<Map<String, String>>? = null
        val sheets = reader.sheetsData as SheetIterator
        while (sheets.hasNext() && content == null) {
            sheets.next().use {
                if (sheets.sheetName == name) {
                    content = getRowsFromSheet(it, headerRow)
                }
            }
        }
        return content
            ?: throw IOException("Could not find sheet with name '$name' in the provided Excel file.")
    }

    /**
     * Retrieve all rows from the specified sheet of the Excel workbook.
     *
     * @param data the worksheet from which to retrieve the data
     * @param headerRow index of the row containing headers (0-based)
     * @return a list of rows, each being a mapping from column name to its value
     */
    private fun getRowsFromSheet(
        data: InputStream,
        headerRow: Int,
    ): List<Map<String, String>> {
        val allRows = mutableListOf<Map<String, String>>()
        val rowHandler = RowHandler(headerRow)
        val factory = SAXParserFactory.newInstance()
        val parser = factory.newSAXParser().xmlReader
        val handler =
            XSSFSheetXMLHandler(
                styles,
                strings,
                rowHandler,
                formatter,
                false,
            )
        parser.contentHandler = handler
        val source = InputSource(data)
        parser.parse(source)
        val header = rowHandler.header
        for (row in rowHandler.rows) {
            val rowMapping = mutableMapOf<String, String>()
            header.forEachIndexed { idx, colName ->
                rowMapping[colName] = row.getOrNull(idx) ?: ""
            }
            if (rowMapping.isNotEmpty()) {
                allRows.add(rowMapping)
            }
        }
        return allRows
    }

    /** {@inheritDoc}  */
    @Throws(IOException::class)
    override fun close() {
        pkg.close()
    }

    /**
     * Memory-efficient event-based streaming reader for an Excel file (without loading the entire, heavy
     * structure into memory).
     */
    private class RowHandler(
        val headerIdx: Int = 0,
    ) : SheetContentsHandler {
        private var currentRow = mutableListOf<String>()
        private var currentRowNum = -1
        val header = mutableListOf<String>()
        val rows = mutableListOf<List<String>>()

        /** {@inheritDoc} */
        override fun startRow(rowNum: Int) {
            currentRowNum = rowNum
            currentRow = mutableListOf()
        }

        /** {@inheritDoc} */
        override fun cell(
            cellReference: String?,
            formattedValue: String?,
            comment: XSSFComment?,
        ) {
            currentRow.add(formattedValue ?: "")
        }

        /** {@inheritDoc} */
        override fun endRow(rowNum: Int) {
            // Process the completed row
            if (rowNum == headerIdx) {
                header.addAll(currentRow)
            } else if (rowNum > headerIdx && currentRow.isNotEmpty()) {
                rows.add(currentRow.toList())
            }
        }
    }
}
