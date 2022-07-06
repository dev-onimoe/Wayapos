package com.wayapaychat.wayapay.presentation.screens.home.dispute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.RejectChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.network.model.SettleContent
import com.wayapaychat.wayapay.framework.network.model.TransactionData
import com.wayapaychat.wayapay.framework.network.model.TransactionsAPIResponse
import com.wayapaychat.wayapay.framework.repo.dispute_repo.DisputeRepo
import com.wayapaychat.wayapay.framework.repo.transactions_repo.TransactionsRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import okhttp3.MultipartBody

@HiltViewModel
class WayaPayDisputeViewModel
@Inject
constructor(val repo: DisputeRepo) : ViewModel() {

    private val _allDisputeObserver =
        MutableLiveData<StateMachine<AllDisputeResponse>>()
    val allDisputeObserver: LiveData<StateMachine<AllDisputeResponse>> =
        _allDisputeObserver

    private val _acceptChargeBackObserver =
        MutableLiveData<StateMachine<AllDisputeResponse>>()
    val acceptChargeBackObserver: LiveData<StateMachine<AllDisputeResponse>> =
        _acceptChargeBackObserver

    private val _rejectChargeBackObserver =
        MutableLiveData<StateMachine<RejectChargeBackRequest>>()
    val rejectChargeBackObserver: LiveData<StateMachine<RejectChargeBackRequest>> =
        _rejectChargeBackObserver


    init {
        getAllDispute()
    }

    fun setStateEvents(
        stateEvents: DisputesStateEvents,
        requestBody: Any?
    ) {
        when (stateEvents) {
            is DisputesStateEvents.GetAllDisputes -> {
                getAllDispute()
            }
        }
    }

    private fun getAllDispute() {
        repo.getAllDispute().onEach {
            _allDisputeObserver.value = it
        }.launchIn(viewModelScope)
    }

    fun acceptChargeBack(dispute_id: String, chargeBack: AcceptChargeBackRequest) {
        repo.acceptChargeBack(dispute_id, chargeBack).onEach {
            _acceptChargeBackObserver.value = it
        }.launchIn(viewModelScope)
    }

    fun rejectChargeBack(
        dispute_id: String,
        merchantRejectionDocumentType: MultipartBody.Part,
        disputeRejectReason: MultipartBody.Part,
        files: MultipartBody.Part
    ) {
        repo.rejectChargeBack(dispute_id, merchantRejectionDocumentType, disputeRejectReason, files)
            .onEach {
                _rejectChargeBackObserver.value = it
            }.launchIn(viewModelScope)
    }


    sealed class DisputesStateEvents {
        object GetAllDisputes : DisputesStateEvents()
        object AcceptChargeBack : DisputesStateEvents()
    }

}