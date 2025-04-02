package com.example.trendalert.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsdata.io/api/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build()
    }

    private val okhttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor("pub_72566741feecdcc5d083de560dc1ca142a679"))
            .build()
    }

    val api: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}
