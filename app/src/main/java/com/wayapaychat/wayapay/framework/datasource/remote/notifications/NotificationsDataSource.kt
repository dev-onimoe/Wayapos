package com.wayapaychat.wayapay.framework.datasource.remote.notifications

import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.NotificationsResponse
import com.wayapaychat.wayapay.framework.network.model.RevenueStats

interface NotificationsDataSource {

    suspend fun getAllNotifications(id : String): NotificationsResponse

}