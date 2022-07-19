package com.wayapaychat.wayapos.external.transactions

import com.wayapaychat.wayapos.models.Response
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface TerminalTransactionApi {

    @POST("api/v1/transactions/viewAllMerchantTerminalTransactions")
    fun getTerminalTransactions(@Query("merchantId") merchantID : String): Call<Any>


}