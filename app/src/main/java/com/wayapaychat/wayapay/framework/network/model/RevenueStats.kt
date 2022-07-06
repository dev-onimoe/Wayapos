package com.wayapaychat.wayapay.framework.network.model

import com.google.gson.annotations.Expose

data class RevenueStats(
    @Expose
    val data: Stats?,
    @Expose
    val message: String,
    @Expose
    val status: Boolean,
    @Expose
    val timeStamp: Long
)

data class Stats(
    @Expose
    val paymentChannelStats: List<PaymentChannelStat>,
    @Expose
    val refusalErrorStats: List<RefusalErrorStat>,
    @Expose
    val revenueStats: RevenueStatsX,
    @Expose
    val settlementStats: SettlementStats,
    @Expose
    val statusStats: List<StatusStat>,
    @Expose
    val successErrorStats: List<SuccessErrorStat>,
    @Expose
    val yearMonthStats: List<YearMonthStat>
)

data class PaymentChannelStat(
    @Expose
    val channel: String,
    @Expose
    val count: Int
)

data class RefusalErrorStat(
    @Expose
    val count: Int,
    @Expose
    val status: String
)

data class RevenueStatsX(
    @Expose
    val grossRevenue: Double,
    @Expose
    val netRevenue: Double
)

data class SettlementStats(
    @Expose
    val latestSettlement: Double,
    @Expose
    val nextSettlement: Double
)

data class StatusStat(
    @Expose
    val count: Int,
    @Expose
    val status: String
)

data class SuccessErrorStat(
    @Expose
    val count: Int,
    @Expose
    val status: String
)

data class YearMonthStat(
    @Expose
    val merchantId: String,
    @Expose
    val month: String,
    @Expose
    val totalRevenue: Double,
    @Expose
    val year: Int
)