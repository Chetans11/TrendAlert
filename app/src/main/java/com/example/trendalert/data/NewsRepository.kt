package com.example.trendalert.data

import com.example.trendalert.components.Article
import com.example.trendalert.components.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepository {
    private val apiService = RetrofitClient.newsApiService
    private var currentLanguage = "en"
    private val cachedArticles = mutableMapOf<String, Article>()

    fun setLanguage(languageCode: String) {
        currentLanguage = languageCode
    }

    private fun mapToArticle(response: ArticleResponse): Article {
        return Article(
            title = response.title,
            description = response.description ?: "",
            url = response.link,
            urlToImage = response.image_url,
            category = response.category?.firstOrNull() ?: "",
            source = Source(
                id = response.source_id,
                name = response.source_id.replaceFirstChar { it.uppercase() }
            ),
            publishedAt = response.pubDate,
            content = response.content ?: ""
        )
    }

    suspend fun getTopNews(): List<Article> {
        return try {
            val response = apiService.getNews(
                category = null,
                country = "in",
                language = currentLanguage
            )
            val articles = response.results?.map { mapToArticle(it) } ?: emptyList()
            // Cache the articles
            articles.forEach { article ->
                cachedArticles[article.url] = article
            }
            articles
        } catch (e: Exception) {
            println("Error fetching top news: ${e.message}")
            emptyList()
        }
    }

    suspend fun getNewsByCategory(category: String): List<Article> {
        return try {
            val response = apiService.getNews(
                category = category,
                country = "in",
                language = currentLanguage
            )
            // Filter out any articles that don't match the category
            val articles = response.results
                ?.filter { article -> 
                    article.category?.any { it.equals(category, ignoreCase = true) } == true
                }
                ?.map { mapToArticle(it) }
                ?: emptyList()
            
            // Cache the articles
            articles.forEach { article ->
                cachedArticles[article.url] = article
            }
            articles
        } catch (e: Exception) {
            println("Error fetching news by category: ${e.message}")
            emptyList()
        }
    }

    fun getArticleByUrl(url: String): Article? {
        return cachedArticles[url]
    }
} 