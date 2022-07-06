package com.wayapaychat.wayapay.presentation.screens.auth.forget_password.dialogs

import android.os.Bundle
import android.view.View
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.OtpDialogBinding
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment

class OTPDialogFragment(private val resend: () -> Unit) :
    BaseDialogFragment(R.layout.otp_dialog) {

    private lateinit var binding: OtpDialogBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OtpDialogBinding.bind(view)
        initView()
    }

    private fun initView() {
        listeners()
    }

    private fun listeners() {
        with(binding) {
            resendBtn.setOnClickListener {
                resend()
                dismiss()
            }
        }
    }
}
