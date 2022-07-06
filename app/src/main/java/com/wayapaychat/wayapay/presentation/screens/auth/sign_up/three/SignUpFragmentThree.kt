package com.wayapaychat.wayapay.presentation.screens.auth.sign_up.three

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SignupFragmentThreeBinding
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpStateEvents
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpViewModel
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.four.SignUpOtpFragment
import com.wayapaychat.wayapay.presentation.screens.privacy_policy.PrivacyPolicyActivity
import com.wayapaychat.wayapay.presentation.screens.terms_and_condition.TermsAndConditionActivity
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import com.wayapaychat.wayapay.presentation.utils.helper.PasswordUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragmentThree : BaseActivity() {

    @Inject
    lateinit var cache: Cache
    val viewModel: SignUpViewModel by viewModels()
    lateinit var binding: SignupFragmentThreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.signup_fragment_three)
        initView()
    }

    private fun initView() {
        listeners()
        validatePassword()
        observer()
    }

    private fun setError() {
        setErrorView(binding.confirmPasswordField)
        setErrorView(binding.createPasswordField)
        binding.errorTxt.visibility = View.VISIBLE
    }

    private fun removeError() {
        clearErrorView(binding.confirmPasswordField)
        clearErrorView(binding.createPasswordField)
        binding.errorTxt.visibility = View.GONE
    }

    private fun validatePassword() {
        val passwordUtil1 = PasswordUtil()
        val passwordUtil2 = PasswordUtil()

        binding.createPasswordField.addTextChangedListener(passwordUtil1)
        binding.confirmPasswordField.addTextChangedListener(passwordUtil2)

        passwordUtil2.strengthLevel.observe(
            this
        ) {
            displayStrength(it, true)
        }

        passwordUtil1.strengthLevel.observe(
            this
        ) {
            displayStrength(it)
        }
        binding.strengthOne.visibility = View.GONE
        binding.strengthTwo.visibility = View.GONE
    }

    private fun listeners() {
        with(binding) {
            nextbtn.setOnClickListener {
                if (privacyAndPolicy.isChecked) {
                    val requestBody =
                        cache.getObject(
                            "AccountDetails",
                            APICreateAccount::class.java
                        ) as APICreateAccount
                    requestBody.password = binding.createPasswordField.text.toString()
                    requestBody.referenceCode = binding.referalField.text.toString()
                    viewModel.setStateEvents(SignUpStateEvents.CreateAccount, requestBody)
                } else{
                    showError("Please accept T&C")
                    enableLoginButton(false, binding.nextbtn)
                }
            }
            termsAndConditions.setOnClickListener {
                startActivity(Intent(applicationContext, TermsAndConditionActivity::class.java))
            }
            privacyAndPolicy.setOnClickListener {
                startActivity(Intent(applicationContext, PrivacyPolicyActivity::class.java))
            }
        }
    }

//    private fun loading(state: Boolean) {
//        if (state) {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            binding.progressBar.visibility = View.GONE
//        }
//    }

    private fun observer() {
        viewModel.createAccountObserver.observe(
            this
        ) {
            it?.getContentIfNotHandled()?.let {
                when (it) {
                    is StateMachine.Loading -> {
                        loading(true, binding.progressBar)
                    }

                    is StateMachine.Error -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(message = it.error.localizedMessage)
                    }

                    is StateMachine.Success -> {
                        loading(false, binding.progressBar)
                        startActivity(Intent(applicationContext, SignUpOtpFragment::class.java))
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
                            message = it.error?.message.toString(),
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
        }
    }

    private fun displayStrength(
        strength: PasswordUtil.StrengthLevel,
        isConfirmpassword: Boolean = false
    ) {
        enableLoginButton(false, binding.nextbtn)
        // binding.message.visibility = View.GONE

        if (isConfirmpassword.not()) {
            binding.strengthOne.visibility = View.VISIBLE
            binding.strengthTwo.visibility = View.GONE
            when (strength) {
                PasswordUtil.StrengthLevel.WEAK -> {
                    binding.strengthOneTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                    binding.strengthOneTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                    binding.strengthOneTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                }
                PasswordUtil.StrengthLevel.MEDIUM -> {
                    binding.strengthOneTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )

                    binding.strengthOneTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                    binding.strengthOneTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                }
                PasswordUtil.StrengthLevel.STRONG -> {
                    binding.strengthOneTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                }
                PasswordUtil.StrengthLevel.BULLET_PROOF -> {
                    binding.strengthOneTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthOneTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_green
                        )
                    )

                    if (binding.createPasswordField.text.toString()
                        .isNotEmpty() && binding.confirmPasswordField.text.toString()
                            .isNotEmpty() && binding.createPasswordField.text.toString() == binding.confirmPasswordField.text.toString()
                    ) {
                        enableLoginButton(true, binding.nextbtn)
                        removeError()
                    } else if (binding.confirmPasswordField.text.toString().isEmpty()) {
                        enableLoginButton(false, binding.nextbtn)
                    } else {
                        enableLoginButton(false, binding.nextbtn)
                        setError()
                    }
                }
            }
        } else {
            binding.strengthTwo.visibility = View.VISIBLE
            binding.strengthOne.visibility = View.GONE
            when (strength) {
                PasswordUtil.StrengthLevel.WEAK -> {
                    binding.strengthTwoTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                    binding.strengthTwoTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                    binding.strengthTwoTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                }
                PasswordUtil.StrengthLevel.MEDIUM -> {
                    binding.strengthTwoTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )

                    binding.strengthTwoTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                    binding.strengthTwoTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                }
                PasswordUtil.StrengthLevel.STRONG -> {
                    binding.strengthTwoTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.grey
                        )
                    )
                }
                PasswordUtil.StrengthLevel.BULLET_PROOF -> {
                    binding.strengthTwoTileOne.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileTwo.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileThree.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )
                    binding.strengthTwoTileFour.setBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.deep_orange
                        )
                    )

                    if (binding.createPasswordField.text.toString() == binding.confirmPasswordField.text.toString()) {
                        enableLoginButton(true, binding.nextbtn)
                        removeError()
                    } else {
                        enableLoginButton(false, binding.nextbtn)
                        setError()
                    }
                }
            }
        }
    }
}
