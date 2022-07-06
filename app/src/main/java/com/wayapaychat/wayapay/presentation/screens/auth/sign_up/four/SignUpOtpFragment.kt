package com.wayapaychat.wayapay.presentation.screens.auth.sign_up.four

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SignupFragmentFourBinding
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOtp
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.forget_password.dialogs.OTPDialogFragment
import com.wayapaychat.wayapay.presentation.screens.auth.login.LoginActivity
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpStateEvents
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpViewModel
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpOtpFragment :
    BaseActivity() {
    lateinit var binding: SignupFragmentFourBinding

    val viewModel: SignUpViewModel by viewModels()

    @Inject
    lateinit var cache: Cache

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = DataBindingUtil.setContentView(this, R.layout.signup_fragment_four)
        initView()
    }

    private fun initView() {
        listener()
        observer()
    }

    private fun observer() {
        viewModel.confirmOtpObserver.observe(
            this,
            Observer {
                when (it) {
                    is StateMachine.Loading -> {
                        loading(true)
                    }

                    is StateMachine.Error -> {
                        loading(false)
                        showAlertDialogMessage(
                            message = it.error.localizedMessage!!,
                            positiveBottomText = "Retry"
                        )
                    }

                    is StateMachine.Success -> {
                        loading(false)
                        val i = Intent(applicationContext, LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                    }

                    is StateMachine.TimeOut -> {
                        loading(false)
                        showAlertDialogMessage(
                            message = getString(R.string.timeout_request),
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.GenericError -> {
                        loading(false)
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

    private fun listener() {
        with(binding) {
            loginBtn.setOnClickListener {
                validateOTP()
            }

            didntGetOtp.setOnClickListener {
                OTPDialogFragment {
                }.show(supportFragmentManager, "SignUpOtpFragment")
            }
        }
    }

    private fun validateOTP() {
        val otpPhoneNumber = (
            cache.getObject(
                "AccountDetails",
                APICreateAccount::class.java
            ) as APICreateAccount
            ).phoneNumber
        val requestBody = APIConfirmOtp(
            otp = binding.otpField.text.toString(),
            phoneOrEmail = otpPhoneNumber
        )
        viewModel.setStateEvents(SignUpStateEvents.ConfirmOtp, requestBody)
    }

    private fun loading(state: Boolean) {
        if (state) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            binding.progressBar.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            binding.progressBar.visibility = View.GONE
        }
    }
}
