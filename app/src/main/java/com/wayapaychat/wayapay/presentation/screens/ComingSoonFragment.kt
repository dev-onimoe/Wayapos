package com.wayapaychat.wayapay.presentation.screens

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ComingSoonBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity

class ComingSoonActivity : BaseActivity() {
    lateinit var binding: ComingSoonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.coming_soon)
        binding.button.setOnClickListener {
            finish()
        }
    }
}
