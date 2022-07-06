package com.wayapaychat.wayapay.presentation.screens.auth.forget_password.two

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.PasswordResetTwoBinding
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.forget_password.three.ChangePasswordFragment
import dagger.hilt.android.AndroidEntryPoint

const val OTP_DIALOG = "OTP_DIALOG"

@AndroidEntryPoint
class ResetPasswordOtpFragment :
    BaseActivity() {
    lateinit var binding: PasswordResetTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.password_reset_two)
        initView()
    }

    private fun initView() {
        listeners()
    }

    private fun listeners() {
        with(binding) {
            verifyBtn.setOnClickListener {
                validateFields {
                    val i = Intent(applicationContext, ChangePasswordFragment::class.java)
                    i.putExtra("phoneOrEmail", intent.extras?.getString("phoneOrEmail"))
                    i.putExtra("otp", binding.otpField.text.toString())
                    startActivity(i)
                }
            }
        }
    }

    private fun validateFields(callBack: () -> Unit) {
        if (binding.otpField.text?.toString()?.isEmpty()!!) {
            showError("Input OTP")
            return
        }

        if (binding.otpField.text?.toString()?.length != 6) {
            showError("Invalid OTP")
            return
        }
        callBack()
    }
}
