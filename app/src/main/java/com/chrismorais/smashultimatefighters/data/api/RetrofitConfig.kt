package com.chrismorais.smashultimatefighters.data.api

import android.content.Context
import com.chrismorais.smashultimatefighters.BuildConfig
import com.chrismorais.smashultimatefighters.data.api.OkHttpProvider.getOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig(private val context: Context) {

    private fun getRetroInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(getOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): Service = getRetroInstance().create(Service::class.java)
}