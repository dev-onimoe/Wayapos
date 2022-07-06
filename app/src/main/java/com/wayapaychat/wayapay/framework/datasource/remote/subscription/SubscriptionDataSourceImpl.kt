package com.wayapaychat.wayapay.framework.datasource.remote.subscription

import com.wayapaychat.wayapay.framework.network.WayapayApiService
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionRequest

class SubscriptionDataSourceImpl(private val apiService: WayapayApiService) :
    SubscriptionDataSource {
    override suspend fun createPlan(body: APICreatePlanRequest) = apiService.createPlan(body)

    override suspend fun plans() = apiService.plans()

    override suspend fun plan(planId: String) = apiService.plan(planId)

    override suspend fun createSubscription(body: APICreateSubscriptionRequest) =
        apiService.createSubscription(body)

    override suspend fun subscriptions() = apiService.subscriptions()

    override suspend fun subscription(subscriptionId: String) =
        apiService.subscription(subscriptionId)
}
