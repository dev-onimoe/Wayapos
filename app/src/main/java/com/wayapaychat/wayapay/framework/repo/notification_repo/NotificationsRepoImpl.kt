package com.wayapaychat.wayapay.framework.repo.notification_repo

import com.wayapaychat.wayapay.framework.datasource.remote.dispute.DisputeDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.notifications.NotificationsDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.transactions.TransactionsDataSource
import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.NotificationsResponse
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationsRepoImpl(val dataSource: NotificationsDataSource) : NotificationRepo {

    override fun getAllNotifications(id : String) = flow<StateMachine<NotificationsResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = dataSource.getAllNotifications(id)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }
}