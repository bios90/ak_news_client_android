package com.dimfcompany.aknewsclient.logic.utils.pdf

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.dimfcompany.aknewsclient.base.Constants
import com.dimfcompany.aknewsclient.base.enums.TypeNewsCategory
import com.dimfcompany.aknewsclient.base.extensions.scaleToMaxSize
import com.dimfcompany.aknewsclient.logic.models.ModelEvent
import com.dimfcompany.aknewsclient.logic.models.ModelNews
import com.dimfcompany.aknewsclient.logic.utils.DateManager
import com.dimfcompany.aknewsclient.logic.utils.StringManager
import com.dimfcompany.aknewsclient.logic.utils.files.FileManager
import com.dimfcompany.aknewsclient.logic.utils.files.MyFileItem
import com.dimfcompany.aknewsclient.logic.utils.formatToString
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.rucode.autopass.logic.utils.images.GlideManager
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream

class PdfCreator
{
    lateinit var globalReg: BaseFont
    lateinit var globalBold: BaseFont
    lateinit var globalBoldItalic: BaseFont
    lateinit var globalFaw: BaseFont

    lateinit var colorYellow: BaseColor
    lateinit var colorRed: BaseColor
    lateinit var colorGray: BaseColor
    lateinit var colorBlue: BaseColor

    lateinit var fontTitle: Font
    lateinit var fontText: Font
    lateinit var fontAuthor: Font
    lateinit var fontEmail: Font
    lateinit var fontFaw: Font
    lateinit var fontFawTitle: Font

    suspend fun generateDocument(news_list: ArrayList<ModelNews>, event: ModelEvent): MyFileItem
    {
        initAll()
        val file = FileManager.createPdfFile(StringManager.getNameForNewFile("pdf"))
        val document = Document(PageSize.A4, 0f, 0f, 20f, 20f)
        val fos = FileOutputStream(file)
        val writer = PdfWriter.getInstance(document, fos)
        writer.setCompressionLevel(9)
        document.open()

        val text_title = event.category?.getNameForDisplay() + " от " + event.date?.formatToString(DateManager.FORMAT_FOR_DISPLAY_FULL_MONTH)
        val par = Paragraph(text_title, fontTitle)
        par.alignment = Element.ALIGN_CENTER
        document.add(par)

        var last_category: TypeNewsCategory? = null

        for (news in news_list)
        {
            if (last_category == null || last_category != news.category && news.category != null)
            {
                document.addSpace(20f)
                document.add(getCategoryTable(news.category!!))
            }

            document.addSpace(20f)
            document.add(getNewsTable(news))

            last_category = news.category
        }

        document.close()
        fos.flush()
        fos.close()
        return MyFileItem.createFromFile(file)
    }

    private suspend fun getNewsTable(news: ModelNews): PdfPTable
    {
        val table = PdfPTable(4)
        table.setWidths(intArrayOf(10, 10, 10, 10))
        table.setWidthPercentage(90f)
        table.setTableEvent(TableDrawLineEvent())
        table.keepTogether = true


        if (!news.images.isNullOrEmpty())
        {
            for (img in news.images!!)
            {
                val url = img.url ?: continue
                val bitmap = GlideManager.getImageAsBitmap(url) ?: continue
                val scaled_bitmap = bitmap.scaleToMaxSize(800)

                val image = getImageFromBitmap(scaled_bitmap)
                val cell = PdfPCell(image, true)
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.verticalAlignment = Element.ALIGN_MIDDLE
                cell.setPadding(4f)
                cell.border = Rectangle.NO_BORDER
                table.addCell(cell)
            }

            val count_free = news.images!!.size % 4
            for (i in 0 until 4 - count_free)
            {
                BuilderCell().setBorder(false).build().addToTable(table)
            }
        }

        if (news.author_name != null)
        {
            BuilderCell()
                    .setBorder(false)
                    .setFont(fontAuthor)
                    .setText(news.author_name!!)
                    .setColSpan(4)
                    .setAlignHor(Element.ALIGN_RIGHT)
                    .build()
                    .addToTable(table)
        }

        if (news.author_email != null)
        {
            BuilderCell()
                    .setBorder(false)
                    .setFont(fontEmail)
                    .setText(news.author_email!!)
                    .setColSpan(4)
                    .setAlignHor(Element.ALIGN_RIGHT)
                    .build()
                    .addToTable(table)
        }

        if (news.name != null)
        {
            BuilderCell()
                    .setBorder(false)
                    .setFont(fontTitle)
                    .setText(news.name!!)
                    .setColSpan(4)
                    .setAlignHor(Element.ALIGN_LEFT)
                    .build()
                    .addToTable(table)
        }

        if (news.text != null)
        {
            BuilderCell()
                    .setBorder(false)
                    .setFont(fontText)
                    .setText(news.text!!)
                    .setColSpan(4)
                    .setAlignHor(Element.ALIGN_LEFT)
                    .build()
                    .addToTable(table)
        }

        return table
    }

    private fun getCategoryTable(category: TypeNewsCategory): PdfPTable
    {
        val table = PdfPTable(3)
        table.setWidths(intArrayOf(10, 30, 10))
        table.setWidthPercentage(100f)
        table.keepTogether = true

        BuilderCell()
                .setBorder(false)
                .setBgColor(colorBlue)
                .build()
                .addToTable(table)

        BuilderCell().setFont(fontFawTitle)
                .setBorder(false)
                .setText(category.getNameForDisplay())
                .setBgColor(colorBlue)
                .build()
                .addToTable(table)

        BuilderCell()
                .setBorder(false)
                .setBgColor(colorBlue)
                .build()
                .addToTable(table)

        return table
    }

    private fun initAll()
    {
        globalReg = BaseFont.createFont("assets/exo_reg.ttf", "Cp1251", BaseFont.EMBEDDED)
        globalBold = BaseFont.createFont("assets/exo_bold.ttf", "Cp1251", BaseFont.EMBEDDED)
        globalBoldItalic = BaseFont.createFont("assets/exo_bold_italic.ttf", "Cp1251", BaseFont.EMBEDDED)
        globalFaw = BaseFont.createFont("assets/fa_solid.ttf", "utf8", BaseFont.EMBEDDED)

        colorRed = BaseColor(231, 39, 30)
        colorYellow = BaseColor(255, 117, 24)
        colorGray = BaseColor(138, 138, 138)
        colorBlue = BaseColor(2, 152, 211)

        fontTitle = Font(globalBold, 14f, Font.BOLD, colorRed)
        fontText = Font(globalReg, 8f)
        fontEmail = Font(globalBoldItalic, 10f, Font.BOLDITALIC, colorYellow)
        fontAuthor = Font(globalReg, 10f, Font.NORMAL, colorGray)
        fontFaw = Font(globalFaw, 12f, Font.NORMAL, BaseColor.WHITE)
        fontFawTitle = Font(globalReg, 12f, Font.NORMAL, BaseColor.WHITE)
    }
}

fun getImageFromBitmap(bitmap: Bitmap): Image
{
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

    val image = Image.getInstance(stream.toByteArray())

    stream.flush()
    stream.close()

    return image
}

class TableDrawLineEvent : PdfPTableEvent
{
    override fun tableLayout(table: PdfPTable?, widths: Array<out FloatArray>?, heights: FloatArray?, headerRows: Int, rowStart: Int, canvases: Array<out PdfContentByte>?)
    {
        val width = widths?.get(0) ?: return
        val x1 = width[0]
        val x2 = width[width.size - 1]
        val y1 = heights?.get(0) ?: return
        val y2 = heights.get(heights.size - 1)

        val cb = canvases?.get(PdfPTable.LINECANVAS) ?: return
        cb.rectangle(x1, y1, x2 - x1, y2 - y1)
        cb.stroke()
        cb.resetRGBColorStroke()
    }
}