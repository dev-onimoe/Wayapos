package com.wayapaychat.wayapos.external.UserMerchant

import retrofit2.Call
import retrofit2.http.GET

interface UserMerchant {

    @GET("api/v1/agent/getallmerchants")
    fun getMerchants() : Call<Any>
}