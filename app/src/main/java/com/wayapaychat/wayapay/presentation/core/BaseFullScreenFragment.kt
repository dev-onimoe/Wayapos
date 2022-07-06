package com.wayapaychat.wayapay.presentation.core

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

open abstract class BaseFullScreenFragment<db : ViewDataBinding>(@LayoutRes layoutRes: Int) : BaseFragment<db>(layoutRes) {
    override fun onResume() {
        super.onResume()
        requireActivity().window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}
