package com.wayapaychat.wayapay.framework.datasource.remote.authDataSource

import com.wayapaychat.wayapay.framework.network.model.APIAccountNumberResponse
import com.wayapaychat.wayapay.framework.network.model.APIChangePasswordBody
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOTPResponse
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOtp
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.network.model.APISignIn
import com.wayapaychat.wayapay.framework.network.model.BusinessTypes

interface AuthDatasource {
    suspend fun createAccount(apiCreateAccount: APICreateAccount): APICreateAccountResponse
    suspend fun businessTypes(): BusinessTypes
    suspend fun confirmOTP(apiConfirmOtp: APIConfirmOtp): APIConfirmOTPResponse
    suspend fun signIn(apiSignIn: APISignIn): APILoginResponse
    suspend fun resetPasswordOTPPhone(phone: String): APICreateAccountResponse
    suspend fun resetPasswordOTPEmail(email: String): APICreateAccountResponse
    suspend fun changePassword(body: APIChangePasswordBody): APICreateAccountResponse
    suspend fun getAccountNumber(userId: Int): APIAccountNumberResponse
}
