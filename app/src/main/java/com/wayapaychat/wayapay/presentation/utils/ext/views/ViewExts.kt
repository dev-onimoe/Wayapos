package com.wayapaychat.wayapay.presentation.utils.ext.views

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

internal val Fragment.TAG: String get() = "Enike ${javaClass.simpleName}"
internal val Activity.TAG: String get() = "Enike ${javaClass.simpleName}"

internal fun Activity.getImageDrawable(id: Int): Drawable =
    ContextCompat.getDrawable(applicationContext, id)!!
internal fun Fragment.getImageDrawable(id: Int): Drawable = ContextCompat.getDrawable(requireContext(), id)!!

internal fun Fragment.showAlertDialogMessage(
    message: String,
    title: String? = null,
    positiveBottomText: String? = null,
    positiveBottomAction: BottomAction? = null,
    negativeBottomText: String? = null,
    negativeBottomAction: BottomAction? = null,
    isCancelable: Boolean = false
): AlertDialog.Builder {
    val alertDialog = AlertDialog.Builder(requireContext())
    alertDialog.setMessage(message)

    alertDialog.setTitle(title ?: "Alert")
    alertDialog.setPositiveButton(
        positiveBottomText ?: "ok"
    ) { dialog, p ->
        positiveBottomAction?.invoke()
        dialog.dismiss()
    }
    negativeBottomText?.let {
        alertDialog.setNegativeButton(
            negativeBottomText ?: "ok"
        ) { dialog, p ->
            negativeBottomAction?.invoke()
            dialog.dismiss()
        }
    }
    alertDialog.setCancelable(isCancelable)
    alertDialog.show()
    return alertDialog
}
private typealias BottomAction = () -> Unit

internal fun Activity.showAlertDialogMessage(
    message: String,
    title: String? = null,
    positiveBottomText: String? = null,
    positiveBottomAction: BottomAction? = null,
    negativeBottomText: String? = null,
    negativeBottomAction: BottomAction? = null,
    isCancelable: Boolean = false
): AlertDialog.Builder {
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.setMessage(message)

    alertDialog.setTitle(title ?: "Alert")
    alertDialog.setPositiveButton(
        positiveBottomText ?: "ok"
    ) { dialog, p ->
        positiveBottomAction?.invoke()
        dialog.dismiss()
    }
    negativeBottomText?.let {
        alertDialog.setNegativeButton(
            negativeBottomText ?: "ok"
        ) { dialog, p ->
            negativeBottomAction?.invoke()
            dialog.dismiss()
        }
    }
    alertDialog.setCancelable(isCancelable)
    alertDialog.show()
    return alertDialog
}

internal fun ViewPager2.removeOverScroll() {
    (getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}
