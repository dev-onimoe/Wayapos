package com.wayapaychat.wayapay.presentation.screens.auth.forget_password.one

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.PasswordResetOneBinding
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.forget_password.two.ResetPasswordOtpFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpStateEvents
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpViewModel
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import com.wayapaychat.wayapay.presentation.utils.helper.getPhoneNumber
import com.wayapaychat.wayapay.presentation.utils.helper.validateEmail
import com.wayapaychat.wayapay.presentation.utils.helper.validatePhoneNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordEmailFragment : BaseActivity() {
    lateinit var binding: PasswordResetOneBinding
    val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.password_reset_one)
        initView()
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun observer() {
        viewModel.requestOtpEmailObserver.observe(
            this
        ) {

            when (it) {
                is StateMachine.Loading -> {
                    loading(true, binding.progressBar)
                }

                is StateMachine.Error -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    val intent = Intent(applicationContext, ResetPasswordOtpFragment::class.java)
                    intent.putExtra("phoneOrEmail", binding.emailField.text.toString().trim())
                    startActivity(intent)
                }

                is StateMachine.TimeOut -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Retry"
                    )
                }
                is StateMachine.GenericError -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error!!.message!!,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
        viewModel.requestOtpPhoneObserver.observe(
            this
        ) {

            when (it) {
                is StateMachine.Loading -> {
                    loading(true, binding.progressBar)
                }

                is StateMachine.Error -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    finish()
                    val intent = Intent(applicationContext, ResetPasswordOtpFragment::class.java)
                    intent.putExtra(
                        "phoneOrEmail",
                        getPhoneNumber(binding.emailField.text.toString().trim())
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                is StateMachine.TimeOut -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Retry"
                    )
                }
                is StateMachine.GenericError -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error!!.message!!,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private fun listeners() {
        with(binding) {
            resetPasswordBtn.setOnClickListener {
                validateFields {

                    checkIfIsEmailOrPhone {
                        when (it) {
                            CredentialType.UNDEFINED -> {
                                showError("Invalid Email or Phone No.")
                            }
                            CredentialType.PHONE_NUMBER -> {
                                viewModel.setStateEvents(
                                    SignUpStateEvents.SendOtpPhoneNumber,
                                    getPhoneNumber(binding.emailField.text.toString().trim())
                                )
                            }
                            CredentialType.EMAIL -> {
                                viewModel.setStateEvents(
                                    SignUpStateEvents.SendOtpEmail,
                                    binding.emailField.text.toString().trim()
                                )
                            }
                        }
                    }
                }
            }
            backToSignInTxt.setOnClickListener {
                finish()
            }
        }
    }

    private fun checkIfIsEmailOrPhone(callBack: (CredentialType) -> Unit) {

        val delegate = binding.emailField.text.toString().trim()
        if (validateEmail(delegate)) {
            callBack(CredentialType.EMAIL)
            return
        }

        if (validatePhoneNumber(delegate, applicationContext)) {
            callBack(CredentialType.PHONE_NUMBER)
            return
        }

        if (validatePhoneNumber(
                delegate,
                applicationContext
            ).not() && validateEmail(delegate).not()
        ) {
            callBack(CredentialType.UNDEFINED)
            return
        }
    }

    enum class CredentialType() {
        EMAIL, PHONE_NUMBER, UNDEFINED
    }

    private fun validateFields(callBack: () -> Unit) {
        if (binding.emailField.text.isEmpty()) {
            showError("Email or Phone is required")
            return
        }

        if (binding.emailField.text.isEmpty()) {
            showError("Email or Phone is required")
            return
        }
        callBack()
    }
}
