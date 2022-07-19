package com.wayapaychat.wayapos.external

import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        //lateinit var cache : Cache

        fun user(cache : Cache): APILoginResponse {
            val userDetails = cache.getObject(CacheConstants.Keys.USER_DETAILS, APILoginResponse::class.java) as APILoginResponse
            return userDetails
        }

        var BASE_URL = ""//Constants.TRANSACTION_SERVICE_BASE_URL
        fun getInstance(cache : Cache): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", user(cache).data?.token!!).build()
                chain.proceed(request)
            }.build()).build()
        }

    }
}