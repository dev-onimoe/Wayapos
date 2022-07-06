package com.wayapaychat.wayapay.framework.repo.dispute_repo

import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.RejectChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import com.wayapaychat.wayapay.framework.repo.BaseRepo
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface DisputeRepo : BaseRepo {
    fun getAllDispute(): Flow<StateMachine<AllDisputeResponse>>
    fun acceptChargeBack(
        dispute_id: String,
        chargeBack: AcceptChargeBackRequest
    ): Flow<StateMachine<AllDisputeResponse>>

    fun rejectChargeBack(
        dispute_id: String,
        merchantRejectionDocumentType: MultipartBody.Part,
        disputeRejectReason: MultipartBody.Part,
        files: MultipartBody.Part
    ): Flow<StateMachine<RejectChargeBackRequest>>
}