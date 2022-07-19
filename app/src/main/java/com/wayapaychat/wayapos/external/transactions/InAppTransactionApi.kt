package com.wayapaychat.wayapos.external.transactions

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InAppTransactionApi {

    @GET("api/v1/wallet/accounts/{user_id}")
    fun getUserWallets(@Path("user_id") userId : Int): Call<Any>

    @GET("api/v1/wallet/find/transactions/{accountNo}")
    fun getInAppTransactions(@Path("accountNo") accountNo : String, @Query("page") page : Int, @Query("size") size : Int): Call<Any>
}