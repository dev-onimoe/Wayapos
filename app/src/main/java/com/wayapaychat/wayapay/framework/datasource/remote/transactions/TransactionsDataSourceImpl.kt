package com.wayapaychat.wayapay.framework.datasource.remote.transactions

import com.wayapaychat.wayapay.framework.network.WayapayTransactionApiService
import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponse
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.SettlementApiResponse
import com.wayapaychat.wayapay.framework.network.model.TotalRevenueResponse
import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse

class TransactionsDataSourceImpl(
    private val wayapayApiService: WayapayTransactionApiService, ) : TransactionsDataSource {
    override suspend fun getAllTransactions(merchantId: String): TransactionsAPIResponse {
        return wayapayApiService.getAllTransactions(merchantId)
    }

    override suspend fun getRevenueStats(): RevenueStats {
        return wayapayApiService.getRevenueStats()
    }

    override suspend fun getAllSettlements(): SettlementApiResponse {
        return wayapayApiService.getAllSettlements()
    }

    override suspend fun getTotalRevenue(
        endDate: String,
        merchantId: String,
        startDate: String
    ): TotalRevenueResponse {
        return wayapayApiService.getTotalRevenue(endDate,merchantId, startDate)
    }

}