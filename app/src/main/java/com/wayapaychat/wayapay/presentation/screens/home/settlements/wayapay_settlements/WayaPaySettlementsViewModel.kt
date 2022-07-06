package com.wayapaychat.wayapay.presentation.screens.home.settlements.wayapay_settlements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse
import com.wayapaychat.wayapay.framework.repo.transactions_repo.TransactionsRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WayaPaySettlementsViewModel
@Inject
constructor(val transactionRepository: TransactionsRepo) : ViewModel() {

    private val _allSettlementsObserver =
        MutableLiveData<StateMachine<List<SettleContent>?>>()
    val allSettlementsObserver: LiveData<StateMachine<List<SettleContent>?>> =
        _allSettlementsObserver


    fun setStateEvents(
        stateEvents: SettlementsStateEvents,
        requestBody: Any?,
        status: String,
        channel: String,
        date: String,
        search: String
    ) {
        when (stateEvents) {
            is SettlementsStateEvents.GetAllSettlements -> {
                getAllSettlements(status,channel,date,search)
            }
        }
    }

    private fun getAllSettlements(status: String,channel: String, date : String,search : String) {
        transactionRepository.getAllLocalSettlements("%$status%", "%$channel%","%$date%","%$search%").onEach {
            _allSettlementsObserver.value = it
        }.launchIn(viewModelScope)
    }


    sealed class SettlementsStateEvents {
        object GetAllSettlements : SettlementsStateEvents()
    }

}