package com.example.trendalert.data

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("news")
    suspend fun getNews(
        @Query("apikey") apiKey: String = "pub_72566741feecdcc5d083de560dc1ca142a679",
        @Query("category") category: String? = null,
        @Query("country") country: String? = null,
        @Query("language") language: String = "en"
    ): NewsResponse
}

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val results: List<ArticleResponse>?
)

data class ArticleResponse(
    val title: String,
    val description: String?,
    val link: String,
    val image_url: String?,
    val pubDate: String,
    val source_id: String,
    val category: List<String>?,
    val content: String?
) 