package com.example.trendalert.features.newslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendalert.data.repository.NewsRepository
import com.example.trendalert.features.shared.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var category: String? = null  // ✅ Default is null (fetch all categories)

    fun setCategory(newCategory: String?) {
        Log.d("NewsViewModel", "setCategory() called with: $newCategory")

        category = if (newCategory?.lowercase() == "all") null else newCategory

        Log.d("NewsViewModel", "Final category value after processing: ${category ?: "All"}")

        fetchNews()
    }

    fun fetchNews() {
        val apiKey = "pub_72566741feecdcc5d083de560dc1ca142a679"
        val country = "in,us"

        Log.d("NewsViewModel", "Fetching news for category: ${category ?: "All"}")

        val requestUrl = "https://newsdata.io/api/1/news?apikey=$apiKey&country=$country${category?.let { "&category=$it" } ?: ""}"
        Log.d("NewsViewModel", "API Request URL: $requestUrl")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.fetchNews(apiKey, category = category, country = country)

                withContext(Dispatchers.Main) {
                    if (response != null) {
                        Log.d("NewsViewModel", "Raw API response: $response")

                        if (response.results.isNotEmpty()) {
                            _articles.value = response.results
                            Log.d("NewsViewModel", "Successfully fetched ${response.results.size} articles")
                        } else {
                            _error.value = "No news available for category: ${category ?: "All"}"
                            Log.w("NewsViewModel", "No articles found in API response")
                        }
                    } else {
                        _error.value = "Failed to fetch news. API returned null."
                        Log.e("NewsViewModel", "API returned null response")
                    }
                }
            } catch (e: Exception) {
                val exceptionMsg = "Exception: ${e.localizedMessage ?: "Unknown error"}"
                withContext(Dispatchers.Main) {
                    _error.value = exceptionMsg
                }
                Log.e("NewsViewModel", exceptionMsg)
            }
        }
    }
}
