package com.wayapaychat.wayapay.presentation.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.wayapaychat.wayapay.R

open abstract class BaseFragment<db : ViewDataBinding>(
    @LayoutRes private val layout: Int
) : Fragment(layout) {
    open lateinit var binding: db

    open fun init() {}

    private fun init(inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        init(inflater, container!!)
        init()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Navigation.findNavController(requireView()).popBackStack()
                }
            }
        )
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
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

    fun loading(
        state: Boolean,
        progressBar: CircularProgressBar,
        blockUiInteraction: Boolean = false
    ) {
        if (state) {
            if (blockUiInteraction)
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
}
