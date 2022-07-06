package com.wayapaychat.wayapay.presentation.screens.home.customers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.CreateCustomerLayoutBinding
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerRequest
import com.wayapaychat.wayapay.framework.network.model.CustomerData
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseDialogFragment
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import com.wayapaychat.wayapay.presentation.utils.helper.validateEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCustomerDialog(private val refresh: (CustomerData) -> Unit) :
    BaseDialogFragment(R.layout.create_customer_layout) {
    val viewModel: CreateCustomerViewModel by viewModels()

    private lateinit var binding: CreateCustomerLayoutBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateCustomerLayoutBinding.bind(view)
        initView()
    }

    private fun initView() {
        listener()
        observer()
    }

    private fun observer() {
        with(viewModel) {
            createCustomersObserver.observe(
                viewLifecycleOwner
            ) {
                when (it) {
                    is StateMachine.Loading -> {
                        loading(true, binding.progressBar)
                    }

                    is StateMachine.Error -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error.localizedMessage!!,
                            positiveBottomText = "ok"
                        )
                    }

                    is StateMachine.Success -> {
                        loading(false, binding.progressBar)
                        refresh(it.data.data)
                        Toast.makeText(requireContext().applicationContext, "Customer Successfully Created", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }

                    is StateMachine.TimeOut -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = getString(R.string.timeout_request),
                            positiveBottomText = "Ok"
                        )
                    }
                    is StateMachine.GenericError -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error?.details.toString(),
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
        }
    }

    private fun listener() {
        with(binding) {
            cancelButton.setOnClickListener {
                dismiss()
            }
            createBtn.setOnClickListener {
                validateField {
                    viewModel.setStateEvents(
                        CustomersEvents.CreateCustomer,
                        APICreateCustomerRequest(
                            firstName = binding.firstNameField.text.toString(),
                            lastName = binding.lastNameField.text.toString(),
                            email = binding.emailField.text.toString(),
                            phoneNumber = "0${binding.phoneField.text}"
                        ),
                        "","",false
                    )
                }
            }
        }
    }

    private fun validateField(callback: () -> Unit) {
        if (binding.firstNameField.text.toString().isEmpty()) {
            showError("First name is required")
            return
        }

        if (binding.lastNameField.text.toString().isEmpty()) {
            showError("Last name is required")
            return
        }

        if (binding.emailField.text.toString().isEmpty() ) {
            showError("Email is required")
            return
        }
        else if(!validateEmail(binding.emailField.text.toString())) {
            showError("Invalid Email")
            return
        }

        if (binding.phoneField.text.toString()
            .isEmpty()
        ) {
            showError("Phone Number is required")
            return
        }else if(binding.phoneField.text.toString().length < 10 ||
            binding.phoneField.text.toString().length > 10 ){
            showError("Invalid Phone Number")
            return
        }
        callback()
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        requireDialog().window?.setLayout(width, height)
    }
}
