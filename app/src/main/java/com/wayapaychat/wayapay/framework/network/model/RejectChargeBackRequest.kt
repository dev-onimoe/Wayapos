package com.wayapaychat.wayapay.framework.network.model

data class RejectChargeBackRequest(
    val data: Data,
    val message: String,
    val status: Boolean,
    val timestamp: Long
)

data class RejectChargeBackRequestData(
    val accepted: Boolean,
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
    val deleted: Boolean,
    val disputeInitiationDate: String,
    val disputeResolutionStatus: String,
    val disputeStatus: String,
    val disputeType: String,
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
)