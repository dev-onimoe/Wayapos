package com.wayapaychat.wayapay.framework.repo.customers

import com.wayapaychat.wayapay.framework.network.model.CustomerToggleResponse
import com.wayapaychat.wayapay.framework.network.model.CustomerContent
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomersResponse
import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponse
import com.wayapaychat.wayapay.framework.repo.BaseRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow

interface CustomersRepo : BaseRepo {
    fun createCustomer(body: APICreateCustomerRequest): Flow<StateMachine<APICreateCustomerResponse>>
    fun customer(customerId: String): Flow<StateMachine<APICustomerResponse>>

    fun customerTransactions(customerId: String): Flow<StateMachine<CustomerTransactionApiResponse>>

    suspend fun customers(): StateMachine<APICustomersResponse>

    fun toggleCustomers(
        customerId: String,
        avoid: Boolean
    ): Flow<StateMachine<CustomerToggleResponse>>

    fun getLocalCustomer(status: String, search: String): Flow<StateMachine<List<CustomerContent>?>>
}
