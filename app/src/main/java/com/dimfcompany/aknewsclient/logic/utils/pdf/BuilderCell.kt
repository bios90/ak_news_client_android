package com.dimfcompany.aknewsclient.logic.utils.pdf

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable

class BuilderCell
{
    var text: String? = null
        private set
    var font: Font? = null
        private set
    var border: Boolean = true
        private set
    var col_span: Int = 1
        private set
    var row_span: Int = 1
        private set
    var align_hor: Int = Element.ALIGN_CENTER
        private set
    var aligh_vert: Int = Element.ALIGN_MIDDLE
        private set
    var padding: Float = 4f
        private set
    var bg_color: BaseColor? = null
        private set

    fun setText(text: String) = apply(
        {
            this.text = text
        })

    fun setFont(font: Font) = apply(
        {
            this.font = font
        })

    fun setBorder(border: Boolean) = apply(
        {
            this.border = border
        })

    fun setColSpan(col_span: Int) = apply(
        {
            this.col_span = col_span
        })

    fun setRowSpan(row_span: Int) = apply(
        {
            this.row_span = row_span
        })

    fun setAlignHor(align_hor: Int) = apply(
        {
            this.align_hor = align_hor
        })

    fun setAlignVert(aligh_vert: Int) = apply(
        {
            this.aligh_vert = aligh_vert
        })

    fun setPadding(padding: Float) = apply(
        {
            this.padding = padding
        })

    fun setBgColor(color: BaseColor?) = apply(
        {
            this.bg_color = color
        })

    fun build(): PdfPCell
    {
        var cell: PdfPCell
        if (font == null)
        {
            cell = PdfPCell()
        }
        else
        {
            cell = PdfPCell(Paragraph(text ?: "", font))
        }

        if (border)
        {
            cell.border = Rectangle.BOX
        }
        else
        {
            cell.border = Rectangle.NO_BORDER
        }

        cell.colspan = col_span
        cell.rowspan = row_span

        cell.horizontalAlignment = align_hor
        cell.verticalAlignment = aligh_vert

        cell.setPadding(padding)

        if (bg_color != null)
        {
            cell.backgroundColor = bg_color
        }

        return cell
    }
}

fun PdfPCell.addToTable(table: PdfPTable)
{
    table.addCell(this)
}

fun Document.addSpace(space: Float)
{
    this.add(Paragraph(space, "\u00a0"))
}