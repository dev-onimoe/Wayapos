package com.wayapaychat.wayapay.presentation.screens.home.welcomescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ActivityWelcomeBinding
import com.wayapaychat.wayapay.databinding.SecurityActivityBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity

class WelcomeActivity : BaseActivity() {
    lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        binding.appBar.setOnClickListener {
            finish()
        }
    }
}