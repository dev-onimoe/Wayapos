package com.wayapaychat.wayapay.presentation.screens.settings.kyc.bvn

import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.BvnOtpVerificationFragmentBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BvnOtpVerificationFragment :
    BaseFragment<BvnOtpVerificationFragmentBinding>(R.layout.bvn_otp_verification_fragment) {

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun listeners() {
        with(binding) {
        }
    }

    private fun observer() {
    }
}
