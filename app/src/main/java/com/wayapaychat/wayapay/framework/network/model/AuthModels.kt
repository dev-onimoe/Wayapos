package com.wayapaychat.wayapay.framework.network.model

import com.google.gson.annotations.Expose

data class APIChangePasswordBody(
    @Expose
    val newPassword: String,
    @Expose
    val otp: Long,
    @Expose
    val phoneOrEmail: String,
)

data class APILoginResponse(
    @Expose
    val code: Int?,
    @Expose
    val data: LoginData?,
    @Expose
    val message: String?,
    @Expose
    val status: Boolean?
)

data class LoginData(
    @Expose
    val corporate: Boolean?,
    @Expose
    val passwordAge: Int?,
    @Expose
    val pinCreated: Boolean?,
    @Expose
    val merchantId : String?,
    @Expose
    val privilege: List<String?>?,
    @Expose
    val roles: List<String?>?,
    @Expose
    val token: String?,
    @Expose
    val user: User?
)

data class User(
    @Expose
    val address: String?,
    @Expose
    val city: String?,
    @Expose
    val dateOfBirth: String?,
    @Expose
    val district: String?,
    @Expose
    val email: String?,
    @Expose
    val firstName: String?,
    @Expose
    val gender: String?,
    @Expose
    val id: Int?,
    @Expose
    val isAccountDeleted: Boolean?,
    @Expose
    val isAccountExpired: Boolean?,
    @Expose
    val isAccountLocked: Boolean?,
    @Expose
    val isActive: Boolean?,
    @Expose
    val isAdmin: Boolean?,
    @Expose
    val isCorporate: Boolean?,
    @Expose
    val isCredentialsExpired: Boolean?,
    @Expose
    val isEmailVerified: Boolean?,
    @Expose
    val isPhoneVerified: Boolean?,
    @Expose
    val lastName: String?,
    @Expose
    val links: List<Any?>?,
    @Expose
    val middleName: String?,
    @Expose
    val permits: List<String?>?,
    @Expose
    val phoneNumber: String?,
    @Expose
    val pinCreated: Boolean?,
    @Expose
    val profileImage: String?,
    @Expose
    val referenceCode: String?,
    @Expose
    val roles: List<String?>?,
    @Expose
    val state: String?
)

data class APIConfirmOTPResponse(
    @Expose
    val timeStamp: String,
    @Expose
    val status: Boolean,
    @Expose
    val message: String
)

data class APIConfirmOtp(
    @Expose
    var otp: String,
    @Expose
    var phoneOrEmail: String

)

data class APISignIn(
    @Expose
    var password: String,
    @Expose
    var emailOrPhoneNumber: String

)

data class APICreateAccount(
    @Expose
    var businessType: String = "",
    @Expose
    var city: String = "",
    @Expose
    var firstName: String = "",
    @Expose
    var orgEmail: String = "",
    @Expose
    var orgName: String = "",
    @Expose
    var orgPhone: String = "",
    @Expose
    var password: String = "",
    @Expose
    var state: String = "",
    @Expose
    var surname: String = "",
    @Expose
    var admin: Boolean = false,
    @Expose
    var email: String = "",
    @Expose
    var gender: String = "",
    @Expose
    var orgType: String = "",
    @Expose
    var phoneNumber: String = "",
    @Expose
    var officeAddress: String = "",
    @Expose
    var dateOfBirth: String = "",
    @Expose
    var referenceCode: String = ""
)

data class APICreateAccountResponse(
    @Expose
    val timestamp: String?,
    @Expose
    val status: Boolean?,
    @Expose
    val message: String?,
    @Expose
    val data: Any?
)

data class BusinessTypes(
    @Expose
    val businessTypeList: List<BusinessTypesItem>,
    @Expose
    val id: Int,
    @Expose
    val totalItems : Int,
    @Expose
    val currentPage : Int
)

data class BusinessTypesItem(
    @Expose
    val businessType: String,
    @Expose
    val id: Int
)
