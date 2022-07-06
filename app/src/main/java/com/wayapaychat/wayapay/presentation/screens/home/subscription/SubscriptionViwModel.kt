package com.wayapaychat.wayapay.presentation.screens.home.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanRequest
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionResponse
import com.wayapaychat.wayapay.framework.network.model.APIPlansResponse
import com.wayapaychat.wayapay.framework.network.model.APISubscriptionsResponse
import com.wayapaychat.wayapay.framework.repo.subscription.SubscriptionRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SubscriptionViwModel @Inject constructor(
    private val subscriptionRepo: SubscriptionRepo
) : ViewModel() {

    private val _createPlanObserver = MutableLiveData<StateMachine<APICreatePlanResponse>>()
    val createPlanObserver: LiveData<StateMachine<APICreatePlanResponse>> = _createPlanObserver

    private val _planObserver = MutableLiveData<StateMachine<APICreatePlanResponse>>()
    val planObserver: LiveData<StateMachine<APICreatePlanResponse>> = _planObserver

    private val _plansObserver = MutableLiveData<StateMachine<APIPlansResponse>>()
    val plansObserver: LiveData<StateMachine<APIPlansResponse>> = _plansObserver

    private val _subscriptionsObserver = MutableLiveData<StateMachine<APISubscriptionsResponse>>()
    val subscriptionsObserver: LiveData<StateMachine<APISubscriptionsResponse>> =
        _subscriptionsObserver

    private val _subscriptionObserver =
        MutableLiveData<StateMachine<APICreateSubscriptionResponse>>()
    val subscriptionObserver: LiveData<StateMachine<APICreateSubscriptionResponse>> =
        _subscriptionObserver

    private val _createSubscriptionObserver =
        MutableLiveData<StateMachine<APICreateSubscriptionResponse>>()
    val createSubscriptionObserver: LiveData<StateMachine<APICreateSubscriptionResponse>> =
        _createSubscriptionObserver

    fun setStateEvent(stateEvents: SubscriptionStateEvents, body: Any? = null) {
        when (stateEvents) {
            SubscriptionStateEvents.CreatePlan -> {
                body?.let {

                    createPlan(body as APICreatePlanRequest)
                }
            }
            SubscriptionStateEvents.CreateSubscription -> {
                body?.let {

                    createSubscription(body as APICreateSubscriptionRequest)
                }
            }
            SubscriptionStateEvents.GetPlan -> {
                body?.let {
                    plan(body as String)
                }
            }

            SubscriptionStateEvents.GetSubscription -> {
                body?.let {
                    subscription(body as String)
                }
            }

            SubscriptionStateEvents.GetPlans -> plans()
            SubscriptionStateEvents.GetSubscriptions -> subscriptions()
        }
    }

    private fun createPlan(requestBody: APICreatePlanRequest) {
        subscriptionRepo.createPlan(requestBody).onEach {
            _createPlanObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun createSubscription(requestBody: APICreateSubscriptionRequest) {
        subscriptionRepo.createSubscription(requestBody).onEach {
            _createSubscriptionObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun plans() {
        subscriptionRepo.plans().onEach {
            _plansObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun subscriptions() {
        subscriptionRepo.subscriptions().onEach {
            _subscriptionsObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun plan(planId: String) {
        subscriptionRepo.plan(planId).onEach {
            _planObserver.value = it
        }.launchIn(viewModelScope)
    }

    private fun subscription(subscriptionId: String) {
        subscriptionRepo.subscription(subscriptionId).onEach {
            _subscriptionObserver.value = it
        }.launchIn(viewModelScope)
    }
}

sealed class SubscriptionStateEvents() {
    object GetPlans : SubscriptionStateEvents()
    object GetPlan : SubscriptionStateEvents()
    object CreatePlan : SubscriptionStateEvents()

    object GetSubscriptions : SubscriptionStateEvents()
    object CreateSubscription : SubscriptionStateEvents()
    object GetSubscription : SubscriptionStateEvents()
}
