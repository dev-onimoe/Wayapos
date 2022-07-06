package com.wayapaychat.wayapay.framework.network.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

data class APICreatePaymentLink(
    @Expose
    val paymentLinkType: String,
    @Expose
    val paymentLinkName: String,
    @Expose
    val description: String,
    @Expose
    val payableAmount: String,
    @Expose
    val totalCount: Int,
    @Expose
    val chargeInterval: String,
    @Expose
    val currency: String = "NGN",
    @Expose
    val successMessage: String = "",
    @Expose
    val phoneNumber: String = "",
    @Expose
    val redirectLink: String = "",
    @Expose
    val customerPaymentLink: String = "",
    @Expose
    val otherDetailsJSON: String = "{}",
)

data class APICreatePaymentLinkResponse(
    @Expose
    val code: String,
    @Expose
    val message: String,
    @Expose
    val date: String,
    @Expose
    val data: CreatePaymentData
)

data class CreatePaymentData(
    @Expose
    val merchantId: String,
    @Expose
    val paymentLinkId: String,
    @Expose
    val paymentLinkType: String,
    @Expose
    val createdAt: String,
    @Expose
    val dateModified: String,
    @Expose
    val modifiedBy: String,
    @Expose
    val createdBy: String,
    @Expose
    val paymentLinkName: String,
    @Expose
    val description: String,
    @Expose
    val payableAmount: String,
    @Expose
    val currency: String,
    @Expose
    val amountText: String,
    @Expose
    val successMessage: String,
    @Expose
    val redirectLink: String,
    @Expose
    val customerPaymentLink: String,
    @Expose
    val otherDetailsJSON: String,
    @Expose
    val status: Boolean,
    @Expose
    val deleted: String,
    @Expose
    val dateDeleted: String,
    @Expose
    val deletedBy: String,
    @Expose
    val merchantKeyMode: String,
    @Expose
    val paymentLinkReference: String
)

data class APIPaymentLinks(
    @Expose
    val code: String,
    @Expose
    val data: APIPaymentLinkData,
    @Expose
    val date: String,
    @Expose
    val message: String
)

data class APIPaymentLinkData(
    @Expose
    val content: List<Content>,
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
    val pageable: Pageable,
    @Expose
    val size: Int,
    @Expose
    val sort: SortX,
    @Expose
    val totalElements: Int,
    @Expose
    val totalPages: Int
)

@Parcelize
data class Content(
    @Expose
    val amountText: String,
    @Expose
    val createdAt: String,
    @Expose
    val createdBy: Int,
    @Expose
    val currency: String,
    @Expose
    val customerPaymentLink: String,
    @Expose
    val dateDeleted: String,
    @Expose
    val dateModified: String,
    @Expose
    val deleted: Boolean,
    @Expose
    val deletedBy: String,
    @Expose
    val description: String,
    @Expose
    val merchantId: String,
    @Expose
    val merchantKeyMode: String,
    @Expose
    val modifiedBy: String,
    @Expose
    val otherDetailsJSON: String,
    @Expose
    val payableAmount: Double,
    @Expose
    val paymentLinkId: String,
    @Expose
    val paymentLinkName: String,
    @Expose
    val paymentLinkReference: String,
    @Expose
    val paymentLinkType: String,
    @Expose
    val redirectLink: String,
    @Expose
    val status: String,
    @Expose
    val successMessage: String
) : Parcelable

data class Pageable(
    @Expose
    val offset: Int,
    @Expose
    val pageNumber: Int,
    @Expose
    val pageSize: Int,
    @Expose
    val paged: Boolean,
    @Expose
    val sort: Sort,
    @Expose
    val unpaged: Boolean
)

data class Sort(
    @Expose
    val empty: Boolean,
    @Expose
    val sorted: Boolean,
    @Expose
    val unsorted: Boolean
)

data class SortX(
    @Expose
    val empty: Boolean,
    @Expose
    val sorted: Boolean,
    @Expose
    val unsorted: Boolean
)
