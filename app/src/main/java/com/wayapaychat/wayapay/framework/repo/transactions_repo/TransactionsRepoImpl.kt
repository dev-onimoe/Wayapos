package com.wayapaychat.wayapay.framework.repo.transactions_repo

import android.util.Log
import com.wayapaychat.wayapay.framework.datasource.db.dao.transaction_dao.TransactionDao
import com.wayapaychat.wayapay.framework.datasource.remote.transactions.TransactionsDataSource
import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.SettlementApiResponse
import com.wayapaychat.wayapay.framework.network.model.TotalRevenueResponse
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.framework.state_machine.convertErrorBody
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import kotlinx.coroutines.flow.Flow

class TransactionsRepoImpl(
    val transactionsDataSource: TransactionsDataSource,
    val transactionsLocalDataSource: TransactionDao
) : TransactionsRepo {
    override suspend fun getAllRemoteTransactions(merchantID: String) : StateMachine<TransactionsAPIResponse> {
            try {
                val remoteResponse = transactionsDataSource.getAllTransactions(merchantID)
                remoteResponse.data.map {
                    transactionsLocalDataSource.saveTransaction(it)
                }
                Log.d("testing","done with remote")
                return StateMachine.Success(remoteResponse)
            } catch (t: Throwable) {
               return when (t) {
                   is SocketTimeoutException -> StateMachine.TimeOut(t)
                   is retrofit2.HttpException -> {
                       val status = t.code()
                       val res = convertErrorBody(t)
                       StateMachine.GenericError(status, res)
                   }
                   else -> { StateMachine.Loading}
               }
            }

        }

    override fun getAllTransactions(merchantID: String, status : String , channel : String,date : String,search : String) =
        flow<StateMachine<List<TransactionData>?>> {
            Log.d("testing","in local")
            emit(StateMachine.Loading)
            try {
                getAllRemoteTransactions(merchantID)
                val localresponse = transactionsLocalDataSource.getTransactions(status, channel,date,search)
                emit(StateMachine.Success(localresponse))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }





    override fun getRevenueStats() = flow<StateMachine<RevenueStats>> {
        emit(StateMachine.Loading)
        try {
            val response = transactionsDataSource.getRevenueStats()
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }




    override fun getAllLocalSettlements(status: String, channel: String,date : String,search : String) = flow<StateMachine<List<SettleContent>?>> {

        emit(StateMachine.Loading)
        try {
            getAllRemoteSettlements()
            val localresponse = transactionsLocalDataSource.getSettlements(status, channel,date,search)
            emit(StateMachine.Success(localresponse))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override suspend fun getAllRemoteSettlements() : StateMachine<SettlementApiResponse> {
        try {
            val remoteResponse = transactionsDataSource.getAllSettlements()
            remoteResponse.data.content.map {
                transactionsLocalDataSource.saveSettlement(it)
            }
            Log.d("testing", "done with remote")
            return StateMachine.Success(remoteResponse)
        } catch (t: Throwable) {
            return when (t) {
                is SocketTimeoutException -> StateMachine.TimeOut(t)
                is retrofit2.HttpException -> {
                    val status = t.code()
                    val res = convertErrorBody(t)
                    StateMachine.GenericError(status, res)
                }
                else -> {
                    StateMachine.Loading
                }
            }
        }

    }

    override fun getTotalRevenue(
        endDate: String,
        merchantId: String,
        startDate: String
    ) = flow<StateMachine<TotalRevenueResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = transactionsDataSource.getTotalRevenue(endDate, merchantId, startDate)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

}