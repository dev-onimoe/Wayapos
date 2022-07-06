package com.wayapaychat.wayapay.presentation.screens.auth.forget_password.three

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ChangePasswordFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.APIChangePasswordBody
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.forget_password.four.ForgetPasswordSuccessFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpStateEvents
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpViewModel
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseActivity() {
    lateinit var binding: ChangePasswordFragmentBinding
    val viewModel: SignUpViewModel by viewModels()
    lateinit var phoneOrEmail: String
    lateinit var otp: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.change_password_fragment)
        phoneOrEmail = intent?.extras?.getString("phoneOrEmail")!!
        otp = intent?.extras?.getString("otp")!!
        initView()
    }

    private fun initView() {
        listeners()
        observers()
    }

    private fun observers() {
        viewModel.changePasswordObserver.observe(
            this,
            {

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
                        val intent =
                            Intent(applicationContext, ForgetPasswordSuccessFragment::class.java)
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
        )
    }

    private fun validateFields(callBack: () -> Unit) {
        if (binding.passwordField1.text.toString()
            .isEmpty() || binding.passwordField2.text.toString().isEmpty()
        ) {
            showError("Password Field cannot be blank")
            return
        }

        if (binding.passwordField2.text.toString().length < 6 || binding.passwordField1.text.toString().length < 6) {
            showError("Password length must be greater than 6")
            return
        }
        if (binding.passwordField2.text.toString() != binding.passwordField1.text.toString()) {
            showError("Password Do not match")
            return
        }
        callBack()
    }

    private fun listeners() {
        with(binding) {
            changePasswordBtn.setOnClickListener {
                validateFields {
                    viewModel.setStateEvents(
                        SignUpStateEvents.ChangePassword,
                        APIChangePasswordBody(
                            newPassword = binding.passwordField1.text.toString().trim(),
                            otp = otp.toLong(),
                            phoneOrEmail = phoneOrEmail
                        )
                    )
                }
            }
        }
    }
}
