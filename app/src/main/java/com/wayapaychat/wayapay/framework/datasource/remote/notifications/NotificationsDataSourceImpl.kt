package com.wayapaychat.wayapay.framework.datasource.remote.notifications

import com.wayapaychat.wayapay.framework.network.DisputeApiService
import com.wayapaychat.wayapay.framework.network.NotificationsApiService
import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.NotificationsResponse

class NotificationsDataSourceImpl(private val apiService: NotificationsApiService) : NotificationsDataSource {
    override suspend fun getAllNotifications(id: String): NotificationsResponse {
        return apiService.getAllNotifications(id = id)
    }
}