package com.wayapaychat.wayapay.framework.repo.subscription

import com.wayapaychat.wayapay.framework.network.model.APICreatePlanRequest
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionResponse
import com.wayapaychat.wayapay.framework.network.model.APIPlansResponse
import com.wayapaychat.wayapay.framework.network.model.APISubscriptionsResponse
import com.wayapaychat.wayapay.framework.repo.BaseRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepo : BaseRepo {

    fun createPlan(body: APICreatePlanRequest): Flow<StateMachine<APICreatePlanResponse>>
    fun plans(): Flow<StateMachine<APIPlansResponse>>
    fun plan(planId: String): Flow<StateMachine<APICreatePlanResponse>>
    fun createSubscription(body: APICreateSubscriptionRequest): Flow<StateMachine<APICreateSubscriptionResponse>>
    fun subscriptions(): Flow<StateMachine<APISubscriptionsResponse>>
    fun subscription(subscriptionId: String): Flow<StateMachine<APICreateSubscriptionResponse>>
}
