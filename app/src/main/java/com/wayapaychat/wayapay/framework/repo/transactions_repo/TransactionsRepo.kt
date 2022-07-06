package com.wayapaychat.wayapay.framework.repo.transactions_repo

import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse

import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.framework.network.model.SettlementApiResponse
import com.wayapaychat.wayapay.framework.network.model.TotalRevenueResponse
import com.wayapaychat.wayapay.framework.repo.BaseRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow

interface TransactionsRepo : BaseRepo {

    suspend fun getAllRemoteTransactions(merchantID: String): StateMachine<TransactionsAPIResponse>
    fun getAllTransactions(merchantID: String, status : String , channel : String, date : String,search : String): Flow<StateMachine<List<TransactionData>?>>

    fun getRevenueStats(): Flow<StateMachine<RevenueStats>>

    fun getAllLocalSettlements(status : String , channel : String, date : String,search : String): Flow<StateMachine<List<SettleContent>?>>
    suspend fun getAllRemoteSettlements(): StateMachine<SettlementApiResponse>

    fun getTotalRevenue(endDate : String, merchantId: String,startDate: String): Flow<StateMachine<TotalRevenueResponse>>

}