package com.wayapaychat.wayapay.presentation.screens.notification.wayapay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.datasource.remote.notifications.NotificationsDataSource
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.NotificationsResponse
import com.wayapaychat.wayapay.framework.repo.dispute_repo.DisputeRepo
import com.wayapaychat.wayapay.framework.repo.notification_repo.NotificationRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class WayapayNotificationViewModel
@Inject
constructor(
    val repo: NotificationRepo,
    private val cache: Cache
) : ViewModel() {

    private val _allDisputeObserver =
        MutableLiveData<StateMachine<NotificationsResponse>>()
    val allDisputeObserver: LiveData<StateMachine<NotificationsResponse>> =
        _allDisputeObserver


    init {
        val userId =
            cache.getInt(
                CacheConstants.Keys.USER_ID
            )

        getAllNotifications(userId.toString())

    }

    private fun getAllNotifications(id: String) {
        repo.getAllNotifications(id).onEach {
            _allDisputeObserver.value = it
        }.launchIn(viewModelScope)
    }


}