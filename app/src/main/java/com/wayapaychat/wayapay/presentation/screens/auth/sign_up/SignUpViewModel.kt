package com.wayapaychat.wayapay.presentation.screens.auth.sign_up

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.APIAccountNumberResponse
import com.wayapaychat.wayapay.framework.network.model.APIChangePasswordBody
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOTPResponse
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOtp
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.network.model.APISignIn
import com.wayapaychat.wayapay.framework.network.model.BusinessTypes
import com.wayapaychat.wayapay.framework.repo.auth_repo.AuthRepository
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.utils.helper.Event
import com.wayapaychat.wayapay.presentation.utils.helper.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _createAccountObserver =
        MutableLiveData<Event<StateMachine<APICreateAccountResponse>>>()
    val createAccountObserver: LiveData<Event<StateMachine<APICreateAccountResponse>>> =
        _createAccountObserver

    private val _accountNumberObserver =
        MutableLiveData<StateMachine<APIAccountNumberResponse>>()
    val accountNumberObserver: LiveData<StateMachine<APIAccountNumberResponse>> =
        _accountNumberObserver

    private val _businessTypesObserver = MutableLiveData<StateMachine<BusinessTypes>>()
    val businessTypesObserver: LiveData<StateMachine<BusinessTypes>> =
        _businessTypesObserver

    private val _confirmOtpObserver = MutableLiveData<StateMachine<APIConfirmOTPResponse>>()
    val confirmOtpObserver: LiveData<StateMachine<APIConfirmOTPResponse>> =
        _confirmOtpObserver

    private val _loginObserver = MutableLiveData<StateMachine<APILoginResponse>>()
    val loginObserver: LiveData<StateMachine<APILoginResponse>> =
        _loginObserver

    private val _requestOtpPhoneObserver = MutableLiveData<StateMachine<APICreateAccountResponse>>()
    val requestOtpPhoneObserver: LiveData<StateMachine<APICreateAccountResponse>> =
        _requestOtpPhoneObserver

    private val _requestOtpEmailObserver = MutableLiveData<StateMachine<APICreateAccountResponse>>()
    val requestOtpEmailObserver: LiveData<StateMachine<APICreateAccountResponse>> =
        _requestOtpEmailObserver

    private val _changePasswordObserver = MutableLiveData<StateMachine<APICreateAccountResponse>>()
    val changePasswordObserver: LiveData<StateMachine<APICreateAccountResponse>> =
        _changePasswordObserver

    fun setStateEvents(stateEvents: SignUpStateEvents, requestBody: Any?) {
        when (stateEvents) {
            is SignUpStateEvents.CreateAccount -> {
                createAccount(requestBody as APICreateAccount)
            }
            is SignUpStateEvents.BusinessTypes -> {
                getBusinessTypes()
            }
            is SignUpStateEvents.ConfirmOtp -> {
                confirmOTP(requestBody as APIConfirmOtp)
            }
            is SignUpStateEvents.Login -> {
                login(requestBody as APISignIn)
            }
            is SignUpStateEvents.SendOtpEmail -> {
                requestOtpEmail(requestBody as String)
            }
            is SignUpStateEvents.SendOtpPhoneNumber -> {
                requestOtpPhoneNumber(requestBody as String)
            }
            is SignUpStateEvents.ChangePassword -> {
                changePassword(requestBody as APIChangePasswordBody)
            }
            is SignUpStateEvents.AccountNumber -> {
                fetchAccountNumber(requestBody as Int)
            }
        }
    }

    private fun fetchAccountNumber(userId: Int) {
        authRepository.accountNumber(userId).onEach {
            _accountNumberObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun requestOtpEmail(email: String) {
        authRepository.resetPasswordOTPEmail(email).onEach {
            _requestOtpEmailObserver.value = it
        }.launchIn(
            viewModelScope
        )
    }

    private fun requestOtpPhoneNumber(phone: String) {
        authRepository.resetPasswordOTPPhone(phone).onEach {
            _requestOtpPhoneObserver.value = it
        }.launchIn(
            viewModelScope
        )
    }
    private fun changePassword(body: APIChangePasswordBody) {
        authRepository.changePassword(body).onEach {
            _changePasswordObserver.value = it
        }.launchIn(
            viewModelScope
        )
    }

    private fun login(requestBody: APISignIn) {
        authRepository.signIn(requestBody).onEach {
            _loginObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun confirmOTP(apiConfirmOtp: APIConfirmOtp) {
        authRepository.confirmOtp(apiConfirmOtp).onEach {
            _confirmOtpObserver.value = it
        }.launchIn(
            viewModelScope
        )
    }

    private fun getBusinessTypes() {
        authRepository.businessType().onEach {
            _businessTypesObserver.value = it
        }.launchIn(
            viewModelScope
        )
    }

    private fun createAccount(requestBody: APICreateAccount) {
        Log.d(TAG, "listeners: Clicked")

        authRepository.createAccount(requestBody).onEach {
            Log.d(TAG, "listeners: Clicked")
            _createAccountObserver.value = Event(it)
        }.launchIn(
            viewModelScope
        )
    }
}

sealed class SignUpStateEvents {
    object CreateAccount : SignUpStateEvents()
    object BusinessTypes : SignUpStateEvents()
    object ConfirmOtp : SignUpStateEvents()
    object Login : SignUpStateEvents()
    object SendOtpEmail : SignUpStateEvents()
    object SendOtpPhoneNumber : SignUpStateEvents()
    object ChangePassword : SignUpStateEvents()
    object AccountNumber : SignUpStateEvents()
}
