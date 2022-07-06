package com.wayapaychat.wayapay.presentation.core

import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.wayapaychat.wayapay.R

abstract class BaseActivity : AppCompatActivity() {

    fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
            .show()
    }

    fun enableLoginButton(b: Boolean, next: Button) {
        next.isEnabled = b
    }

    fun setErrorView(view: View, @DrawableRes viewBackground: Int? = R.drawable.input_text_error) {
        viewBackground?.let {
            view.setBackgroundResource(viewBackground)
        } ?: view.setBackgroundResource(0)
    }

    fun clearErrorView(
        view: View,
        @DrawableRes viewBackground: Int? = R.drawable.input_field_bgrd
    ) {
        viewBackground?.let {
            view.setBackgroundResource(viewBackground)
        } ?: view.setBackgroundResource(0)
    }

    fun loading(state: Boolean, progressBar: CircularProgressBar) {
        if (state) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            progressBar.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = View.GONE
        }
    }
}
