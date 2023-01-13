package com.geekstudio.composetest.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * Retrofit Service 빌더
 */
object ApiBuilder {
    private val client = getOkHttpInterceptor()

    fun <T>build(url:String, clazz:Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(client)
            .build()
            .create(clazz)
    }

    private fun getOkHttpInterceptor():OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}