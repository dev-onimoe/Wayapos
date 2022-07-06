package com.wayapaychat.wayapay.presentation.core

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.mikhaellopez.circularprogressbar.CircularProgressBar

abstract class BaseDialogFragment(@LayoutRes layout: Int) : DialogFragment(layout) {
    fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }
    fun loading(state: Boolean, progressBar: CircularProgressBar) {
        if (state) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            progressBar.visibility = View.VISIBLE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = View.GONE
        }
    }
    override fun onStart() {
        super.onStart()
        requireDialog().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        requireDialog().window?.setLayout(width, height)
    }
}
