package com.wayapaychat.wayapay.presentation.screens.settings.kyc.bvn

import androidx.navigation.Navigation
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.AddBvnLayoutBinding
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import com.wayapaychat.wayapay.presentation.utils.helper.validatePhoneNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBvnFragment : BaseFragment<AddBvnLayoutBinding>(R.layout.add_bvn_layout), ISelectedDate {
    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        listener()
    }

    private fun listener() {
        with(binding) {
            nextBtn.setOnClickListener {
                validateFields {
                    validateBvn()
                    Navigation.findNavController(requireView())
                        .navigate(AddBvnFragmentDirections.actionAddBvnFragmentToVerifyBvnFragment())
                }
            }
            dateOfBirthField.setOnClickListener {
                DatePickerFragment(this@AddBvnFragment).show(
                    requireActivity().supportFragmentManager,
                    "DatePickerFragment"
                )
            }
        }
    }

    private fun validateBvn() {
    }

    private fun validateFields(callBack: () -> Unit) {
        with(binding) {
            if (inputBvnField.text.toString().isEmpty()) {
                showError("Please fill in your bvn")
                return
            }

            if (inputBvnField.text.toString().length < 11) {
                showError("Invalid BVN")
                return
            }
            if (phoneNumberField.text.toString().isEmpty()) {
                showError("phone number is required")
                return
            }

            if (validatePhoneNumber(phoneNumberField.text.toString(), requireContext()).not()) {
                showError("invalid phone number is required")
                return
            }
            if (dateOfBirthField.text.toString().isEmpty()) {
                showError("Select a date of birth")
                return
            }
        }
        callBack()
    }

    override fun onSelectedDate(string: String) {
        binding.dateOfBirthField.setText(string)
    }
}
