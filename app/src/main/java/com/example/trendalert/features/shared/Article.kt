package com.example.trendalert.features.shared

data class Article(
    val article_id: String,  // Unique ID for each news
    val title: String,  // News title
    val link: String,  // URL to full article
    val source_id: String,  // Name of the news source
    val source_url: String,  // URL of the source
    val source_icon: String?,  // Logo of the source
    val image_url: String?,  // Image associated with the news
    val description: String?,  // Short description
    val pubDate: String,  // Published date
    val category: List<String>,  // List of categories
    val country: List<String>,  // List of countries
    val language: String  // Language of the article
)

