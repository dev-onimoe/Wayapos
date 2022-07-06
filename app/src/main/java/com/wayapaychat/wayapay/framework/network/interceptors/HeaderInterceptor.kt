package com.wayapaychat.wayapay.framework.network.interceptors

import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val cache: Cache) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authToken = cache.getString(CacheConstants.Keys.TOKEN)
        val requestBuilder =
            originalRequest.newBuilder().header(Headers.AUTHORIZATION, authToken)
        val newRequest = requestBuilder.build()

        return chain.proceed(newRequest)
    }
}
