package com.wayapaychat.wayapay.framework.datasource.remote.dispute

import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.RejectChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.RevenueStats
import okhttp3.MultipartBody

interface DisputeDataSource {

    suspend fun getAllDisputes(): AllDisputeResponse
    suspend fun acceptChargeBack(dispute_id : String , chargeBack : AcceptChargeBackRequest): AllDisputeResponse
    suspend fun rejectChargeBack(
        dispute_id: String,
        merchantRejectionDocumentType: MultipartBody.Part,
        disputeRejectReason: MultipartBody.Part,
        files: MultipartBody.Part): RejectChargeBackRequest

}