package com.wayapaychat.wayapay.framework.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AllDisputeResponse(
    val data: GetAllDisputeResponseData,
    val message: String,
    val status: Boolean,
    val timestamp: Long
)

data class GetAllDisputeResponseData(
    val content: List<GetAllDisputeResponseDataContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: GetAllDisputeResponseDataPageable,
    val size: Int,
    val sort: GetAllDisputeResponseDataSortX,
    val totalElements: Int,
    val totalPages: Int
)

data class GetAllDisputeResponseDataPageableSort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class GetAllDisputeResponseDataSortX(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class GetAllDisputeResponseDataPageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: GetAllDisputeResponseDataPageableSort,
    val unpaged: Boolean
)

@Parcelize
data class GetAllDisputeResponseDataContent(
    val accepted: Boolean,
    val adminMessage: String,
    val adminResponseDate: String,
    val adminStatus: String,
    val attachment: String,
    val createdBy: Int,
    val customerEmail: String,
    val customerFirstName: String,
    val customerId: String,
    val customerLastName: String,
    val customerName: String,
    val customerPhoneNumber: String,
    val dateCreated: String,
    val dateModified: String,
    val deleted: Boolean,
    val disputeInitiationDate: String,
    val disputeResolutionStatus: String,
    val disputeResolvedBy: Int,
    val disputeStatus: String,
    val disputeType: String,
    val merchantAcceptanceComment: String,
    val merchantCustomerDisputeId: String,
    val merchantEmail: String,
    val merchantId: String,
    val merchantName: String,
    val merchantRejectionDocumentType: String,
    val merchantRejectionReason: String,
    val merchantResponseDate: String,
    val merchantResponseDueDate: String,
    val merchantStatus: String,
    val merchantSuccessfulDebited: Boolean,
    val merchantUserId: Int,
    val modifiedBy: Int,
    val reasonForDisputeInDetails: String,
    val referenceId: String,
    val resolved: Boolean,
    val trackingNumber: String,
    val transactionAmount: Double,
    val transactionDate: String,
    val transactionReference: String
): Parcelable