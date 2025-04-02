package com.example.trendalert.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.trendalert.components.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SavedArticlesViewModel : ViewModel() {
    private val _savedArticles = mutableStateListOf<Article>()
    val savedArticles: List<Article> get() = _savedArticles

    fun addArticle(article: Article) {
        if (!_savedArticles.any { it.url == article.url }) {
            _savedArticles.add(article.copy(isBookmarked = true))
        }
    }

    fun removeArticle(article: Article) {
        _savedArticles.removeAll { it.url == article.url }
    }

    fun isArticleSaved(url: String): Boolean {
        return _savedArticles.any { it.url == url }
    }
} 