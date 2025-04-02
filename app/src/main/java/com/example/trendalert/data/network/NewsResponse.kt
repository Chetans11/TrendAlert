package com.example.trendalert.data.network


import com.example.trendalert.features.shared.Article

data class NewsResponse(
    val status: String,  // "success" or "error"
    val totalResults: Int,
    val results: List<Article>,
    val nextPage: String? // For pagination
)
