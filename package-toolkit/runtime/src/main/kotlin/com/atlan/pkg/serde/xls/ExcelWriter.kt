/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.xls

import com.atlan.model.enums.AtlanEnum
import org.apache.poi.common.usermodel.HyperlinkType
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.Closeable
import java.io.FileOutputStream
import kotlin.math.min

/**
 * Utility class for writing to Excel files, using Apache POI.
 *
 * @param path location and filename of the Excel file to write
 */
class ExcelWriter @JvmOverloads constructor(
    val path: String,
    private val rowAccessWindowSize: Int = 100,
) : Closeable {
    private val workbook = SXSSFWorkbook(rowAccessWindowSize)
    private val headerStyle = createHeaderStyle()
    private val dataStyle = createDataStyle()
    private val linkStyle = createLinkStyle()

    /**
     * Create a new worksheet within the workbook.
     *
     * @param name of the sheet to create
     * @return the worksheet
     */
    fun createSheet(name: String): Sheet {
        return workbook.createSheet(name)
    }

    /**
     * Create a header row for the worksheet.
     *
     * @param worksheet in which to create the header row
     * @param headers ordered map of header names and descriptions
     */
    fun addHeader(worksheet: Sheet, headers: Map<String, String>) {
        val header = worksheet.createRow(0)
        var colIdx = 0
        for ((name, desc) in headers) {
            addHeaderCell(header, colIdx, name, desc)
            worksheet.setColumnWidth(
                colIdx,
                min((name.length * 256).toDouble(), (255 * 256).toDouble()).toInt(),
            )
            colIdx++
        }
    }

    /**
     * Add a row of data to the end of a worksheet.
     *
     * @param worksheet the worksheet into which to add the row
     * @param data the row of data to add
     */
    fun appendRow(worksheet: Sheet, data: List<Any>) {
        val row = worksheet.createRow(worksheet.lastRowNum + 1)
        for (i in data.indices) {
            when (val datum = data[i]) {
                is Double -> addDataCell(row, i, datum)
                is Long -> addDataCell(row, i, datum)
                is Boolean -> addDataCell(row, i, datum)
                is String -> addDataCell(row, i, datum)
                is AtlanEnum -> addDataCell(row, i, datum.value)
                else -> addDataCell(row, i, datum.toString())
            }
        }
    }

    /**
     * Add a header cell to the worksheet.
     *
     * @param header row for the header
     * @param index location of the cell (column index)
     * @param name of the header column
     * @param description of the header column (will be put into a comment)
     */
    fun addHeaderCell(header: Row, index: Int, name: String?, description: String?) {
        val drawing = header.sheet.createDrawingPatriarch()
        val factory = workbook.creationHelper
        val cell = header.createCell(index, CellType.STRING)
        cell.setCellValue(name)
        cell.cellStyle = headerStyle
        val anchor = factory.createClientAnchor()
        anchor.setCol1(cell.columnIndex)
        anchor.setCol2(cell.columnIndex + 1)
        anchor.row1 = cell.rowIndex
        anchor.row2 = cell.rowIndex + 1
        val comment = drawing.createCellComment(anchor)
        val str = factory.createRichTextString(description)
        comment.isVisible = false
        comment.string = str
        cell.cellComment = comment
    }

    /**
     * Add a data cell with a string value.
     *
     * @param row in which to add the cell
     * @param index column for the cell
     * @param value to set for the cell
     */
    fun addDataCell(row: Row, index: Int, value: String?) {
        val cell = row.createCell(index, CellType.STRING)
        if (value == null) {
            cell.setCellValue("")
            cell.cellStyle = dataStyle
        } else {
            // Maximum length of a cell in Excel is 32767 characters, so
            // if the value exceeds this truncate it and replace the ending
            // with ellipses
            if (value.length > 32767) {
                cell.setCellValue(value.substring(0, 32764) + "...")
            } else {
                cell.setCellValue(value)
            }
            if (value.startsWith("https://")) {
                val link = workbook.creationHelper.createHyperlink(HyperlinkType.URL)
                link.address = value
                cell.hyperlink = link
                cell.cellStyle = linkStyle
            } else {
                cell.cellStyle = dataStyle
            }
        }
    }

    /**
     * Add a data cell with a boolean value.
     *
     * @param row in which to add the cell
     * @param index column for the cell
     * @param value to set for the cell
     */
    fun addDataCell(row: Row, index: Int, value: Boolean) {
        val cell = row.createCell(index, CellType.BOOLEAN)
        cell.setCellValue(value)
        cell.cellStyle = dataStyle
    }

    /**
     * Add a data cell with a numeric value.
     *
     * @param row in which to add the cell
     * @param index column for the cell
     * @param value to set for the cell
     */
    fun addDataCell(row: Row, index: Int, value: Long) {
        val cell = row.createCell(index, CellType.NUMERIC)
        cell.setCellValue(value.toDouble())
        cell.cellStyle = dataStyle
    }

    /**
     * Add a data cell with a numeric value.
     *
     * @param row in which to add the cell
     * @param index column for the cell
     * @param value to set for the cell
     */
    fun addDataCell(row: Row, index: Int, value: Double) {
        val cell = row.createCell(index, CellType.NUMERIC)
        cell.setCellValue(value)
        cell.cellStyle = dataStyle
    }

    /** {@inheritDoc} */
    override fun close() {
        val fos = FileOutputStream(path)
        workbook.write(fos)
        workbook.close()
        workbook.dispose() // cleanup temporary files
        fos.close()
    }

    /**
     * Create styling for a header row.
     * @return style
     */
    private fun createHeaderStyle(): CellStyle {
        val style = workbook.createCellStyle()
        style.alignment = HorizontalAlignment.LEFT
        style.borderBottom = BorderStyle.THICK
        style.bottomBorderColor = IndexedColors.BLACK1.getIndex()
        val font = workbook.createFont()
        font.fontName = "Helvetica"
        font.bold = true
        style.setFont(font)
        return style
    }

    /**
     * Create styling for a data row.
     * @return style
     */
    private fun createDataStyle(): CellStyle {
        val style = workbook.createCellStyle()
        style.alignment = HorizontalAlignment.LEFT
        val font = workbook.createFont()
        font.fontName = "Helvetica"
        font.bold = false
        style.setFont(font)
        return style
    }

    /**
     * Create styling for a link within a cell.
     * @return style
     */
    private fun createLinkStyle(): CellStyle {
        val style = workbook.createCellStyle()
        style.alignment = HorizontalAlignment.LEFT
        val font = workbook.createFont()
        font.fontName = "Helvetica"
        font.bold = false
        font.underline = Font.U_SINGLE
        font.color = IndexedColors.BLUE.getIndex()
        style.setFont(font)
        return style
    }
}
