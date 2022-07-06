package com.wayapaychat.wayapay.presentation.utils.constants

import com.wayapaychat.wayapay.BuildConfig

object ApiConstants {
    val BASE_URL = if (BuildConfig.DEBUG) "https://services.staging.wayapay.ng/auth-service/api/v1/" else ""
    val WAYAPAY_BASE_URL = if (BuildConfig.DEBUG) "https://services.staging.wayapay.ng/identity-manager-service/api/v1/" else ""
    val WAYAPAY_PAYMENT_BASE_URL = if (BuildConfig.DEBUG) "https://services.staging.wayapay.ng/payment-gateway/api/v1/" else ""
    val WAYAPAY_DISPUTE_BASE_URL = if(BuildConfig.DEBUG) "https://services.staging.wayapay.ng/dispute-service/api/v2/" else ""
    val WAYAPAY_NOTIFICATIONS_BASE_URL = if(BuildConfig.DEBUG) "https://services.staging.wayabank.ng/notification-service/" else ""

    object EndPoints {
        const val CREATE_ACCOUNT =
            "auth/create-corporate?devicePlatform=ANDROID&mobile=true&normal=true&tablet=true"
        const val BUSINESS_TYPE = "business/type/find/all"
        const val CONFIRM_OTP = "auth/verify-otp"
        const val SIGN_IN = "auth/login"
        const val RESET_PASSWORD_PHONE = "password/forgot-password/byPhone?"
        const val RESET_PASSWORD_EMAIL = "password/forgot-password/byEmail?"
        const val CHANGE_PASSWORD = "password/forgot-password"
        const val CREATE_PAYMENT_LINK = "webpos/payment-link"
        const val ALL_PAYMENT_LINKS = "webpos/payment-link/filter-search?size=1000"
        const val CREATE_CUSTOMER = "webpos/customer"
        const val CUSTOMERS = "webpos/customer/filter-search?size=100"
        const val CUSTOMER = "webpos/customer"
        const val CUSTOMER_TRANSACTIONS = "transactions"
        const val TOGGLE_CUSTOMER = "webpos/customer/toggle-avoid"
        const val SUBSCRIPTION = "webpos/subscription"
        const val CREATE_SUB = "webpos/customer-subscription"
        const val CREATE_PLAN = "webpos/plan"
        const val PLANS = "webpos/plan/filter-search?size=100"
        const val SUBSCRIPTIONS = "webpos/customer-subscription/filter-search?size=100"
        const val PROFILE = "profile"
        const val DELETE_PAYMENT_LINK = "/webpos/payment-link"
        const val UPDATE_PROFILE = "profile/update-corporate-profile"
        const val UPDATE_PROFILE_IMAGE = "profile/update-profile-image"


        const val TRANSACTIONS = "report/query"
        const val SETTLEMENTS = "transactions/settlements/fetch-all"
        const val REVENUE_STATS = "transactions/report/overview"

        const val GET_TOTAL_REVENUE = "transactions/report/year-month-stats"

        const val GET_ALL_DISPUTE = "dispute/filter-search?size=10000&order=DESC"
        const val ACCEPT_CHARGE_BACK_DISPUTE = "dispute/accept-dispute"
        const val REJECT_CHARGE_BACK_DISPUTE = "dispute/reject-dispute"
        const val GET_ALL_NOTIFICATIONS = "in-app-notifications"
    }
}
