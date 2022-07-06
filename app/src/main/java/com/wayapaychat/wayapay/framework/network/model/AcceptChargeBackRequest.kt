package com.wayapaychat.wayapay.framework.network.model

data class AcceptChargeBackRequest(
    val merchantAcceptanceComment: String,
    val newRefundAmount: Int
)