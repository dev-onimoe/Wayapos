package com.wayapaychat.wayapos.views.fragments.transaction

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapay.presentation.utils.cache.CacheImpl
import com.wayapaychat.wayapos.helperClasses.NumberFormats
import com.wayapaychat.wayapos.helperClasses.PdfDocumentAdapter
import com.wayapaychat.wayapos.models.AgencyTransactions
import org.w3c.dom.Document
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AgencyTransactionDetailsFragment(var transaction : AgencyTransactions) : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var cache :CacheImpl
    lateinit var receipt_rl : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cache = CacheImpl(context!!, Gson())
        if (transaction.tranType.equals("TRANSFER", true)) {
            val view = inflater.inflate(R.layout.fragment_agency_transaction_transfer_details, container, false)
            initializeTransferViews(view)
            return view
        }else {
            val view = inflater.inflate(R.layout.agency_transaction_bill_details, container, false)
            initializebillViews(view)
            return view
        }

    }

    private fun initializeTransferViews(view : View) {

        val date = view.findViewById<TextView>(R.id.trans_date)
        val ref_id = view.findViewById<TextView>(R.id.ref_id)
        val type = view.findViewById<TextView>(R.id.trans_type)
        val category = view.findViewById<TextView>(R.id.trans_category)
        val senders_account = view.findViewById<TextView>(R.id.senders_name)
        val receiver_name = view.findViewById<TextView>(R.id.senders_name)
        val receiver_account = view.findViewById<TextView>(R.id.senders_name)
        val receiver_bank = view.findViewById<TextView>(R.id.senders_name)
        val narration = view.findViewById<TextView>(R.id.senders_name)
        val senders_name = view.findViewById<TextView>(R.id.senders_name)
        val print = view.findViewById<TextView>(R.id.print_text)
        val amount = view.findViewById<TextView>(R.id.trans_price)
        val back = view.findViewById<LinearLayout>(R.id.back_layout)
        receipt_rl = view.findViewById(R.id.receipt_rl)

        back.setOnClickListener{
            activity!!.onBackPressed()
        }
        print.setOnClickListener{
            createPdfFile(R.layout.transfer_pdf_receipt)
        }

        date.text = LocalDate.parse(transaction.tranDate).toString()
        ref_id.text = transaction.paymentReference
        type.text = transaction.tranType
        category.text = "NEXGO N3"
        senders_account.text = transaction.acct
        receiver_name.text = transaction.receiverName
        receiver_account.text = "---"
        receiver_bank.text = "---"
        narration.text = transaction.tranNarrate
        senders_name.text = transaction.senderName
        amount.text = NumberFormats.formatToNGN(transaction.tranAmount!!.toDouble())
    }

    private fun initializebillViews(view : View) {

        val date = view.findViewById<TextView>(R.id.trans_date)
        val ref_id = view.findViewById<TextView>(R.id.ref_id)
        val type = view.findViewById<TextView>(R.id.trans_type)
        val fee = view.findViewById<TextView>(R.id.trans_fee)
        val category = view.findViewById<TextView>(R.id.trans_category)
        val amount = view.findViewById<TextView>(R.id.trans_price)
        val print = view.findViewById<TextView>(R.id.print_text)
        val back = view.findViewById<LinearLayout>(R.id.back_layout)
        receipt_rl = view.findViewById(R.id.receipt_rl)

        back.setOnClickListener{
            activity!!.onBackPressed()
        }
        print.setOnClickListener{
            createPdfFile(R.layout.bill_pdf_receipt)
        }

        amount.text = NumberFormats.formatToNGN(transaction.tranAmount!!.toDouble())
        date.text = LocalDate.parse(transaction.tranDate).toString()
        ref_id.text = transaction.paymentReference
        type.text = transaction.tranType
        category.text = "NEXGO N3"
        fee.text = "NGN0.00"
    }

    fun user(): APILoginResponse {
        val cache = CacheImpl(context!!, Gson())
        val userDetails = cache.getObject(CacheConstants.Keys.USER_DETAILS, APILoginResponse::class.java) as APILoginResponse
        return userDetails
    }

    private fun createPdfFile( layoutID : Int) {
        val view = LayoutInflater.from(context!!).inflate(layoutID, receipt_rl, false)
        if (layoutID == R.layout.transfer_pdf_receipt) {
            tfViews(view)
        }else {
            billViews(view)
        }
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context!!.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        }
        else{
            activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        Bitmap.createScaledBitmap(bitmap, 595, 842, true)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        pdfDocument.finishPage(page)

        val path = context!!.getExternalFilesDir(null)!!.absolutePath
        val file = path + "/Receipt.pdf"
        val filePath = File(file)

        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()
        Toast.makeText(context!!, "pdf saved to" + file, 3)

        printDocument(file)
    }

    private fun printDocument(path : String) {
        val printManager = activity!!.getSystemService(Context.PRINT_SERVICE) as PrintManager
        try{
           val printDocAdapter = PdfDocumentAdapter(context!!, path)
            printManager.print("Document", printDocAdapter, PrintAttributes.Builder().build())
        }catch(e : Exception) {
            println(e.message)
        }
    }

    private fun tfViews(view : View) {

        val date = view.findViewById<TextView>(R.id.trans_date)
        val ref_id = view.findViewById<TextView>(R.id.ref_id)
        val type = view.findViewById<TextView>(R.id.trans_type)
        val category = view.findViewById<TextView>(R.id.trans_category)
        val senders_account = view.findViewById<TextView>(R.id.senders_name)
        val receiver_name = view.findViewById<TextView>(R.id.senders_name)
        val receiver_account = view.findViewById<TextView>(R.id.senders_name)
        val receiver_bank = view.findViewById<TextView>(R.id.senders_name)
        val narration = view.findViewById<TextView>(R.id.senders_name)
        val senders_name = view.findViewById<TextView>(R.id.senders_name)
        val amount = view.findViewById<TextView>(R.id.trans_price)
        val back = view.findViewById<LinearLayout>(R.id.back_layout)

        date.text = LocalDate.parse(transaction.tranDate).toString()
        ref_id.text = transaction.paymentReference
        type.text = transaction.tranType
        category.text = "NEXGO N3"
        senders_account.text = cache.getString("account")
        receiver_name.text = transaction.receiverName
        receiver_account.text = transaction.acct
        receiver_bank.text = "---"
        narration.text = transaction.tranNarrate
        senders_name.text = transaction.senderName
        amount.text = NumberFormats.formatToNGN(transaction.tranAmount!!.toDouble())
    }

    private fun billViews(view : View) {

        val date = view.findViewById<TextView>(R.id.trans_date)
        val ref_id = view.findViewById<TextView>(R.id.ref_id)
        val type = view.findViewById<TextView>(R.id.trans_type)
        val fee = view.findViewById<TextView>(R.id.trans_fee)
        val category = view.findViewById<TextView>(R.id.trans_category)
        val amount = view.findViewById<TextView>(R.id.trans_price)
        val back = view.findViewById<LinearLayout>(R.id.back_layout)

        amount.text = NumberFormats.formatToNGN(transaction.tranAmount!!.toDouble())
        date.text = LocalDate.parse(transaction.tranDate).toString()
        ref_id.text = transaction.paymentReference
        type.text = transaction.tranType
        category.text = "NEXGO N3"
        fee.text = "NGN0.00"
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, t : AgencyTransactions) =
            AgencyTransactionDetailsFragment(t).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}