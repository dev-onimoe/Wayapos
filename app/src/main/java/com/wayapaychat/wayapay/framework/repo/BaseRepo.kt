package com.wayapaychat.wayapay.framework.repo

import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.framework.state_machine.convertErrorBody
import kotlinx.coroutines.flow.FlowCollector
import java.net.SocketTimeoutException

interface BaseRepo {
    suspend fun <T> emitError(flow: FlowCollector<StateMachine<T>>, t: Throwable) {
        flow.run {
            when (t) {
                is SocketTimeoutException -> emit(StateMachine.TimeOut(t))
                is retrofit2.HttpException -> {
                    val status = t.code()
                    val res = convertErrorBody(t)
                    emit(StateMachine.GenericError(status, res))
                }
                is Exception -> emit(StateMachine.Error(t))
            }
        }
    }
}
