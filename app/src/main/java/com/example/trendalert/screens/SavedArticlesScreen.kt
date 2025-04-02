package com.example.trendalert.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trendalert.components.NewsCard
import com.example.trendalert.viewmodels.SavedArticlesViewModel
import com.example.trendalert.ui.theme.TrendAlertTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedArticlesScreen(
    navController: NavController,
    viewModel: SavedArticlesViewModel
) {
    val savedArticles = viewModel.savedArticles
    val trendAlertBlue = Color(0xFF0088CC)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = TrendAlertTheme.getBackgroundColor()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopAppBar(
                title = { Text("Saved Articles") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TrendAlertTheme.getSurfaceColor(),
                    titleContentColor = TrendAlertTheme.getTextColor(),
                    navigationIconContentColor = TrendAlertTheme.getTextColor()
                )
            )

            if (savedArticles.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved articles yet",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = TrendAlertTheme.getTextSecondaryColor()
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(TrendAlertTheme.getBackgroundColor())
                ) {
                    items(savedArticles) { article ->
                        NewsCard(
                            article = article,
                            onSaveClick = {
                                viewModel.removeArticle(article)
                            },
                            onShareClick = { /* Implement share functionality */ },
                            onArticleClick = { url -> navController.navigate("article/$url") }
                        )
                    }
                }
            }
        }
    }
} 