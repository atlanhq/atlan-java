/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.xls

import com.atlan.model.enums.AtlanEnum
import com.atlan.pkg.serde.TabularWriter
import org.apache.poi.common.usermodel.HyperlinkType
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import kotlin.math.min

/**
 * Utility class for writing to Excel sheets, using Apache POI.
 *
 * @param workbook Excel workbook in which the sheet exists
 * @param name of the worksheet to create and manage within the workbook
 */
class ExcelSheetWriter(
    private val workbook: Workbook,
    private val name: String,
) : TabularWriter {
    private val worksheet = workbook.createSheet(name)
    private val headerStyle = createHeaderStyle()
    private val dataStyle = createDataStyle()
    private val linkStyle = createLinkStyle()

    /**
     * Create a header row for the worksheet.
     *
     * @param headers ordered map of header names and descriptions
     */
    override fun writeHeader(headers: Map<String, String>) {
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
     * Create a header row for the worksheet.
     *
     * @param values ordered list of header column names
     */
    override fun writeHeader(values: Iterable<String>) {
        val header = worksheet.createRow(0)
        for ((colIdx, name) in values.withIndex()) {
            addHeaderCell(header, colIdx, name, "")
            worksheet.setColumnWidth(
                colIdx,
                min((name.length * 256).toDouble(), (255 * 256).toDouble()).toInt(),
            )
        }
    }

    /**
     * Write a row of data into the worksheet, where key of the map is the column name and the value
     * is the value to write for that column of the row of data.
     * Note: be sure you have first called {@code writeHeader} to output the header row.
     *
     * @param values map keyed by column name with values for the row of data
     */
    override fun writeRecord(values: Map<String, Any?>?) {
        TODO("Not yet implemented")
    }

    /**
     * Add a row of data to the end of a worksheet.
     *
     * @param data the row of data to add
     */
    override fun writeRecord(data: Iterable<Any?>?) {
        if (data != null) {
            val row = worksheet.createRow(worksheet.lastRowNum + 1)
            for ((i, datum) in data.withIndex()) {
                when (datum) {
                    is Double -> addDataCell(row, i, datum)
                    is Long -> addDataCell(row, i, datum)
                    is Boolean -> addDataCell(row, i, datum)
                    is String -> addDataCell(row, i, datum)
                    is AtlanEnum -> addDataCell(row, i, datum.value)
                    else -> addDataCell(row, i, datum?.toString() ?: "")
                }
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
    fun addHeaderCell(
        header: Row,
        index: Int,
        name: String?,
        description: String?,
    ) {
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
    fun addDataCell(
        row: Row,
        index: Int,
        value: String?,
    ) {
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
    fun addDataCell(
        row: Row,
        index: Int,
        value: Boolean,
    ) {
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
    fun addDataCell(
        row: Row,
        index: Int,
        value: Long,
    ) {
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
    fun addDataCell(
        row: Row,
        index: Int,
        value: Double,
    ) {
        val cell = row.createCell(index, CellType.NUMERIC)
        cell.setCellValue(value)
        cell.cellStyle = dataStyle
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
