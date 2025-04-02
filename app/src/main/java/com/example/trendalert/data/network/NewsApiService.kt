package com.example.trendalert.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("latest")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("q") query: String? = null,  // ✅ Now optional (null by default)
        @Query("country") country: String = "in,us",  // ✅ Fetches Indian & US news by default
        @Query("category") category: String? = null,  // ✅ Optional category filter
        @Query("language") language: String = "en"  // ✅ Defaults to English
    ): Response<NewsResponse>
}
