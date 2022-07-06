package com.wayapaychat.wayapay.presentation.screens.auth.sign_up.one

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SignupFragmentOneBinding
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.framework.network.model.BusinessTypesItem
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpStateEvents
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.SignUpViewModel
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.dialog.BusinessTypesBottomSheet
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.SignUpFragmentTwo
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import com.wayapaychat.wayapay.presentation.utils.helper.getPhoneNumber
import com.wayapaychat.wayapay.presentation.utils.helper.validateEmail
import com.wayapaychat.wayapay.presentation.utils.helper.validatePhoneNumber
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragmentOne :
    BaseActivity() {
    val viewModel: SignUpViewModel by viewModels()
    lateinit var binding: SignupFragmentOneBinding

    @Inject
    lateinit var cache: Cache
    private lateinit var businessTypes: List<BusinessTypesItem>
    private lateinit var businessType: String
    private var lastStateEvent: SignUpStateEvents? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.signup_fragment_one)
        initView()
    }

    private fun initView() {
        listeners()
        observer()
        viewModel.setStateEvents(SignUpStateEvents.BusinessTypes, requestBody = null)
    }

    private fun observer() {
        with(viewModel) {
            businessTypesObserver.observe(
                this@SignUpFragmentOne
            ) {
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
                        val types = listOf<BusinessTypesItem>(
                            BusinessTypesItem("Catering and Food", 1),
                            BusinessTypesItem("Cakes and Pastries", 2),
                            BusinessTypesItem("Event Planning",3),
                            BusinessTypesItem("Music and DJ", 4),
                            BusinessTypesItem("Event Courier and Logistics",5),
                            BusinessTypesItem("Health and Skin Care", 6),
                            BusinessTypesItem("Fashion",7),
                            BusinessTypesItem("Clothing, Accessories, and Shoes", 8),
                            BusinessTypesItem("Makeup",9),
                            BusinessTypesItem("Computer, Accessories, and Services", 10),
                            BusinessTypesItem("Babies and Kids",11),
                            BusinessTypesItem("Art, Crafts, and Collectibles", 12),
                            BusinessTypesItem("Home and Gardens",13),
                            BusinessTypesItem("Groceries", 14),
                            BusinessTypesItem("Transportation",15),
                            BusinessTypesItem("Pharmacy/Hospitals", 16),
                            BusinessTypesItem("Aggregator",17),
                            BusinessTypesItem("Agent",18),
                            BusinessTypesItem("Agency banking",19),
                            BusinessTypesItem("Financial institution",20),
                            BusinessTypesItem("Church",21),
                            BusinessTypesItem("Mosque",22),
                            BusinessTypesItem("School",23),
                            BusinessTypesItem("Supermarket",24),
                            BusinessTypesItem("E-Commerce",25),
                            BusinessTypesItem("Consulting",26),
                            BusinessTypesItem("Hairs",27),
                            BusinessTypesItem("Finance",28)
                        )
                        businessTypes = types
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
                            message = it.error!!.message!!.toString(),
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
        }
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

    private fun listeners() {
        with(binding) {
            nextbtn.setOnClickListener {
                validateFields { requestBody ->
                    cache.putObject("AccountDetails", requestBody)
                    startActivity(Intent(applicationContext, SignUpFragmentTwo::class.java))
                }
            }
            loginNow.setOnClickListener {
                finish()
            }
            businessType.setOnClickListener {
                BusinessTypesBottomSheet(businessTypes) {
                    this@SignUpFragmentOne.businessType = it.businessType
                    binding.businessType.setText(it.businessType)
                }.show(supportFragmentManager, "Business_type")
            }
        }
    }

    private fun validateFields(callBack: (APICreateAccount) -> Unit) {
        with(binding) {
            if (binding.businessNameField.text.isEmpty()
                || binding.businessNameField.text.toString() == " "
            ) {
                showError("Business name cannot be blank")
                return
            }

            if (binding.businessEmailField.text.isEmpty() || validateEmail(binding.businessEmailField.text.toString()).not()) {
                showError("Invalid Email address")
                return
            }

            if (binding.phoneNumberField.text.toString().isEmpty()) {
                showError("Business Phone number is Required")
                return
            }

            if (binding.phoneNumberField.text.toString()
                    .isEmpty() || binding.phoneNumberField.text.toString().length < 10
                || binding.phoneNumberField.text.toString().length > 10
            ) {
                showError("Invalid mobile number")
                return
            }
            if (binding.businessType.text.toString().isEmpty()) {
                showError("Select a Business Type")
                return
            }
        }
        val requestBody = APICreateAccount(
            businessType = businessType,
            orgEmail = binding.businessEmailField.text.toString(),
            orgPhone = getPhoneNumber(binding.phoneNumberField.text.toString()),
            orgName = binding.businessNameField.text.toString(),
        )
        callBack(requestBody)
    }
}
