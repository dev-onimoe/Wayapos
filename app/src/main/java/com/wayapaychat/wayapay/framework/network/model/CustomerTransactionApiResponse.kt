package com.wayapaychat.wayapay.framework.network.model

import com.google.gson.annotations.Expose

data class CustomerTransactionApiResponse(
    @Expose
    val `data`: CustomerTransactionApiResponseData,
    @Expose
    val message: String,
    @Expose
    val status: Boolean,
    @Expose
    val timeStamp: Long
)

data class CustomerTransactionApiResponseData(
    @Expose
    val content: List<CustomerTransactionApiResponseContent>,
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
    val pageable: CustomerTransactionApiResponsePageable,
    val size: Int,
    val sort: CustomerTransactionApiResponseSortX,
    val totalElements: Int,
    val totalPages: Int
)

data class CustomerTransactionApiResponseContent(
    @Expose
    val amount: Double,
    @Expose
    val channel: String,
    @Expose
    val currencyCode: String,
    @Expose
    val customerEmail: String,
    @Expose
    val customerId: String,
    @Expose
    val customerIpAddress: String,
    @Expose
    val customerName: String,
    @Expose
    val customerPhone: String,
    @Expose
    val del_flg: Boolean,
    @Expose
    val description: String,
    @Expose
    val fee: Double,
    @Expose
    val id: Int,
    @Expose
    val isFromRecurrentPayment: Boolean,
    @Expose
    val maskedPan: String,
    @Expose
    val merchantEmail: String,
    @Expose
    val merchantId: String,
    @Expose
    val merchantName: String,
    @Expose
    val paymentMetaData: String,
    @Expose
    val rcre_time: String,
    @Expose
    val refNo: String,
    @Expose
    val returnUrl: String,
    @Expose
    val scheme: String,
    @Expose
    val settlementStatus: String,
    @Expose
    val status: String,
    @Expose
    val successfailure: Boolean,
    @Expose
    val tranDate: String,
    @Expose
    val tranId: String,
    @Expose
    val tranflg: Boolean,
    @Expose
    val transactionExpired: Boolean,
    @Expose
    val transactionReceiptSent: Boolean,
    @Expose
    val vendorDate: String
)

data class CustomerTransactionApiResponsePageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: CustomerTransactionApiResponseSort,
    val unpaged: Boolean
)

data class CustomerTransactionApiResponseSort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class CustomerTransactionApiResponseSortX(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)