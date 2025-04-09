package com.ahmer.accounts.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel
import com.ahmer.accounts.utils.HelperUtils.roundValue
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter

object PdfUtils {

    private const val PAGE_MARGIN = 54F // 0.75 inch (72 * 0.75)
    private const val FONT_SIZE_SMALL = 8F
    private const val FONT_SIZE_MEDIUM = 10F
    private const val FONT_SIZE_LARGE = 12F

    /**
     * Creates an [Intent] to prompt the user to choose a location for saving a generated PDF.
     *
     * @param context The [Context] used to resolve resources.
     * @param transactions List of [TransactionEntity] representing the transaction details.
     * @return An [Intent] to create a document if transactions exist; otherwise, `null`.
     */
    fun createPdfIntent(context: Context, transactions: List<TransactionEntity>?): Intent? {
        if (transactions.isNullOrEmpty()) {
            HelperUtils.showToast(
                context = context,
                msg = context.getString(R.string.toast_pdf_not_generated)
            )
            return null
        }

        val fileName = HelperUtils.getDateTime(
            time = System.currentTimeMillis(), pattern = Constants.PATTERN_FILE_NAME
        ) + ".pdf"

        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            val mimeType = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, fileName)
            type = mimeType
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
    }

    /**
     * Generates a PDF using the provided [Uri] and detailed content parameters.
     *
     * @param context The [Context] used to resolve resources.
     * @param uri A [Uri] where the PDF is to be saved.
     * @param transactions List of [TransactionEntity] containing transaction details.
     * @param sumModel A [TransactionSumModel] containing overall sums for debit, credit and balance.
     * @param person A [PersonEntity] representing the account holder.
     * @return `true` if the PDF generation is successful; otherwise, `false`.
     */
    fun generatePdf(
        context: Context,
        uri: Uri,
        transactions: List<TransactionEntity>,
        sumModel: TransactionSumModel,
        person: PersonEntity
    ): Boolean {
        val document = Document(PageSize.A4, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN)

        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                PdfWriter.getInstance(document, outputStream).apply {
                    pageEvent = HeaderFooterPageEvent(context = context)
                }

                document.setupMetadata(context = context, person = person)
                document.open()
                document.addHeaderContent(person = person)
                document.addTransactionTable(
                    context = context, transactions = transactions, sumModel = sumModel
                )
                document.close()
            }
            true
        } catch (e: Exception) {
            when (e) {
                is DocumentException, is NullPointerException -> {
                    logError(message = "PDF generation failed", exception = e)
                }

                else -> {
                    logError(message = "Unexpected error during PDF generation", exception = e)
                }
            }
            false
        }
    }

    /**
     * Sets up the metadata for the PDF document.
     *
     * @param context The current [Context].
     * @param person A [PersonEntity] whose name is used in the metadata.
     */
    private fun Document.setupMetadata(context: Context, person: PersonEntity) {
        val appName = context.getString(R.string.app_name)
        open()
        addAuthor(appName)
        addCreationDate()
        addCreator(appName)
        addKeywords("${person.name}, Statement, Balance, Sheet")
        addSubject(appName)
        addTitle(person.name)
    }

    /**
     * Adds header content (account holder's name, address, and statement title) to the PDF.
     *
     * @param person A [PersonEntity] containing the account holder's details.
     */
    private fun Document.addHeaderContent(person: PersonEntity) {
        val nameFont = createFont(size = FONT_SIZE_MEDIUM, isBold = true)
        val addressFont = createFont(size = FONT_SIZE_SMALL)
        val statementFont = createFont(size = FONT_SIZE_LARGE, isBold = true)

        add(Paragraph(person.name, nameFont).apply {
            spacingBefore = 10F
            alignment = Element.ALIGN_LEFT
        })
        add(Paragraph(person.address, addressFont).apply {
            spacingAfter = 10F
            alignment = Element.ALIGN_LEFT
        })
        add(Paragraph("Statement of Account".uppercase(), statementFont).apply {
            spacingAfter = 10F
            alignment = Element.ALIGN_CENTER
        })
    }

    /**
     * Adds a table of transactions, a totals table, and summary information for the PDF document.
     *
     * @param context The current [Context] used to resolve string resources.
     * @param transactions A list of [TransactionEntity] representing individual transactions.
     * @param sumModel A [TransactionSumModel] with the sum of debits, credits, and balance.
     */
    private fun Document.addTransactionTable(
        context: Context,
        transactions: List<TransactionEntity>,
        sumModel: TransactionSumModel
    ) {
        val table = createMainTable(context = context)
        val sortedTransactions = transactions.sortedBy { it.id }

        var currentBalance = 0.0
        var creditCount = 0
        var debitCount = 0

        sortedTransactions.forEach { transaction ->
            table.addDateCell(text = transaction.shortDate)
            table.addDescriptionCell(text = transaction.description)

            val (debit, credit) = when (transaction.type) {
                Constants.TYPE_CREDIT -> {
                    creditCount++
                    Pair(0.0, transaction.amount.toDouble())
                }

                else -> {
                    debitCount++
                    Pair(transaction.amount.toDouble(), 0.0)
                }
            }

            currentBalance += credit - debit
            table.addNumericCell(value = debit)
            table.addNumericCell(value = credit)
            table.addNumericCell(value = currentBalance, isBal = true)
        }

        add(table)
        add(createTotalTable(context = context, sumModel = sumModel))
        addTransactionSummary(context = context, creditCount = creditCount, debitCount = debitCount)
    }

    /**
     * Creates the main table for listing transactions with header cells.
     *
     * @param context The current [Context] to resolve string resources.
     */
    private fun createMainTable(context: Context): PdfPTable {
        return PdfPTable(5).apply {
            widthPercentage = 100F
            setTotalWidth(floatArrayOf(54F, 163F, 90F, 90F, 90F))
            isLockedWidth = true

            listOf(
                context.getString(R.string.label_date),
                context.getString(R.string.label_description),
                Constants.TYPE_DEBIT,
                Constants.TYPE_CREDIT,
                context.getString(R.string.label_balance)
            ).forEach { header ->
                addHeaderCell(text = header.uppercase())
            }
        }
    }

    /**
     * Adds a header cell with centered text to the table.
     */
    private fun PdfPTable.addHeaderCell(text: String) {
        addCell(createCell(text = text, isHeading = true, alignment = Element.ALIGN_CENTER))
    }

    /**
     * Adds a date cell with centered text to the table.
     */
    private fun PdfPTable.addDateCell(text: String) {
        addCell(createCell(text = text, alignment = Element.ALIGN_CENTER))
    }

    /**
     * Adds a description cell with left-aligned text to the table.
     */
    private fun PdfPTable.addDescriptionCell(text: String) {
        addCell(createCell(text = text, alignment = Element.ALIGN_LEFT))
    }

    /**
     * Adds a numeric cell to the table.
     * Displays the [value] rounded if nonzero, or an empty string unless [isBal] is true.
     *
     * @param value The numeric value to display.
     * @param isBal If true, always display the value (for balance column).
     */
    private fun PdfPTable.addNumericCell(value: Double, isBal: Boolean = false) {
        val rounded = roundValue(value = value)
        val text = when {
            isBal -> rounded  // Always show formatted value for balance
            value != 0.0 -> rounded  // Show value for non-zero debit/credit
            else -> ""  // Empty for zero in debit/credit
        }
        addCell(createCell(text = text, alignment = Element.ALIGN_RIGHT))
    }

    /**
     * Creates a totals table for debit, credit, and balance.
     *
     * @param context The current [Context] to resolve string resources.
     * @param sumModel A [TransactionSumModel] that holds the totals.
     */
    private fun createTotalTable(context: Context, sumModel: TransactionSumModel): PdfPTable {
        return PdfPTable(4).apply {
            widthPercentage = 100F
            setTotalWidth(floatArrayOf(217F, 90F, 90F, 90F))
            isLockedWidth = true

            addCell(
                createCell(
                    text = context.getString(R.string.label_total).uppercase(),
                    isTotal = true,
                    alignment = Element.ALIGN_CENTER
                )
            )
            addCell(
                createCell(
                    text = roundValue(value = sumModel.debitSum),
                    isTotal = true,
                    alignment = Element.ALIGN_RIGHT
                )
            )
            addCell(
                createCell(
                    text = roundValue(value = sumModel.creditSum),
                    isTotal = true,
                    alignment = Element.ALIGN_RIGHT
                )
            )
            addCell(
                createCell(
                    text = roundValue(value = sumModel.balance),
                    isTotal = true,
                    alignment = Element.ALIGN_RIGHT
                )
            )
        }
    }

    /**
     * Adds a summary of transaction counts (credits and debits) to the document.
     *
     * @param context The current [Context] to resolve string resources.
     * @param creditCount Number of credit transactions.
     * @param debitCount Number of debit transactions.
     */
    private fun Document.addTransactionSummary(
        context: Context,
        creditCount: Int,
        debitCount: Int
    ) {
        val font = createFont(size = FONT_SIZE_SMALL)
        add(Paragraph(context.getString(R.string.label_pdf_credit, creditCount), font).apply {
            spacingBefore = 10F
            alignment = Element.ALIGN_LEFT
        })
        add(Paragraph(context.getString(R.string.label_pdf_debit, debitCount), font))
    }

    /**
     * Creates and returns a [PdfPCell] with the specified text and styling.
     *
     * @param text The text to show inside the cell.
     * @param isHeading Whether the cell represents a header.
     * @param isTotal Whether the cell is part of a totals table.
     * @param alignment The horizontal alignment of the text.
     */
    private fun createCell(
        text: String,
        isHeading: Boolean = false,
        isTotal: Boolean = false,
        alignment: Int
    ): PdfPCell {
        val font = when {
            isHeading || isTotal -> createFont(size = FONT_SIZE_MEDIUM, isBold = true)
            else -> createFont(size = FONT_SIZE_SMALL)
        }

        return PdfPCell(Phrase(text, font)).apply {
            verticalAlignment = Element.ALIGN_MIDDLE
            horizontalAlignment = alignment
            paddingTop = if (isHeading) 5F else 3F
            paddingBottom = when {
                isHeading -> 8F
                isTotal -> 7F
                else -> 5F
            }
        }
    }

    /**
     * Creates a [Font] object using the specified parameters.
     *
     * @param family The font family to use. Defaults to [Font.FontFamily.HELVETICA].
     * @param size The font size; defaults to [FONT_SIZE_SMALL].
     * @param isBold Whether the font should be bold.
     * @param color The color of the text; defaults to [BaseColor.BLACK].
     */
    private fun createFont(
        family: Font.FontFamily = Font.FontFamily.HELVETICA,
        size: Float = FONT_SIZE_SMALL,
        isBold: Boolean = false,
        color: BaseColor = BaseColor.BLACK
    ): Font {
        return Font(family, size, if (isBold) Font.BOLD else Font.NORMAL, color)
    }

    /**
     * Custom [PdfPageEventHelper] that adds header and footer content to each page.
     *
     * Uses variables such as the application name, timestamp (from [Constants.PATTERN_PDF]),
     * play store link, and a dedicated font.
     */
    private class HeaderFooterPageEvent(context: Context) : PdfPageEventHelper() {
        private val appName = context.getString(R.string.app_name)
        private val timestamp = HelperUtils.getDateTime(
            System.currentTimeMillis(), Constants.PATTERN_PDF
        )
        private val playStoreLink = HelperUtils.playStoreLink(context)
        private val font = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, BaseColor.BLACK)

        override fun onStartPage(writer: PdfWriter, document: Document?) {
            with(writer.directContent) {
                // Left-aligned app name
                ColumnText.showTextAligned(
                    this,
                    Element.ALIGN_CENTER,
                    Phrase(appName, font),
                    72F,   //Start page margin 1 inch 72 (0+72)
                    806F,  //Top margin 50% of a inch (842-36)
                    0F
                )

                // Right-aligned date
                ColumnText.showTextAligned(
                    this,
                    Element.ALIGN_CENTER,
                    Phrase(timestamp, font),
                    487F, //End page margin 1 inch 72 + adding more 50% inch 36 because date string long (595-108)
                    806F, //Top margin 50% of a inch (842-36)
                    0F
                )
            }
        }

        override fun onEndPage(writer: PdfWriter, document: Document) {
            with(writer.directContent) {
                // Left-aligned play store link
                ColumnText.showTextAligned(
                    this,
                    Element.ALIGN_CENTER,
                    Phrase(playStoreLink, font),
                    144F, //Start page margin 1 inch 72 + adding more 1 inch 72 because link string long (72+72)
                    36F, //Bottom margin 50% of a inch (0+36)
                    0F
                )

                // Right-aligned page number
                ColumnText.showTextAligned(
                    this,
                    Element.ALIGN_CENTER,
                    Phrase("Page ${document.pageNumber}", font),
                    559F, //End page margin 50% inch 36 (595-36)
                    36F, //Bottom margin 50% of a inch (0+36)
                    0F
                )
            }
        }
    }

    /**
     * Logs errors using Android's Log and reports them to Firebase Crashlytics.
     *
     * @param message The log message.
     * @param exception The caught [Exception].
     */
    private fun logError(message: String, exception: Exception) {
        Log.e(Constants.LOG_TAG, "$message: ${exception.localizedMessage}", exception)
        FirebaseCrashlytics.getInstance().apply {
            log(message)
            recordException(exception)
        }
    }
}