package com.wayapaychat.wayapay.framework.datasource.remote.customers

import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponse
import com.wayapaychat.wayapay.framework.network.model.CustomerToggleResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomersResponse

interface CustomersDataSource {
    suspend fun customers(): APICustomersResponse
    suspend fun createCustomer(body: APICreateCustomerRequest): APICreateCustomerResponse
    suspend fun customer(customerId: String): APICustomerResponse
    suspend fun customerTransactions(customerId: String): CustomerTransactionApiResponse
    suspend fun toggleCustomer(customerId: String, avoid : Boolean): CustomerToggleResponse
}
