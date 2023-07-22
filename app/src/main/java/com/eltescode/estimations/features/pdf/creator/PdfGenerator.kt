package com.eltescode.estimations.features.pdf.creator

import android.content.Context
import com.eltescode.estimations.core.extensions.createPdfFileName
import com.eltescode.estimations.core.extensions.prepareDateToDisplay
import com.eltescode.estimations.core.extensions.toLocalDateTime
import com.eltescode.estimations.features.estimation.presentation.model.EstimationDisplayable
import com.eltescode.estimations.features.estimation.presentation.model.TreeDisplayable
import com.eltescode.estimations.ui.theme.color2
import com.eltescode.estimations.R
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

object PdfGenerator {
    fun generatePdf(context: Context, estimation: EstimationDisplayable): String {

        val dirFilePath = createPath(context = context)
        val file = createFile(estimation = estimation, dirFilePath = dirFilePath)
        val layoutDocument = createDocument(file)

        setDocumentConfigurations(layoutDocument)
        addTables(context = context, estimation = estimation, layoutDocument = layoutDocument)
        addMemoTitle(layoutDocument, context.getString(R.string.hint8))
        addMemo(layoutDocument, estimation.memo)
        layoutDocument.close()
        return file.absolutePath
    }

    private fun createPath(context: Context): Path {
        val dirStringPath = context.getString(R.string.pdf_path)
        return if (Paths.get(dirStringPath).exists()) {
            Paths.get(dirStringPath)
        } else {
            Files.createDirectory(Paths.get(dirStringPath))
        }
    }

    private fun createFile(estimation: EstimationDisplayable, dirFilePath: Path): File {
        val fileName = estimation.createPdfFileName()
        return File(dirFilePath.toFile(), fileName)
    }

    private fun createDocument(file: File): Document {
        val fOut = FileOutputStream(file)
        val pdfWriter = PdfWriter(fOut)
        val pdfDocument = PdfDocument(pdfWriter)
        return Document(pdfDocument)
    }

    private fun setDocumentConfigurations(layoutDocument: Document) {
        layoutDocument.apply {
            setFont(createPolishFont())
            setMargins(10F, 45F, 10F, 44F)
        }
    }

    private fun createPolishFont(): PdfFont {
        val fontProgram = FontProgramFactory.createFont(StandardFonts.COURIER)
        return PdfFontFactory.createFont(fontProgram, PdfEncodings.CP1250, false)
    }

    private fun addTables(
        context: Context,
        estimation: EstimationDisplayable,
        layoutDocument: Document
    ) {
        estimation.trees.forEachIndexed { index, tree ->
            addTable(
                context = context,
                layoutDocument = layoutDocument,
                treeDisplayable = tree,
                sectionNumber = estimation.sectionNumber
            )
            layoutDocument.add(
                Paragraph(estimation.date.toLocalDateTime().prepareDateToDisplay())
                    .setTextAlignment(TextAlignment.CENTER)
            )
            if (index != estimation.trees.lastIndex) {
                layoutDocument.add(AreaBreak())
            }
        }
    }

    private fun addTable(
        context: Context,
        layoutDocument: Document,
        treeDisplayable: TreeDisplayable,
        sectionNumber: String
    ) {
        val colorGreen = DeviceRgb(color2.red, color2.green, color2.blue)
        val title =
            treeDisplayable.name.createTitle(context = context, sectionNumber = sectionNumber)
        addTreeTitle(layoutDocument, title)
        val table = Table(
            UnitValue.createPointArray(floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f))
        )
        addTitleCell(table, context.getString(R.string.hint11), colorGreen)
        addTitleCell(table, context.getString(R.string.hint34), colorGreen)
        addTitleCell(table, context.getString(R.string.hint35), colorGreen)
        addTitleCell(table, context.getString(R.string.hint36), colorGreen)
        addTitleCell(table, context.getString(R.string.hint37), colorGreen)
        addTitleCell(table, context.getString(R.string.hint38), colorGreen)
        addTitleCell(table, context.getString(R.string.hint39), colorGreen)
        addTitleCell(table, context.getString(R.string.hint40), colorGreen)

        treeDisplayable.treeRows.forEachIndexed { index, item ->
            val color = if (index % 2 != 0) {
                ColorConstants.LIGHT_GRAY
            } else {
                ColorConstants.WHITE
            }
            addBodyCell(table, item.diameter, color)
            addBodyCell(table, item.treeQualityClasses.class1.toString(), color)
            addBodyCell(table, item.treeQualityClasses.class2.toString(), color)
            addBodyCell(table, item.treeQualityClasses.class3.toString(), color)
            addBodyCell(table, item.treeQualityClasses.classA.toString(), color)
            addBodyCell(table, item.treeQualityClasses.classB.toString(), color)
            addBodyCell(table, item.treeQualityClasses.classC.toString(), color)
            addBodyCell(table, item.height.toString(), color)
        }
        layoutDocument.add(table)
    }

    private fun addMemoTitle(layoutDocument: Document, header: String) {
        layoutDocument.add(
            Cell().add(
                Paragraph(header).setBold().setFontSize(20F).setUnderline()
                    .setTextAlignment(TextAlignment.LEFT)
            )
        )
    }

    private fun addMemo(layoutDocument: Document, memo: String) {
        layoutDocument.add(Paragraph(memo).setFontSize(12F))
    }

    private fun addTreeTitle(layoutDocument: Document, header: String) {
        layoutDocument.add(
            Paragraph(header).setBold().setFontSize(20F).setTextAlignment(TextAlignment.LEFT)
        )
    }

    private fun addTitleCell(table: Table, text: String, color: Color) {
        table.addCell(
            Cell().add(
                Paragraph(text).setFontColor(ColorConstants.WHITE).setBold()
                    .setTextAlignment(TextAlignment.CENTER)
            ).setBackgroundColor(color)
        )
    }

    private fun addBodyCell(table: Table, text: String, color: Color) {
        table.addCell(
            Cell().add(Paragraph(text).setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(color)
        )
    }

    private fun String.createTitle(context: Context, sectionNumber: String) =
        this + ", " + context.getString(R.string.hint33, sectionNumber)
}