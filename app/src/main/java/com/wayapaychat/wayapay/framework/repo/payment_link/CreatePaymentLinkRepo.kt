package com.wayapaychat.wayapay.framework.repo.payment_link

import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLinkResponse
import com.wayapaychat.wayapay.framework.network.model.APIPaymentLinks
import com.wayapaychat.wayapay.framework.repo.BaseRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow

interface CreatePaymentLinkRepo : BaseRepo {
    fun createPaymentLinkRepo(body: APICreatePaymentLink): Flow<StateMachine<APICreatePaymentLinkResponse>>
    fun paymentLinks(endDate : String?,status : String?,startDate : String?): Flow<StateMachine<APIPaymentLinks>>
}
