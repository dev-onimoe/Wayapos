package com.wayapaychat.wayapay.framework.datasource.remote.create_payment_link

import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLinkResponse
import com.wayapaychat.wayapay.framework.network.model.APIPaymentLinks

interface CreatePaymentLinkDatasource {
    suspend fun createPaymentLink(body: APICreatePaymentLink): APICreatePaymentLinkResponse
    suspend fun paymentLinks(endDate : String?,status : String?,startDate : String?): APIPaymentLinks
}
