package com.wayapaychat.wayapay.presentation.core

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wayapaychat.wayapay.R

abstract class BaseBottomSheetDialog<DB : ViewDataBinding> constructor(@LayoutRes private val layoutRes: Int) :
    BottomSheetDialogFragment() {
    lateinit var binding: DB
    open fun init() {}
    private fun init(inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
    }


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        init()
        return binding.root

    }

}
