package com.wayapaychat.wayapay.framework.repo.auth_repo

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
import com.wayapaychat.wayapay.framework.state_machine.convertErrorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import java.net.SocketTimeoutException

interface AuthRepository {
    fun createAccount(apiCreateAccount: APICreateAccount): Flow<StateMachine<APICreateAccountResponse>>
    fun resetPasswordOTPPhone(phone: String): Flow<StateMachine<APICreateAccountResponse>>
    fun resetPasswordOTPEmail(email: String): Flow<StateMachine<APICreateAccountResponse>>
    fun changePassword(body: APIChangePasswordBody): Flow<StateMachine<APICreateAccountResponse>>
    fun accountNumber(userId: Int): Flow<StateMachine<APIAccountNumberResponse>>
    fun signIn(apiSignIn: APISignIn): Flow<StateMachine<APILoginResponse>>
    fun businessType(): Flow<StateMachine<BusinessTypes>>
    fun confirmOtp(apiConfirmOtp: APIConfirmOtp): Flow<StateMachine<APIConfirmOTPResponse>>
    suspend fun <T> emitError(flow: FlowCollector<StateMachine<T>>, t: Throwable) {
        flow.run {
            when (t) {
                is SocketTimeoutException -> emit(StateMachine.TimeOut(t))
                is retrofit2.HttpException -> {
                    val status = t.code()
                    val res = convertErrorBody(t)
                    emit(StateMachine.GenericError(status, res))
                }
                is Exception -> emit(StateMachine.Error(t))
            }
        }
    }
}
