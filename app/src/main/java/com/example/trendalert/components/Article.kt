package com.example.trendalert.components

data class Article(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val category: String,
    val source: Source = Source("", "Unknown"),
    val publishedAt: String = "",
    val content: String = "",
    val isBookmarked: Boolean = false
)

data class Source(
    val id: String?,
    val name: String
) {
    companion object {
        fun fromString(sourceName: String): Source {
            return Source(null, sourceName)
        }
    }
} 