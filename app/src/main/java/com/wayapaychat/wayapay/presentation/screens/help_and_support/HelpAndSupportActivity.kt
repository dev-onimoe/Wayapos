package com.wayapaychat.wayapay.presentation.screens.help_and_support

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.HelpAndSupportBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity

class HelpAndSupportActivity : BaseActivity() {
    private lateinit var binding: HelpAndSupportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.help_and_support)
    }
}
