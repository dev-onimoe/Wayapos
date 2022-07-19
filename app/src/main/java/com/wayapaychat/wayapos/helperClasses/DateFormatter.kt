package com.wayapaychat.wayapos.helperClasses

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    companion object {
        val MMM_dd_yyyy = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val MMM_dd_yyyy_hh_mm_aa = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH)
        val yyyy_MM_ddTHHmmssSSSZ = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val MMM_dd = SimpleDateFormat("MMM dd", Locale.ENGLISH)
        val dd_MMMM = SimpleDateFormat("dd, MMMM", Locale.ENGLISH)
        val MMIddIyyyy = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        val hh_mm_aa = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
        val yyyyMMdd_HHmmss = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
        val MMM_dd_hh_mm_aa = SimpleDateFormat("MMM dd, hh:mm aa", Locale.ENGLISH)
    }
}