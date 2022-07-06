package com.wayapaychat.wayapay.framework.network.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class APICreateSubscriptionRequest(
    @Expose
    var planId: String,
    @Expose
    var customerId: String,
    @Expose
    var notifyCustomer: Boolean,
    @Expose
    var applyDateAfterFirstPayment: Boolean,
    @Expose
    var startDateAfterFirstPayment: String,
    @Expose
    var linkExpirationDate: String,
    @Expose
    var linkCanExpire: Boolean,
    @Expose
    var totalCount: Int,
    @Expose
    var note: String
) : Parcelable

@Parcelize
data class ViewStateSubscriptionRequest(
    var planId: String = "",
    var customerId: String = "",
    var planAmount: Double = 0.0,
    var customerName: String = "",
    var interval: String = "",
    var planName: String = "",
    var notifyCustomer: Boolean = false,
    var applyDateAfterFirstPayment: Boolean = false,
    var startDateAfterFirstPayment: String = "",
    var linkExpirationDate: String = "",
    var linkCanExpire: Boolean = false,
    var totalCount: Int = 0,
    var note: String = ""
) : Parcelable

data class APICreateSubscriptionResponse(
    @Expose
    val code: String?,
    @Expose
    val data: SubscriptionData?,
    @Expose
    val date: String?,
    @Expose
    val message: String?
)

@Parcelize
data class SubscriptionData(
    @Expose
    val linkCanExpire: Boolean?,
    @Expose
    val firstPaymentMade: String?,
    @Expose
    val note: String?,
    @Expose
    val paymentLinkReference: String?,
    @Expose
    val messageAfterProcessing: String?,
    @Expose
    val totalChargeCount: Int?,
    @Expose
    val merchantKeyMode: String?,
    @Expose
    val planId: String?,
    @Expose
    val subscriptionStatusUpdateReason: String?,
    @Expose
    val linkExpirationDate: String?,
    @Expose
    val reSubscriptionDate: String?,
    @Expose
    val dateUnsubscribed: Boolean?,
    @Expose
    val unsubscribed: Boolean?,
    @Expose
    val status: String?,
    @Expose
    val reference: String?,
    @Expose
    val createdBy: Int?,
    @Expose
    val customerSubscriptionId: String?,
    @Expose
    val merchantId: String?,
    @Expose
    val customerId: String?,
    @Expose
    val paymentLinkId: String?,
    @Expose
    val updatedAt: String?,
    @Expose
    val modifiedBy: String?,
    @Expose
    val deleted: String?,
    @Expose
    val deletedBy: String?,
    @Expose
    val dateDeleted: String?,
    @Expose
    val nextDueDate: String?,
    @Expose
    val subscriptionStartDate: String?,
    @Expose
    val createdAt: String?,
    @Expose
    val startDateAfterFirstPayment: String?,
    @Expose
    val subscriptionPaymentDate: String?,
) : Parcelable

data class APISubscriptionsResponse(
    @Expose
    val code: String,
    @Expose
    val data: SubscriptionsData,
    @Expose
    val date: String,
    @Expose
    val message: String
)

@Parcelize
data class CustomerSubscriptionData(
    @Expose
    val createdAt: String?,
    @Expose
    val createdBy: Int?,
    @Expose
    val dateDeleted: String?,
    @Expose
    val dateModified: String?,
    @Expose
    val deleted: Boolean?,
    @Expose
    val deletedBy: String?,
    @Expose
    val merchantId: String?,
    @Expose
    val merchantKeyMode: String?,
    @Expose
    val modifiedBy: String?,
    @Expose
    val phoneNumber: String?,
    @Expose
    val planId: String?,
    @Expose
    val referenceId: String?,
    @Expose
    val startCountingOnFirstPayment: Boolean?,
    @Expose
    val startDate: String?,
    @Expose
    val status: String?,
    @Expose
    val customerPaymentLink: String?,
    @Expose
    val subscriptionId: String?,
    @Expose
    val updatedAt: String?,
    @Expose
    val updatedBy: String?
) : Parcelable

data class SubscriptionsData(
    @Expose
    val content: List<CustomerSubscriptionData>,
    @Expose
    val empty: Boolean,
    @Expose
    val first: Boolean,
    @Expose
    val last: Boolean,
    @Expose
    val number: Int,
    @Expose
    val numberOfElements: Int,
    @Expose
    val pageable: CustomerPageable,
    @Expose
    val size: Int,
    @Expose
    val sort: CustomerSortX,
    @Expose
    val totalElements: Int,
    @Expose
    val totalPages: Int
)

data class APICreatePlanRequest(
    @Expose
    val planName: String,
    @Expose
    val planDescription: String,
    @Expose
    val totalCount: Int,
    @Expose
    val interval: String,
    @Expose
    val planAmount: String,
    @Expose
    val currency: String = "NGN"
)

data class APICreatePlanResponse(
    @Expose
    val code: String?,
    @Expose
    val data: PlanData?,
    @Expose
    val date: String?,
    @Expose
    val message: String?
)

@Parcelize
data class PlanData(
    @Expose
    val createdAt: String?,
    @Expose
    val createdBy: Int?,
    @Expose
    val currency: String?,
    @Expose
    val dateDeleted: String?,
    @Expose
    val dateModified: String?,
    @Expose
    val deleted: Boolean?,
    @Expose
    val deletedBy: String?,
    @Expose
    val interval: String?,
    @Expose
    val merchantId: String?,
    @Expose
    val merchantKeyMode: String?,
    @Expose
    val modifiedBy: String?,
    @Expose
    val otherDetailsJSON: String?,
    @Expose
    val planAmount: Double?,
    @Expose
    val planDescription: String?,
    @Expose
    val planId: String?,
    @Expose
    val planName: String?,
    @Expose
    val status: String?,
    @Expose
    val totalCount: Int?
) : Parcelable

data class APIPlansResponse(
    @Expose
    val code: String,
    @Expose
    val data: PlansData,
    @Expose
    val date: String,
    @Expose
    val message: String
)

data class PlansData(
    @Expose
    val content: List<PlanData>,
    @Expose
    val empty: Boolean,
    @Expose
    val first: Boolean,
    @Expose
    val last: Boolean,
    @Expose
    val number: Int,
    @Expose
    val numberOfElements: Int,
    @Expose
    val pageable: CustomerPageable,
    @Expose
    val size: Int,
    @Expose
    val sort: CustomerSortX,
    @Expose
    val totalElements: Int,
    @Expose
    val totalPages: Int
)
