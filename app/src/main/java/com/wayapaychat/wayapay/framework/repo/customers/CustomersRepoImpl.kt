package com.wayapaychat.wayapay.framework.repo.customers

import android.util.Log
import com.wayapaychat.wayapay.framework.datasource.db.dao.customer_dao.CustomerDao
import com.wayapaychat.wayapay.framework.datasource.remote.customers.CustomersDataSource
import com.wayapaychat.wayapay.framework.network.model.CustomerToggleResponse
import com.wayapaychat.wayapay.framework.network.model.CustomerContent
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateCustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomerResponse
import com.wayapaychat.wayapay.framework.network.model.APICustomersResponse
import com.wayapaychat.wayapay.framework.network.model.CustomerTransactionApiResponse
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.framework.state_machine.convertErrorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException

class CustomersRepoImpl(
    private val customersDataSource: CustomersDataSource,
    val customersLocalDataSource: CustomerDao
) : CustomersRepo {
    override fun createCustomer(body: APICreateCustomerRequest) =
        flow<StateMachine<APICreateCustomerResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = customersDataSource.createCustomer(body)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun customer(customerId: String) = flow<StateMachine<APICustomerResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = customersDataSource.customer(customerId)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun customerTransactions(customerId: String) =
        flow<StateMachine<CustomerTransactionApiResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = customersDataSource.customerTransactions(customerId)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }

        }

    override suspend fun customers(): StateMachine<APICustomersResponse> {

        try {
            val remoteResponse = customersDataSource.customers()
            remoteResponse.data.content.map {
                customersLocalDataSource.saveCustomer(it)
            }
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

    override fun toggleCustomers(
        customerId: String,
        avoid: Boolean
    ) = flow<StateMachine<CustomerToggleResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = customersDataSource.toggleCustomer(customerId, avoid)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }


    }

    override fun getLocalCustomer(status: String, search: String) =
        flow<StateMachine<List<CustomerContent>?>> {
            emit(StateMachine.Loading)
            try {
                customers()
                val localresponse = customersLocalDataSource.getCustomers(status, search)
                emit(StateMachine.Success(localresponse))
            } catch (t: Throwable) {
                emitError(this, t)
            }


        }
}
