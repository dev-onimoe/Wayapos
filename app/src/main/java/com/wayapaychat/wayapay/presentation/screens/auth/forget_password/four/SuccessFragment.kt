package com.wayapaychat.wayapay.presentation.screens.auth.forget_password.four

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ForgetPasswordSuccessBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordSuccessFragment : BaseActivity() {
    lateinit var binding: ForgetPasswordSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.forget_password_success
        )
        initView()
    }

    private fun initView() {
        listener()
    }

    private fun listener() {
        with(binding) {
            loginBtn.setOnClickListener {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                finish()
                startActivity(intent)
            }
        }
    }
}
