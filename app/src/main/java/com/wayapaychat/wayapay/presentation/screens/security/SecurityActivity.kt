package com.wayapaychat.wayapay.presentation.screens.security

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SecurityActivityBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity

class SecurityActivity : BaseActivity() {
    lateinit var binding: SecurityActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.security_activity)
    }
}
