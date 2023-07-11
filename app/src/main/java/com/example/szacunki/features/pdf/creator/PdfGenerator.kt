package com.example.szacunki.features.pdf.creator

import android.content.Context
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.extensions.prepareDateToDisplay
import com.example.szacunki.core.extensions.prepareDateToSave
import com.example.szacunki.core.extensions.toLocalDateTime
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
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
import java.nio.file.Paths
import kotlin.io.path.exists

object PdfGenerator {

    fun generatePdf(context: Context, estimation: EstimationDisplayable): String {

        val dirPath = "/data/data/com.example.szacunki/files/estimationPdf"
        val dirFilePath = if (Paths.get(dirPath).exists()) {
            Paths.get(dirPath)
        } else {
            Files.createDirectory(Paths.get(dirPath))
        }

        val fileName = estimation.sectionNumber + "_" + estimation.date.toLocalDateTime()
            .prepareDateToSave() + ".pdf"
//        val filePath = context.filesDir
//        val file = File(filePath, fileName)

        val file = File(dirFilePath.toFile(), fileName)

        val fOut = FileOutputStream(file)

        val pdfWriter = PdfWriter(fOut)
        //Creating a PDF
        val pdfDocument = PdfDocument(pdfWriter)
        val layoutDocument = Document(pdfDocument)

        val fontProgram = FontProgramFactory.createFont();
        val font = PdfFontFactory.createFont(fontProgram, "CP1250")
        val memo = "Notatka Służbowa:"
        //set font with  polish characters
        layoutDocument.setFont(font)
        layoutDocument.setMargins(10F, 45F, 10F, 44F)


        //title
        addMemoTitle(layoutDocument, memo, estimation.date.toString())

        //memo
        addMemo(layoutDocument, estimation.memo)


        //add table
        estimation.trees.forEachIndexed { index, tree ->
            addTable(layoutDocument, tree, estimation.sectionNumber)
            layoutDocument.add(
                Paragraph(
                    estimation.date.toLocalDateTime().prepareDateToDisplay()
                ).setTextAlignment(TextAlignment.CENTER)
            )
            if (index != estimation.trees.lastIndex) {
                layoutDocument.add(AreaBreak())
            }


        }


        layoutDocument.close()
        return file.absolutePath

    }

    private fun addTable(
        layoutDocument: Document, treeDisplayable: TreeDisplayable, sectionNumber: String
    ) {

        val color = color2
        val colorGreen = DeviceRgb(color.red, color.green, color.blue)
        val title = treeDisplayable.name + ", " + "oddział: $sectionNumber"
        addTreeTitle(layoutDocument, title)
        val table = Table(
            UnitValue.createPointArray(
                floatArrayOf(
                    100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f

                )
            )
        )

        addTitleCell(table, "Średnica", colorGreen)
        addTitleCell(table, "Klasa_1", colorGreen)
        addTitleCell(table, "Klasa_2", colorGreen)
        addTitleCell(table, "Klasa_3", colorGreen)
        addTitleCell(table, "Klasa_A", colorGreen)
        addTitleCell(table, "Klasa_B", colorGreen)
        addTitleCell(table, "Klasa_C", colorGreen)
        addTitleCell(table, "Wysokość", colorGreen)

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

    private fun addMemoTitle(layoutDocument: Document, header: String, date: String) {
        layoutDocument.add(
            Cell().add(
                Paragraph(header).setBold().setUnderline().setTextAlignment(TextAlignment.LEFT)
            ).setFontSize(16F)
        )

    }

    private fun addTreeTitle(layoutDocument: Document, header: String) {
        layoutDocument.add(
            Paragraph(header).setBold().setFontSize(20F).setTextAlignment(TextAlignment.LEFT)
        )

    }

    private fun addMemo(layoutDocument: Document, memo: String) {
        layoutDocument.add(Paragraph(memo).setFontSize(12F))
    }

    private fun addTitleCell(table: Table, text: String, color: Color) {
        table.addCell(
            Cell().add(
                Paragraph(text).setFontColor(ColorConstants.WHITE).setBold()
                    .setTextAlignment(TextAlignment.CENTER)
            )
                .setBackgroundColor(color)
        )
    }

    private fun addBodyCell(table: Table, text: String, color: Color) {
        table.addCell(
            Cell().add(Paragraph(text).setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(color)
        )
    }


}