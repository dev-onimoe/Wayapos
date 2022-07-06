package com.wayapaychat.wayapay.framework.repo.payment_link

import com.wayapaychat.wayapay.framework.datasource.remote.create_payment_link.CreatePaymentLinkDatasource
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLinkResponse
import com.wayapaychat.wayapay.framework.network.model.APIPaymentLinks
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.flow

class CreatePaymentLinkRepoImpl(private val createPaymentLinkDatasource: CreatePaymentLinkDatasource) :
    CreatePaymentLinkRepo {
    override fun createPaymentLinkRepo(body: APICreatePaymentLink) =
        flow<StateMachine<APICreatePaymentLinkResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = createPaymentLinkDatasource.createPaymentLink(body)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun paymentLinks(endDate : String?,status : String?,startDate : String?) = flow<StateMachine<APIPaymentLinks>> {
        emit(StateMachine.Loading)
        try {
            val response = createPaymentLinkDatasource.paymentLinks(endDate, status, startDate)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }
}
