package com.wayapaychat.wayapay.framework.datasource.remote.create_payment_link

import com.wayapaychat.wayapay.framework.network.WayapayApiService
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink

class CreatePaymentLinkDatasourceImpl(val apiService: WayapayApiService) :
    CreatePaymentLinkDatasource {
    override suspend fun createPaymentLink(body: APICreatePaymentLink) =
        apiService.createPaymentLink(body)

    override suspend fun paymentLinks(endDate: String?, status: String?, startDate: String?) =
        apiService.paymentLinks(endDate, status, startDate)
}
