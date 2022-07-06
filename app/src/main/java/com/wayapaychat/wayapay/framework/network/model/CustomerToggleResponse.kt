package com.wayapaychat.wayapay.framework.network.model

import com.google.gson.annotations.Expose

data class CustomerToggleResponse(
    @Expose
    val code: String,
    @Expose
    val `data`: ToggleData,
    @Expose
    val date: String,
    @Expose
    val message: String
)

data class ToggleData(
    @Expose
    val createdAt: String,
    @Expose
    val createdBy: Int,
    @Expose
    val customerAvoided: Boolean,
    @Expose
    val customerId: String,
    @Expose
    val dateCustomerAvoided: String,
    @Expose
    val dateDeleted: Any,
    @Expose
    val deleted: Boolean,
    @Expose
    val email: String,
    @Expose
    val firstName: String,
    @Expose
    val lastName: String,
    @Expose
    val merchantId: String,
    @Expose
    val merchantKeyMode: String,
    @Expose
    val phoneNumber: String,
    @Expose
    val status: String,
    @Expose
    val updatedAt: String,
    @Expose
    val updatedBy: Int
)