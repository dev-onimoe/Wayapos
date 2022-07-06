package com.wayapaychat.wayapay.framework.network.factory

import com.google.gson.Gson
import com.ikonik.remote.interceptor.NoInternetInterceptor
import com.squareup.moshi.Moshi
import com.wayapaychat.wayapay.framework.network.interceptors.HeaderInterceptor
import com.wayapaychat.wayapay.framework.network.interceptors.HttpsInterceptor
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitApiFactory constructor(
    private val moshi: Moshi,
    private val gson: Gson,
    private val cache: Cache
) :
    ApiFactory {

    override fun createRetrofit(url: String, isDebug: Boolean): Retrofit {
        val client: OkHttpClient = makeOkHttpClient(
            makeLoggingInterceptor((isDebug))
        )
        return Retrofit.Builder()
            .baseUrl(url)
            .delegatingCallFactory { client }
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun makeOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,

    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(cache))
            .addInterceptor(HttpsInterceptor)
            .addInterceptor(NoInternetInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Retrofit.Builder.delegatingCallFactory(
        delegate: dagger.Lazy<OkHttpClient>
    ) : Retrofit.Builder = callFactory {
        delegate.get().newCall(it)
    }
}
