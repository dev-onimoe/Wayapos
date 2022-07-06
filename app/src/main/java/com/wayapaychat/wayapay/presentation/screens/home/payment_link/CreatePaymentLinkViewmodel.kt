package com.wayapaychat.wayapay.presentation.screens.home.payment_link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLinkResponse
import com.wayapaychat.wayapay.framework.network.model.APIPaymentLinks
import com.wayapaychat.wayapay.framework.repo.payment_link.CreatePaymentLinkRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreatePaymentLinkViewmodel @Inject constructor(
    private val createPaymentLinkRepo: CreatePaymentLinkRepo
) : ViewModel() {

    private val _createPaymentLinkObserver =
        MutableLiveData<StateMachine<APICreatePaymentLinkResponse>>()
    val createPaymentLinkObserver: LiveData<StateMachine<APICreatePaymentLinkResponse>> =
        _createPaymentLinkObserver

    private val _paymentLinkObserver =
        MutableLiveData<StateMachine<APIPaymentLinks>>()
    val paymentLinkObserver: LiveData<StateMachine<APIPaymentLinks>> =
        _paymentLinkObserver

    fun setStatEvents(events: CreatePaymentEvents, body: Any? = null, endDate: String = "", status : String = "", startDate : String = "") {
        when (events) {
            is CreatePaymentEvents.CreatePaymentLink -> {
                body?.let {
                    val requestBody = body as APICreatePaymentLink
                    createPaymentLink(requestBody)
                }
            }
            is CreatePaymentEvents.PaymentLinks -> {
                getPaymentLinks(endDate, status, startDate)
            }
        }
    }

    private fun createPaymentLink(requestBody: APICreatePaymentLink) {
        createPaymentLinkRepo.createPaymentLinkRepo(requestBody).onEach {
            _createPaymentLinkObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun getPaymentLinks(endDate : String?,status : String?,startDate : String?) {
        createPaymentLinkRepo.paymentLinks(endDate, status, startDate).onEach {
            _paymentLinkObserver.value = it
        }.launchIn(viewModelScope)
    }
}

sealed class CreatePaymentEvents {
    object CreatePaymentLink : CreatePaymentEvents()
    object PaymentLinks : CreatePaymentEvents()
}
