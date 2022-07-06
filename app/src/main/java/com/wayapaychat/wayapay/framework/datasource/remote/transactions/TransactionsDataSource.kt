package com.wayapaychat.wayapay.framework.datasource.remote.transactions

import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.SettlementApiResponse
import com.wayapaychat.wayapay.framework.network.model.TotalRevenueResponse
import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse

interface TransactionsDataSource {

    suspend fun getAllTransactions(merchantId: String): TransactionsAPIResponse
    suspend fun getRevenueStats(): RevenueStats
    suspend fun getAllSettlements(): SettlementApiResponse
    suspend fun getTotalRevenue(endDate : String, merchantId: String,startDate: String): TotalRevenueResponse


}