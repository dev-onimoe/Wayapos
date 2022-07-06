package com.wayapaychat.wayapay.framework.datasource.remote.authDataSource

import com.wayapaychat.wayapay.framework.network.AuthApiService
import com.wayapaychat.wayapay.framework.network.model.APIChangePasswordBody
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOtp
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.framework.network.model.APISignIn

class AuthDatasourceImpl(val authApiService: AuthApiService) : AuthDatasource {
    override suspend fun createAccount(apiCreateAccount: APICreateAccount) =
        authApiService.createAccount(apiCreateAccount)

    override suspend fun businessTypes() =
        authApiService.accountTypes()

    override suspend fun confirmOTP(apiConfirmOtp: APIConfirmOtp) =
        authApiService.confirmOTP(apiConfirmOtp)

    override suspend fun changePassword(body: APIChangePasswordBody) =
        authApiService.changePassword(body)

    override suspend fun getAccountNumber(userId: Int) = authApiService.accountNumber(userId)

    override suspend fun signIn(apiSignIn: APISignIn) = authApiService.signIn(apiSignIn)
    override suspend fun resetPasswordOTPPhone(phone: String) =
        authApiService.resetPasswordOTPPhone(phone)

    override suspend fun resetPasswordOTPEmail(email: String) =
        authApiService.resetPasswordOTPEmail(email)
}
