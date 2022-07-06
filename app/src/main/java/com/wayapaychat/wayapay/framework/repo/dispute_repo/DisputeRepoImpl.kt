package com.wayapaychat.wayapay.framework.repo.dispute_repo

import com.wayapaychat.wayapay.framework.datasource.remote.dispute.DisputeDataSource
import com.wayapaychat.wayapay.framework.datasource.remote.transactions.TransactionsDataSource
import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.RejectChargeBackRequest
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class DisputeRepoImpl(val dataSource: DisputeDataSource) : DisputeRepo {

    override fun getAllDispute() = flow<StateMachine<AllDisputeResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = dataSource.getAllDisputes()
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun acceptChargeBack(
        dispute_id: String,
        chargeBack: AcceptChargeBackRequest
    ) = flow<StateMachine<AllDisputeResponse>> {
        emit(StateMachine.Loading)
        try {
            val response = dataSource.acceptChargeBack(dispute_id, chargeBack)
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }

    override fun rejectChargeBack(
        dispute_id: String,
        merchantRejectionDocumentType: MultipartBody.Part,
        disputeRejectReason: MultipartBody.Part,
        files: MultipartBody.Part
    ) = flow<StateMachine<RejectChargeBackRequest>> {
        try {
            val response = dataSource.rejectChargeBack(
                dispute_id,
                merchantRejectionDocumentType,
                disputeRejectReason,
                files
            )
            emit(StateMachine.Success(response))
        } catch (t: Throwable) {
            emitError(this, t)
        }
    }


}