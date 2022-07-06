package com.wayapaychat.wayapay.presentation.screens.home.customers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.CustomerToggleResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerRequest
import com.wayapaychat.wayapay.framework.network.model.CustomerContent
import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponse
import com.wayapaychat.wayapay.framework.repo.customers.CustomersRepo
import com.wayapaychat.wayapay.framework.repo.transactions_repo.TransactionsRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreateCustomerViewModel @Inject constructor(
    private val customersRepo: CustomersRepo
) : ViewModel() {
    private val _createCustomersObserver =
        MutableLiveData<StateMachine<APICreateCustomerResponse>>()
    val createCustomersObserver: LiveData<StateMachine<APICreateCustomerResponse>> =
        _createCustomersObserver

    private val _customersObserver = MutableLiveData<StateMachine<List<CustomerContent>?>>()
    val customersObserver: LiveData<StateMachine<List<CustomerContent>?>> = _customersObserver

    private val _customerObserver = MutableLiveData<StateMachine<APICustomerResponse>>()
    val customerObserver: LiveData<StateMachine<APICustomerResponse>> = _customerObserver


    private val _avoidCustomerObserver = MutableLiveData<StateMachine<CustomerToggleResponse>>()
    val avoidCustomerObserver: LiveData<StateMachine<CustomerToggleResponse>> = _avoidCustomerObserver


    private val _customerTransactionsObserver = MutableLiveData<StateMachine<CustomerTransactionApiResponse>>()
    val customerTransactionsObserver: LiveData<StateMachine<CustomerTransactionApiResponse>> = _customerTransactionsObserver


    fun setStateEvents(
        customersEvents: CustomersEvents,
        requestBody: Any? = null,
        status: String,
        search: String,
        avoid: Boolean
    ) {
        when (customersEvents) {
            is CustomersEvents.CreateCustomer -> {
                val body = requestBody as APICreateCustomerRequest
                createCustomer(body)
            }
            is CustomersEvents.GetCustomers -> {
                customers(status, search)
            }
            is CustomersEvents.GetCustomerTransactions -> {
                customersTransactions(requestBody as String)
            }
            is CustomersEvents.ToggleCustomer -> {
                toggleCustomer(requestBody as String, avoid)
            }
            is CustomersEvents.GetCustomer -> {
                requestBody?.let {
                    customer(requestBody as String)
                }
            }
        }
    }

    private fun createCustomer(requestBody: APICreateCustomerRequest) {
        customersRepo.createCustomer(requestBody).onEach {
            _createCustomersObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun toggleCustomer(customerId: String,avoid : Boolean) {
        customersRepo.toggleCustomers(customerId,avoid).onEach {
            _avoidCustomerObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun customers(status: String, search: String) {
        customersRepo.getLocalCustomer("%$status%", "%$search%").onEach {
            _customersObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun customersTransactions(customerId: String) {
        customersRepo.customerTransactions(customerId).onEach {
            _customerTransactionsObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun customer(customerId: String) {
        customersRepo.customer(customerId).onEach {
            _customerObserver.value = it
        }.launchIn(viewModelScope)
    }
}

sealed class CustomersEvents() {
    object CreateCustomer : CustomersEvents()
    object GetCustomers : CustomersEvents()
    object GetCustomerTransactions : CustomersEvents()
    object GetCustomer : CustomersEvents()
    object ToggleCustomer : CustomersEvents()
}
