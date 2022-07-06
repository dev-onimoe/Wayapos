package com.wayapaychat.wayapay.presentation.screens.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.BuildConfig
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.LoginActivityBinding
import com.wayapaychat.wayapay.framework.network.model.APISignIn
import com.wayapaychat.wayapay.framework.network.model.AccountNumberData
import com.wayapaychat.wayapay.framework.network.model.User
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.MainActivity
import com.wayapaychat.wayapay.presentation.screens.auth.forget_password.one.ForgotPasswordEmailFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpStateEvents
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpViewModel
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.one.SignUpFragmentOne
import com.wayapaychat.wayapay.presentation.screens.settings.profile.ProfileActivity
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants.Keys.ACCOUNT_NUMBER
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants.Keys.MERCHANT_ID
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants.Keys.TOKEN
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants.Keys.USER_DETAILS
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants.Keys.USER_ID
import com.wayapaychat.wayapay.presentation.utils.ext.views.TAG
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import com.wayapaychat.wayapay.presentation.utils.helper.getPhoneNumber
import com.wayapaychat.wayapay.presentation.utils.helper.validateEmail
import com.wayapaychat.wayapay.presentation.utils.helper.validatePhoneNumber
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: LoginActivityBinding
    val viewModel: SignUpViewModel by viewModels()
    private var profileImage: String? = null
    private var user: User? = null

    @Inject
    lateinit var cache: Cache
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        initView()
        login()
    }

    private fun login() {
        if (BuildConfig.DEBUG) {
            binding.emailField.setText("veragreen20@gmail.com")
            binding.passwordField.setText("Password@234")
        }
    }

    private fun initView() {
        listeners()
        observer()
    }

    private fun observer() {
        viewModel.loginObserver.observe(
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

                    if (it.data.data?.passwordAge!! >= 90) {
                        showAlertDialogMessage(
                            message = getString(R.string.update_password),
                            positiveBottomText = "close"
                        )
                    } else {
                        user = it.data.data?.user
                        profileImage = it.data.data?.user?.profileImage
                        cache.putBoolean(CacheConstants.Keys.IS_LOGGED_IN, true)
                        cache.putObject(USER_DETAILS, it.data)
                        cache.putString(MERCHANT_ID, it.data.data?.merchantId.toString())
                        cache.putInt(USER_ID, it.data.data?.user?.id!!)
                        it.data.data.token?.let { token ->
                            cache.putString(TOKEN, token)
                        }
                        viewModel.setStateEvents(
                            SignUpStateEvents.AccountNumber,
                            it.data.data.user.id
                        )
                    }
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
                        message = "${it.error?.message.toString()} ${it.error?.details.toString()}",
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
        viewModel.accountNumberObserver.observe(this) {
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
                    it.data.data?.referalCode = user?.referenceCode!!
                    cache.putObject(ACCOUNT_NUMBER, it.data.data as Any)
                    val accountDetails = cache.getObject(
                        ACCOUNT_NUMBER,
                        AccountNumberData::class.java
                    ) as AccountNumberData
                    Log.d(TAG, "observer: referal code ${accountDetails.referalCode}")
                    Log.d("okhttp_enike", "${accountDetails}")
                    navigate()
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

    private fun navigate() {
        profileImage?.let {
            val intent = if (it == "") {
                Intent(applicationContext, ProfileActivity::class.java)
            } else {
                Intent(applicationContext, MainActivity::class.java)
            }
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun listeners() {
        with(binding) {
            forgetPasswordTxt.setOnClickListener {
                startActivity(Intent(applicationContext, ForgotPasswordEmailFragment::class.java))
            }
            loginBtn.setOnClickListener {
                validateFields {

                    checkIfIsEmailOrPhone {
                        when (it) {
                            ForgotPasswordEmailFragment.CredentialType.UNDEFINED -> {
                                showError("Invalid Email or Phone No.")
                            }
                            ForgotPasswordEmailFragment.CredentialType.PHONE_NUMBER -> {
                                viewModel.setStateEvents(
                                    SignUpStateEvents.Login,
                                    APISignIn(
                                        emailOrPhoneNumber = getPhoneNumber(binding.emailField.text.toString()),
                                        password = binding.passwordField.text.toString(),
                                    )
                                )
                            }
                            ForgotPasswordEmailFragment.CredentialType.EMAIL -> {
                                viewModel.setStateEvents(
                                    SignUpStateEvents.Login,
                                    APISignIn(
                                        emailOrPhoneNumber = binding.emailField.text.toString(),
                                        password = binding.passwordField.text.toString(),
                                    )
                                )
                            }
                        }
                    }
                }
            }
            signUpTxt.setOnClickListener {
                startActivity(Intent(applicationContext, SignUpFragmentOne::class.java))
            }
        }
    }

    private fun checkIfIsEmailOrPhone(callBack: (ForgotPasswordEmailFragment.CredentialType) -> Unit) {

        val delegate = binding.emailField.text.toString().trim()
        if (validateEmail(delegate)) {
            callBack(ForgotPasswordEmailFragment.CredentialType.EMAIL)
            return
        }

        if (validatePhoneNumber(delegate, applicationContext)) {
            callBack(ForgotPasswordEmailFragment.CredentialType.PHONE_NUMBER)
            return
        }

        if (validatePhoneNumber(
                delegate,
                applicationContext
            ).not() && validateEmail(delegate).not()
        ) {
            callBack(ForgotPasswordEmailFragment.CredentialType.UNDEFINED)
            return
        }
    }

    private fun validateFields(callBack: () -> Unit) {
        if (binding.emailField.text.isEmpty() || binding.passwordField.text.isEmpty()) {
            showError("Invalid credentials")
            return
        }
        callBack()
    }
}
