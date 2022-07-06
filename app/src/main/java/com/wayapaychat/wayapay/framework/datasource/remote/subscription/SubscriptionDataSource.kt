package com.wayapaychat.wayapay.framework.datasource.remote.subscription

import com.wayapaychat.wayapay.framework.network.model.APICreatePlanRequest
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionResponse
import com.wayapaychat.wayapay.framework.network.model.APIPlansResponse
import com.wayapaychat.wayapay.framework.network.model.APISubscriptionsResponse

interface SubscriptionDataSource {

    suspend fun createPlan(body: APICreatePlanRequest): APICreatePlanResponse
    suspend fun plans(): APIPlansResponse
    suspend fun plan(planId: String): APICreatePlanResponse

    suspend fun createSubscription(body: APICreateSubscriptionRequest): APICreateSubscriptionResponse
    suspend fun subscriptions(): APISubscriptionsResponse
    suspend fun subscription(subscriptionId: String): APICreateSubscriptionResponse
}
