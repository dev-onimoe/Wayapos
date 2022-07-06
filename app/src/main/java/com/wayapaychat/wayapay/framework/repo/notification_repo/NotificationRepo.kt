package com.wayapaychat.wayapay.framework.repo.notification_repo

import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLink
import com.wayapaychat.wayapay.framework.network.model.APICreatePaymentLinkResponse
import com.wayapaychat.wayapay.framework.network.model.APIPaymentLinks
import com.wayapaychat.wayapay.framework.network.model.NotificationsResponse
import com.wayapaychat.wayapay.framework.repo.BaseRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow

interface NotificationRepo : BaseRepo {
    fun getAllNotifications(id : String): Flow<StateMachine<NotificationsResponse>>
}
