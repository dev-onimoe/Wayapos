package com.wayapaychat.wayapos.helperClasses

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.*

class PdfDocumentAdapter(var context: Context, var path : String) : PrintDocumentAdapter() {
    override fun onLayout(
        p0: PrintAttributes?,
        p1: PrintAttributes?,
        p2: CancellationSignal?,
        p3: LayoutResultCallback?,
        p4: Bundle?
    ) {
       if (p2!!.isCanceled) {
           p3!!.onLayoutCancelled()
       }else {
           val builder = PrintDocumentInfo.Builder("Receipt")
           builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
               .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
               .build()
           p3!!.onLayoutFinished(builder.build(), p1!!.equals(p0))
       }
    }

    override fun onWrite(
        p0: Array<out PageRange>?,
        p1: ParcelFileDescriptor?,
        p2: CancellationSignal?,
        p3: WriteResultCallback?
    ) {
        var inn : InputStream? = null
        var out : OutputStream? = null

        try{
           val file = File(path)
            inn = FileInputStream(file)
            out = FileOutputStream(p1!!.fileDescriptor)

            val buff = ByteArray(16304)
            var size : Int?
            while (inn.read(buff).also { size = it } >= 0 && !p2!!.isCanceled) {
                out.write(buff, 0, size!!)
            }
            if (p2!!.isCanceled) {
                p3!!.onWriteCancelled()
            }else {
                p3!!.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
        }catch(e : Exception) {
            p3!!.onWriteFailed(e.message)
        }
        finally {
            try {
                inn!!.close()
                out!!.close()
            }catch (e : IOException) {
                e.printStackTrace()
            }
        }
    }

}