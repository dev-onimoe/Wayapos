package com.wayapaychat.wayapay.framework.network

import com.wayapaychat.wayapay.framework.network.model.APICreateAccount

import com.wayapaychat.wayapay.framework.network.model.APICreateAccountResponse
import com.wayapaychat.wayapay.framework.network.model.BusinessTypes
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOtp
import com.wayapaychat.wayapay.framework.network.model.APIConfirmOTPResponse
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.network.model.APISignIn
import com.wayapaychat.wayapay.framework.network.model.APIChangePasswordBody
import com.wayapaychat.wayapay.framework.network.model.APIAccountNumberResponse
import com.wayapaychat.wayapay.framework.network.model.APIProfileResponse
import com.wayapaychat.wayapay.framework.network.model.APIUpdateProfileRequest
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLinkResponse
import com.wayapaychat.wayapay.framework.network.model.APIPaymentLinks
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomersResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomerResponse
import com.wayapaychat.wayapay.framework.network.model.CustomerToggleResponse
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanResponse
import com.wayapaychat.wayapay.framework.network.model.APISubscriptionsResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionRequest
import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponse

import com.wayapaychat.wayapay.framework.network.model.APICreatePlanRequest
import com.wayapaychat.wayapay.framework.network.model.APIPlansResponse
import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.NotificationsResponse
import com.wayapaychat.wayapay.framework.network.model.RejectChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.SettlementApiResponse
import com.wayapaychat.wayapay.framework.network.model.TotalRevenueResponse
import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.ACCEPT_CHARGE_BACK_DISPUTE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.ALL_PAYMENT_LINKS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.BUSINESS_TYPE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CHANGE_PASSWORD
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CONFIRM_OTP
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CREATE_ACCOUNT
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CREATE_CUSTOMER
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CREATE_PAYMENT_LINK
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CREATE_PLAN
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CREATE_SUB
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CUSTOMER
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CUSTOMERS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.CUSTOMER_TRANSACTIONS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.DELETE_PAYMENT_LINK
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.GET_ALL_DISPUTE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.GET_ALL_NOTIFICATIONS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.GET_TOTAL_REVENUE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.PLANS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.PROFILE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.REJECT_CHARGE_BACK_DISPUTE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.RESET_PASSWORD_EMAIL
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.RESET_PASSWORD_PHONE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.REVENUE_STATS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.SETTLEMENTS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.SIGN_IN
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.SUBSCRIPTION
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.SUBSCRIPTIONS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.TOGGLE_CUSTOMER
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.TRANSACTIONS
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.UPDATE_PROFILE
import com.wayapaychat.wayapay.presentation.utils.constants.ApiConstants.EndPoints.UPDATE_PROFILE_IMAGE
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.PATCH

interface AuthApiService {
    @POST(CREATE_ACCOUNT)
    suspend fun createAccount(@Body body: APICreateAccount): APICreateAccountResponse

    @GET(BUSINESS_TYPE)
    suspend fun accountTypes(): BusinessTypes

    @POST(CONFIRM_OTP)
    suspend fun confirmOTP(@Body apiConfirmOtp: APIConfirmOtp): APIConfirmOTPResponse

    @POST(SIGN_IN)
    suspend fun signIn(@Body body: APISignIn): APILoginResponse

    @GET(RESET_PASSWORD_PHONE)
    suspend fun resetPasswordOTPPhone(@Query("phoneNumber") phoneNumber: String): APICreateAccountResponse

    @GET(RESET_PASSWORD_EMAIL)
    suspend fun resetPasswordOTPEmail(@Query("email") email: String): APICreateAccountResponse

    @POST(CHANGE_PASSWORD)
    suspend fun changePassword(@Body body: APIChangePasswordBody): APICreateAccountResponse

    @GET("https://services.staging.wayapay.ng/account-service/account/getAccounts/{userId}")
    suspend fun accountNumber(@Path("userId") userId: Int): APIAccountNumberResponse

    @GET("$PROFILE/{profileId}")
    suspend fun profile(
        @Path("profileId") profileId: Int
    ): APIProfileResponse

    @PUT("$UPDATE_PROFILE/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Int,
        @Body body: APIUpdateProfileRequest
    ): APICreateAccountResponse

    @Multipart
    @POST("$UPDATE_PROFILE_IMAGE/{type}/{userId}")
    suspend fun updateProfileImage(
        @Part file: MultipartBody.Part,
        @Path("type") type: String,
        @Path("userId") userId: Int,
    ): APICreateAccountResponse
}

interface WayapayApiService {
    @POST(CREATE_PAYMENT_LINK)
    suspend fun createPaymentLink(@Body body: APICreatePaymentLink): APICreatePaymentLinkResponse

    @GET(ALL_PAYMENT_LINKS)
    suspend fun paymentLinks(
        @Query("endCreatedAt") endDate: String?,
        @Query("status") status: String?,
        @Query("startCreatedAt") startDate: String?,
    ): APIPaymentLinks

    @POST(CREATE_CUSTOMER)
    suspend fun createCustomer(@Body body: APICreateCustomerRequest): APICreateCustomerResponse

    @GET(CUSTOMERS)
    suspend fun customers(): APICustomersResponse

    @GET("$CUSTOMER/{customerId}")
    suspend fun customer(
        @Path("customerId") customerId: String
    ): APICustomerResponse

    @GET("$CUSTOMER_TRANSACTIONS/{customerId}")
    suspend fun customerTransactions(
        @Path("customerId") customerId: String
    ): CustomerTransactionApiResponse

    @PATCH("$TOGGLE_CUSTOMER/{customerId}")
    suspend fun toggleCustomer(
        @Path("customerId") customerId: String,
        @Query("avoid") avoid: Boolean
    ): CustomerToggleResponse

    @POST(CREATE_SUB)
    suspend fun createSubscription(
        @Body requestBody: APICreateSubscriptionRequest
    ): APICreateSubscriptionResponse

    @GET(SUBSCRIPTION)
    suspend fun subscription(
        @Path("subscriptionId") subscriptionId: String
    ): APICreateSubscriptionResponse

    @GET(SUBSCRIPTIONS)
    suspend fun subscriptions(): APISubscriptionsResponse

    @POST(CREATE_PLAN)
    suspend fun createPlan(
        @Body requestBody: APICreatePlanRequest
    ): APICreatePlanResponse

    @GET("$CREATE_PLAN/{planId}")
    suspend fun plan(
        @Path("planId") planId: String
    ): APICreatePlanResponse

    @GET(PLANS)
    suspend fun plans(): APIPlansResponse

    @DELETE("$DELETE_PAYMENT_LINK/{linkId}")
    suspend fun removePaymentLink(
        @Path("linkId") linkId: String
    ): APIProfileResponse
}

interface WayapayTransactionApiService {

    @GET("$TRANSACTIONS/{merchantId}")
    suspend fun getAllTransactions(
        @Path("merchantId") merchantId: String
    ): TransactionsAPIResponse

    @GET(REVENUE_STATS)
    suspend fun getRevenueStats(): RevenueStats

    @GET(SETTLEMENTS)
    suspend fun getAllSettlements(): SettlementApiResponse

    @GET("$CUSTOMER_TRANSACTIONS/{customerId}")
    suspend fun customerTransactions(
        @Path("customerId") customerId: String
    ): CustomerTransactionApiResponse

    @GET(GET_TOTAL_REVENUE)
    suspend fun getTotalRevenue(
        @Query("endDate") endDate: String?,
        @Query("merchantId") merchantId: String?,
        @Query("startDate") startDate: String?,
    ): TotalRevenueResponse

}

interface DisputeApiService {
    @GET(GET_ALL_DISPUTE)
    suspend fun getAllDispute(): AllDisputeResponse

    @PUT("$ACCEPT_CHARGE_BACK_DISPUTE/{merchantCustomerDisputeId}")
    suspend fun acceptChargeBack(
        @Path("merchantCustomerDisputeId") merchantCustomerDisputeId: String,
        @Body data : AcceptChargeBackRequest
    ): AllDisputeResponse

    @Multipart
    @POST("$REJECT_CHARGE_BACK_DISPUTE/{merchantCustomerDisputeId}")
    suspend fun rejectChargeBack(
        @Path("merchantCustomerDisputeId") merchantCustomerDisputeId: String,
        @Part merchantRejectionDocumentType: MultipartBody.Part,
        @Part disputeRejectReason: MultipartBody.Part,
        @Part files: MultipartBody.Part
    ): RejectChargeBackRequest

}

interface NotificationsApiService {
    @GET("$GET_ALL_NOTIFICATIONS/{id}")
    suspend fun getAllNotifications(
        @Path("id") id: String,
    ): NotificationsResponse
}
