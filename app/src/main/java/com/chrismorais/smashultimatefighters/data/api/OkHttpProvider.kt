package com.chrismorais.smashultimatefighters.data.api

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpProvider {
    private var okHttpClient: OkHttpClient? = null
    private var interceptador = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    fun getOkHttpClient(context: Context? = null): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptador)
            .cache(getCache(context))
            .build()

        this.okHttpClient = okHttpClient

        return okHttpClient
    }

    private fun getCache(context: Context?): Cache? {
        return if (context != null) {
            val cacheSize = (5 * 1024 * 1024).toLong()
            Cache(context.cacheDir, cacheSize)
        } else {
            null
        }
    }
}