package com.example.trendalert.features.newsdetail

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.trendalert.features.shared.Article
import kotlinx.coroutines.launch

class NewsDetailViewModel(articleId: String?) : ViewModel() {

    private val _article = mutableStateOf<Article?>(null)
    val article: State<Article?> = _article

    init {
        articleId?.let {
            fetchArticleDetails(it)
        }
    }

    fun fetchArticleDetails(articleId: String) {
        viewModelScope.launch {
            _article.value = Article(
                article_id = articleId, // ✅ Updated field
                title = "Sample Article Title",
                description = "Sample description for the article.",
                link = "https://example.com/sample-article", // ✅ Updated field
                source_id = "TechNews", // ✅ Updated field (Replaces author)
                source_url = "https://technews.com", // ✅ New field
                source_icon = "https://technews.com/logo.png", // ✅ New field
                image_url = "https://example.com/sample-image.jpg", // ✅ Updated field
                category = listOf("Technology", "AI"),
                pubDate = "2025-02-19", // ✅ Updated field
                country = listOf("US"), // ✅ New field
                language = "en" // ✅ New field
            )
        }
    }
}
