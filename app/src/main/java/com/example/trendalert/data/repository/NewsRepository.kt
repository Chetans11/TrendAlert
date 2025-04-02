package com.example.trendalert.data.repository

import com.example.trendalert.data.network.NewsApiService
import com.example.trendalert.data.network.NewsResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NewsRepository {

    private val apiService: NewsApiService

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsdata.io/api/1/") // ✅ Base URL should match API docs
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(NewsApiService::class.java)
    }

    suspend fun fetchNews(
        apiKey: String,
        query: String? = null,
        country: String = "in,us",
        category: String? = null
    ): NewsResponse? {
        return try {
            val response = apiService.getNews(apiKey, query, country, category, language = "en")

            if (response.isSuccessful) {
                response.body()
            } else {
                println("API Error: ${response.code()} - ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            println("Network Error: ${e.localizedMessage ?: "Unknown error"}")
            null
        }
    }

}
