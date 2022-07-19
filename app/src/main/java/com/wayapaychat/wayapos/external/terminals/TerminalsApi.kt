package com.wayapaychat.wayapos.external.terminals

import com.wayapaychat.wayapos.models.ApiBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TerminalsApi {

    @POST
    fun getMerchantTerminals(@Body body : ApiBody) : Call<Any>

}