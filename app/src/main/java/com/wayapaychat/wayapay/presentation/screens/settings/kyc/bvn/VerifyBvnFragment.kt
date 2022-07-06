package com.wayapaychat.wayapay.presentation.screens.settings.kyc.bvn

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.VerifiyBvnLayoutBinding
import com.wayapaychat.wayapay.databinding.WhyBvnBinding
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.core.BaseFragment

class VerifyBvnFragment : BaseFragment<VerifiyBvnLayoutBinding>(R.layout.verifiy_bvn_layout) {
    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        listener()
        observer()
    }

    private fun listener() {
        with(binding) {
            verifyBtn.setOnClickListener {
                validateFields {
                    Navigation.findNavController(requireView())
                        .navigate(VerifyBvnFragmentDirections.actionVerifyBvnFragmentToBVNLinkedSuccessFragment())
                }
            }
            whyBvnTxt.setOnClickListener {
                WhyBvnDialog().show(requireActivity().supportFragmentManager, "WhyBvnDialog")
            }
        }
    }

    private fun validateFields(callBack: () -> Unit) {
        with(binding) {
            if (bvnField.text.toString().isEmpty()) {
                showError("please fill in your BVN")
                return
            }

            if (bvnField.text.toString().length < 11) {
                showError("Invalid BVN")
                return
            }

            if (verificationCodeField.text.toString().isEmpty()) {
                showError("Enter Verification Code")
                return
            }
        }
        callBack()
    }

    private fun observer() {
    }
}

class WhyBvnDialog : BaseDialogFragment(R.layout.why_bvn) {
    private lateinit var binding: WhyBvnBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WhyBvnBinding.bind(view)
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }
}
