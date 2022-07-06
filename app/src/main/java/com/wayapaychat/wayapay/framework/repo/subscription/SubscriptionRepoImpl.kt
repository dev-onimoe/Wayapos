package com.wayapaychat.wayapay.framework.repo.subscription

import com.wayapaychat.wayapay.framework.datasource.remote.subscription.SubscriptionDataSource
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanRequest
import com.wayapaychat.wayapay.framework.network.model.APICreatePlanResponse
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionRequest
import com.wayapaychat.wayapay.framework.network.model.APICreateSubscriptionResponse
import com.wayapaychat.wayapay.framework.network.model.APIPlansResponse
import com.wayapaychat.wayapay.framework.network.model.APISubscriptionsResponse
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.flow

class SubscriptionRepoImpl(private val subscriptionDataSource: SubscriptionDataSource) :
    SubscriptionRepo {
    override fun createPlan(body: APICreatePlanRequest) =
        flow<StateMachine<APICreatePlanResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = subscriptionDataSource.createPlan(body)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun plans() = flow<StateMachine<APIPlansResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = subscriptionDataSource.plans()
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun plan(planId: String) = flow<StateMachine<APICreatePlanResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = subscriptionDataSource.plan(planId)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun createSubscription(body: APICreateSubscriptionRequest) =
        flow<StateMachine<APICreateSubscriptionResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = subscriptionDataSource.createSubscription(body)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }

    override fun subscriptions() = flow<StateMachine<APISubscriptionsResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = subscriptionDataSource.subscriptions()
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun subscription(subscriptionId: String) =
        flow<StateMachine<APICreateSubscriptionResponse>> {
            emit(StateMachine.Loading)
            try {
                val response = subscriptionDataSource.subscription(subscriptionId)
                emit(StateMachine.Success(response))
            } catch (t: Throwable) {
                emitError(this, t)
            }
        }
}
