package com.wayapaychat.wayapay.framework.network.model

import com.google.gson.annotations.Expose

data class TotalRevenueResponse(
    @Expose
    val data: TotalRevenueResponseData,
    @Expose
    val message: String,
    @Expose
    val status: Boolean,
    @Expose
    val timeStamp: Long
)

data class DateRangeResult(
    @Expose
    val month: String,
    @Expose
    val totalRevenue: Double?,
    @Expose
    val year: Int
)

data class TotalRevenueResponseData(
    @Expose
    val dateRangeResult: List<DateRangeResult>,
    @Expose
    val totalRevenueForSelectedDateRange: Double
)