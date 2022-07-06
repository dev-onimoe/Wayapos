package com.wayapaychat.wayapay.presentation.screens.transaction.wayapay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.TotalRevenueResponse
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse
import com.wayapaychat.wayapay.framework.repo.transactions_repo.TransactionsRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WayaPayTransactionsViewModel
@Inject
constructor(val transactionRepository: TransactionsRepo) : ViewModel() {

    private val _allTransactionsObserver =
        MutableLiveData<StateMachine<List<TransactionData>?>>()
    val allTransactionsObserver: LiveData<StateMachine<List<TransactionData>?>> =
        _allTransactionsObserver

    private val _revenueStatsObserver =
        MutableLiveData<StateMachine<RevenueStats>>()
    val revenueStatsObserver: LiveData<StateMachine<RevenueStats>> =
        _revenueStatsObserver

    private val _getTotalRevenueObserver =
        MutableLiveData<StateMachine<TotalRevenueResponse>>()
    val getTotalRevenueObserver: LiveData<StateMachine<TotalRevenueResponse>> =
        _getTotalRevenueObserver

    init {
        getTotalRevenue("","","")
    }


    fun setStateEvents(
        stateEvents: TransactionsStateEvents,
        requestBody: Any?,
        status: String,
        channel: String,
        date: String,
        search: String
    ) {
        when (stateEvents) {
            is TransactionsStateEvents.GetAllTransactions -> {
                getAllTransactions(requestBody as String, status, channel,date,search)
            }
            is TransactionsStateEvents.GetRevenueStats -> {
                getRevenueStats()
            }
            is TransactionsStateEvents.GetTotalRevenue -> {
               // getTotalRevenue("","","")
            }
        }

    }

    private fun getRevenueStats() {
        transactionRepository.getRevenueStats().onEach {
            _revenueStatsObserver.value = it
        }.launchIn(viewModelScope)
    }


     fun getTotalRevenue(endDate : String, merchantId: String,startDate: String) {
        transactionRepository.getTotalRevenue(endDate, merchantId, startDate) .onEach {
            _getTotalRevenueObserver.value = it
        }.launchIn(viewModelScope)
    }


    private fun getAllTransactions(merchantId: String, status: String, channel: String, date : String,search : String) {
        transactionRepository.getAllTransactions(merchantId, "%$status%", "%$channel%","%$date%","%$search%").onEach {
            _allTransactionsObserver.value = it
        }.launchIn(viewModelScope)
    }

    sealed class TransactionsStateEvents {
        object GetAllTransactions : TransactionsStateEvents()
        object GetAllRemoteTransactions : TransactionsStateEvents()
        object GetRevenueStats : TransactionsStateEvents()
        object GetTotalRevenue : TransactionsStateEvents()
    }

}