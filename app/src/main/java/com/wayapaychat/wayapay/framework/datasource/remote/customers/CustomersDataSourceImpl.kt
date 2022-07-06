package com.wayapaychat.wayapay.framework.datasource.remote.customers

import com.wayapaychat.wayapay.framework.network.WayapayApiService
import com.wayapaychat.wayapay.framework.network.WayapayTransactionApiService
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerRequest
import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponse

class CustomersDataSourceImpl(private val wayapayApiService: WayapayApiService, val paymentApi : WayapayTransactionApiService) :
    CustomersDataSource {
    override suspend fun customers() = wayapayApiService.customers()

    override suspend fun createCustomer(body: APICreateCustomerRequest) =
        wayapayApiService.createCustomer(body)

    override suspend fun customer(customerId: String) = wayapayApiService.customer(customerId)
    override suspend fun customerTransactions(customerId: String): CustomerTransactionApiResponse {
       return paymentApi.customerTransactions(customerId)
    }

    override suspend fun toggleCustomer(customerId: String, avoid:Boolean) = wayapayApiService.toggleCustomer(customerId = customerId, avoid = avoid)
}
