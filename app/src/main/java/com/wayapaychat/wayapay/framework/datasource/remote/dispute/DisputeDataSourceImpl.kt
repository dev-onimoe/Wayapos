package com.wayapaychat.wayapay.framework.datasource.remote.dispute

import com.wayapaychat.wayapay.framework.network.DisputeApiService
import com.wayapaychat.wayapay.framework.network.model.AcceptChargeBackRequest
import com.wayapaychat.wayapay.framework.network.model.AllDisputeResponse
import com.wayapaychat.wayapay.framework.network.model.RejectChargeBackRequest
import okhttp3.MultipartBody

class DisputeDataSourceImpl(private val apiService: DisputeApiService) : DisputeDataSource {
    override suspend fun getAllDisputes(): AllDisputeResponse {
        return apiService.getAllDispute()
    }

    override suspend fun acceptChargeBack(
        dispute_id: String,
        chargeBack: AcceptChargeBackRequest
    ): AllDisputeResponse {
        return apiService.acceptChargeBack(dispute_id, chargeBack)
    }

    override suspend fun rejectChargeBack(
        dispute_id: String,
        merchantRejectionDocumentType: MultipartBody.Part,
        disputeRejectReason: MultipartBody.Part,
        files: MultipartBody.Part
    ): RejectChargeBackRequest {
        return apiService.rejectChargeBack(dispute_id,merchantRejectionDocumentType, disputeRejectReason, files)
    }
}