package com.example.trendalert.features.newslist

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trendalert.data.repository.NewsRepository
import com.example.trendalert.features.shared.Article
import com.example.trendalert.ui.theme.TrendAlertTheme

@Composable
fun NewsListScreen(viewModel: NewsViewModel, onArticleClick: (Article) -> Unit) {
    val articles by viewModel.articles.observeAsState(emptyList())
    val errorMessage by viewModel.error.observeAsState()

    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val filteredArticles = if (searchQuery.isEmpty()) {
        articles
    } else {
        articles.filter { article ->
            article.title.contains(searchQuery, ignoreCase = true) ||
                    (article.description?.contains(searchQuery, ignoreCase = true) == true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "TrendAlert",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                if (!isSearchVisible) {
                    IconButton(onClick = { isSearchVisible = true }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                } else {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(text = "Search...", color = Color.Gray)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 4.dp)
                            .height(56.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isSearchVisible = false
                                searchQuery = ""
                            }
                        )
                    )
                }
            }
            val categories = listOf(
                "Top", "Business", "Crime", "Domestic", "Education", "Entertainment",
                "Environmental", "Lifestyle", "Other", "Politics", "Science", "Sports",
                "Technology", "Tourism", "World"
            )

            Row(modifier = Modifier.horizontalScroll(scrollState)) {
                categories.forEach { category ->
                    Button(
                        onClick = { viewModel.setCategory(category.lowercase()) },
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = category)
                    }
                }
            }

            val error = errorMessage
            if (error != null) {
                Text(text = error, color = Color.Red)
            } else if (filteredArticles.isEmpty() && searchQuery.isNotEmpty()) {
                Text(text = "No results found", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(filteredArticles) { article ->
                        NewsCard(article = article, onClick = { onArticleClick(article) })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsListScreenPreview() {
    TrendAlertTheme {
        NewsListScreen(viewModel = NewsViewModel(NewsRepository()), onArticleClick = {})
    }
}
