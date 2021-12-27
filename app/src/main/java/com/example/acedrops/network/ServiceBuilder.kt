package com.example.acedrops.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    const val BASEURL = "https://acedrops.herokuapp.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor { chain ->
            val request =
                chain.request().newBuilder().header("Content-Type", "application/json").build()
            chain.proceed(request)
        }.build())
        .build()

    fun buildService(): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}
