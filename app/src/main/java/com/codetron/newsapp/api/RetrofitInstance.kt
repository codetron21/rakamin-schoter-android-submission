package com.codetron.newsapp.api

import com.codetron.newsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private var INSTANCES = setOf<Retrofit>()

    private fun logging() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private fun client() = OkHttpClient.Builder()
        .addInterceptor(logging())
        .connectTimeout(2000, TimeUnit.MILLISECONDS)
        .readTimeout(2000, TimeUnit.MILLISECONDS)
        .build()

    private fun getRetrofit(url: String) = Retrofit.Builder()
        .baseUrl(url)
        .client(client())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    fun get(baseUrl: String): Retrofit {
        return INSTANCES.findLast {
            it.baseUrl().equals(baseUrl)
        } ?: kotlin.run {
            val instance = getRetrofit(baseUrl)
            INSTANCES.plus(instance)
            instance
        }
    }
}