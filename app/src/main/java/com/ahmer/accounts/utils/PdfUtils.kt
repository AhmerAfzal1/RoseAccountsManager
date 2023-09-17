package com.ahmer.accounts.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.ahmer.accounts.R
import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
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

    @JvmStatic
    fun exportToPdf(
        context: Context,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
        transList: ResultState<List<TransEntity>>
    ) {
        if (transList is ResultState.Success) {
            if (transList.data.isNotEmpty()) {
                val mFileName =
                    HelperUtils.getDateTime(
                        time = System.currentTimeMillis(), pattern = "ddMMyyHHmmss"
                    ) + ".pdf"
                val mIntent: Intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    val mMimeType = "application/pdf"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(mMimeType))
                    putExtra(Intent.EXTRA_TITLE, mFileName)
                    type = mMimeType
                }
                launcher.launch(mIntent)
            } else {
                HelperUtils.toastLong(
                    context = context,
                    msg = context.getString(R.string.pdf_not_generated)
                )
            }
        }
    }

    @JvmStatic
    fun generatePdf(
        context: Context, uri: Uri, transEntity: List<TransEntity>,
        transSumModel: TransSumModel, accountName: String
    ): Boolean {
        //One inch size equal to 70F; margin using 0.75% of a inch
        val mDocument = Document(PageSize.A4, 52.5F, 52.5F, 52.5F, 52.5F)
        try {
            val mOutPutStream = context.contentResolver.openOutputStream(uri)
                ?: throw NullPointerException("OutputStream for given input Uri is null")

            val mPdfWriter = PdfWriter.getInstance(mDocument, mOutPutStream)
            val mTitleStatement = "$accountName Account Statement"
            val mTotalCredit: Double = transSumModel.creditSum?.toDouble() ?: 0.0
            val mTotalDebit: Double = transSumModel.debitSum?.toDouble() ?: 0.0
            val mTotalBalance: Double = mTotalCredit.minus(mTotalDebit)

            mPdfWriter.pageEvent = HeaderFooterPageEvent(context)
            mDocument.open()
            mDocument.addCreationDate()
            mDocument.addAuthor(context.getString(R.string.app_name))
            mDocument.addTitle(mTitleStatement)
            mDocument.addCreator(context.getString(R.string.app_name))
            mDocument.addSubject(context.getString(R.string.app_name))

            val mFont = Font(Font.FontFamily.HELVETICA)
            mFont.color = BaseColor.BLACK
            mFont.size = 16F
            mFont.style = Font.NORMAL
            val mParagraph = Paragraph(mTitleStatement, mFont)
            mParagraph.spacingAfter = 20F
            mParagraph.alignment = Element.ALIGN_CENTER
            mDocument.add(mParagraph)

            val mTableMain = PdfPTable(5)
            mTableMain.widthPercentage = 100F
            mTableMain.setTotalWidth(floatArrayOf(36F, 72F, 202F, 90F, 90F))
            mTableMain.isLockedWidth = true
            mTableMain.addCell(cellFormat("Sr", true))
            mTableMain.addCell(cellFormat("Date", true))
            mTableMain.addCell(cellFormat("Description", true))
            mTableMain.addCell(cellFormat("Debit", true))
            mTableMain.addCell(cellFormat("Credit", true))

            val mSortedList = transEntity.sortedWith { o1, o2 -> o1.id - o2.id }
            var mSrNo = 0
            mSortedList.forEach { list ->
                mSrNo += 1
                mTableMain.addCell(cellFormat(mSrNo.toString(), false, "Center"))
                mTableMain.addCell(cellFormat(list.newCurrentShortDate, false, "Center"))
                mTableMain.addCell(cellFormat(list.description, false))
                var mCreditList = 0.0
                var mDebitList = 0.0
                if (list.type == "Credit") {
                    mCreditList = list.amount.toDouble()
                } else {
                    mDebitList = list.amount.toDouble()
                }
                val mDebit = HelperUtils.getRoundedValue(value = mDebitList)
                val mCredit = HelperUtils.getRoundedValue(value = mCreditList)
                if (mDebit == "0") {
                    mTableMain.addCell("")
                } else {
                    mTableMain.addCell(cellFormat(mDebit, false, "Right"))
                }
                if (mCredit == "0") {
                    mTableMain.addCell("")
                } else {
                    mTableMain.addCell(cellFormat(mCredit, false, "Right"))
                }
            }

            val mTableTotal = PdfPTable(3)
            mTableTotal.widthPercentage = 100F
            mTableTotal.setTotalWidth(floatArrayOf(310F, 90F, 90F))
            mTableTotal.isLockedWidth = true
            mTableTotal.addCell(cellFormat("Total", false, "Center", true))
            mTableTotal.addCell(
                cellFormat(
                    HelperUtils.getRoundedValue(value = mTotalDebit), false, "Right", true
                )
            )
            mTableTotal.addCell(
                cellFormat(
                    HelperUtils.getRoundedValue(value = mTotalCredit), false, "Right", true
                )
            )

            val mTableBalance = PdfPTable(2)
            mTableBalance.widthPercentage = 100F
            mTableBalance.setTotalWidth(floatArrayOf(310F, 180F))
            mTableBalance.isLockedWidth = true
            mTableBalance.addCell(cellFormat("Balance", false, "Center", true))
            mTableBalance.addCell(
                cellFormat(
                    HelperUtils.getRoundedValue(value = mTotalBalance), false, "Right", true
                )
            )

            mDocument.add(mTableMain)
            mDocument.add(mTableTotal)
            mDocument.add(mTableBalance)
            return true
        } catch (de: DocumentException) {
            Log.e(Constants.LOG_TAG, de.localizedMessage, de)
            FirebaseCrashlytics.getInstance().recordException(de)
            return false
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, e.localizedMessage, e)
            FirebaseCrashlytics.getInstance().recordException(e)
            return false
        } finally {
            mDocument.close()
        }
    }

    private fun cellFormat(
        text: String, isForTable: Boolean, alignment: String = "", isCellForTotal: Boolean = false
    ): PdfPCell {
        val mFont = Font(Font.FontFamily.HELVETICA)
        mFont.color = BaseColor.BLACK
        if (isForTable) {
            mFont.size = 14F
            mFont.style = Font.BOLD
        } else {
            if (isCellForTotal) {
                mFont.size = 14F
                mFont.style = Font.BOLD
            } else {
                mFont.size = 12F
                mFont.style = Font.NORMAL
            }
        }
        val mPdfPCell = PdfPCell(Phrase(text, mFont))
        if (isForTable) {
            mPdfPCell.verticalAlignment = Element.ALIGN_MIDDLE
            mPdfPCell.horizontalAlignment = Element.ALIGN_CENTER
            mPdfPCell.paddingTop = 5F
            mPdfPCell.paddingBottom = 8F
        } else {
            when (alignment) {
                "Right" -> {
                    mPdfPCell.verticalAlignment = Element.ALIGN_MIDDLE
                    mPdfPCell.horizontalAlignment = Element.ALIGN_RIGHT
                }

                "Center" -> {
                    mPdfPCell.verticalAlignment = Element.ALIGN_MIDDLE
                    mPdfPCell.horizontalAlignment = Element.ALIGN_CENTER
                }

                "" -> {
                    mPdfPCell.verticalAlignment = Element.ALIGN_MIDDLE
                }
            }
            if (isCellForTotal) {
                mPdfPCell.paddingTop = 5F
                mPdfPCell.paddingBottom = 7F
            } else {
                mPdfPCell.paddingTop = 3F
                mPdfPCell.paddingBottom = 5F
            }
        }
        return mPdfPCell
    }

    class HeaderFooterPageEvent(private val context: Context) : PdfPageEventHelper() {

        override fun onStartPage(writer: PdfWriter, document: Document?) {
            val mFont = Font(Font.FontFamily.HELVETICA)
            mFont.color = BaseColor.BLACK
            mFont.size = 10F
            mFont.style = Font.NORMAL

            ColumnText.showTextAligned(
                writer.directContent, Element.ALIGN_CENTER,
                Phrase(context.getString(R.string.app_name), mFont), 80F, 800F, 0F
            )

            ColumnText.showTextAligned(
                writer.directContent,
                Element.ALIGN_CENTER,
                Phrase(
                    "${
                        HelperUtils.getDateTime(
                            time = System.currentTimeMillis(), pattern = "dd MMM yyyy"
                        )
                    } - ${
                        HelperUtils.getDateTime(
                            time = System.currentTimeMillis(), pattern = "hh:mm:ss a"
                        )
                    }", mFont
                ), 477F, 800F, 0F
            )

        }

        override fun onEndPage(writer: PdfWriter, document: Document) {
            val mFont = Font(Font.FontFamily.HELVETICA)
            mFont.color = BaseColor.BLACK
            mFont.size = 10F
            mFont.style = Font.NORMAL

            val mPhrase = Phrase("", mFont)
            val mPlayStoreLink = HelperUtils.getPlayStoreLink(context = context)
            val mChunk = Chunk(mPlayStoreLink)
            mChunk.setAnchor(mPlayStoreLink)
            mPhrase.add(mChunk)

            ColumnText.showTextAligned(
                writer.directContent, Element.ALIGN_CENTER,
                Phrase(mPhrase), 170F, 35F, 0F
            )
            ColumnText.showTextAligned(
                writer.directContent, Element.ALIGN_CENTER,
                Phrase("Page " + document.pageNumber, mFont), 530F, 35F, 0F
            )
        }
    }
}