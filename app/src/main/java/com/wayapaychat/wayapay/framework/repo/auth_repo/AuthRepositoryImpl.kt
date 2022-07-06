package com.wayapaychat.wayapay.framework.repo.auth_repo

import com.wayapaychat.wayapay.framework.datasource.remote.authDataSource.AuthDatasource
import com.wayapaychat.wayapay.framework.network.model.APIAccountNumberResponse
import com.wayapaychat.wayapay.framework.network.model.APIChangePasswordBody
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOTPResponse
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOtp
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.network.model.APISignIn
import com.wayapaychat.wayapay.framework.network.model.BusinessTypes
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val authDatasource: AuthDatasource) : AuthRepository {
    override fun createAccount(apiCreateAccount: APICreateAccount) =
        flow<StateMachine<APICreateAccountResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = authDatasource.createAccount(apiCreateAccount)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun accountNumber(userId: Int) = flow<StateMachine<APIAccountNumberResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = authDatasource.getAccountNumber(userId)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun changePassword(body: APIChangePasswordBody) =
        flow<StateMachine<APICreateAccountResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = authDatasource.changePassword(body)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun resetPasswordOTPPhone(phone: String) =
        flow<StateMachine<APICreateAccountResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = authDatasource.resetPasswordOTPPhone(phone)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun resetPasswordOTPEmail(email: String) =
        flow<StateMachine<APICreateAccountResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = authDatasource.resetPasswordOTPEmail(email)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun signIn(apiSignIn: APISignIn) = flow<StateMachine<APILoginResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = authDatasource.signIn(apiSignIn)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun businessType() = flow<StateMachine<BusinessTypes>> {
        emit(StateMachine.Loading)
        try {
            val response = authDatasource.businessTypes()
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun confirmOtp(apiConfirmOtp: APIConfirmOtp) =
        flow<StateMachine<APIConfirmOTPResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = authDatasource.confirmOTP(apiConfirmOtp)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }
}
