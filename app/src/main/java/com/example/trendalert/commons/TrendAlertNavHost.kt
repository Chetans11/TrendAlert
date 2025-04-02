package com.example.trendalert.commons

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trendalert.features.newsdetail.NewsDetailScreen
import com.example.trendalert.features.newslist.NewsListScreen
import com.example.trendalert.features.newslist.NewsViewModel
import com.example.trendalert.features.newsdetail.NewsDetailViewModel

@Composable
fun TrendAlertNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "news_list") {
        composable("news_list") {
            val newsViewModel: NewsViewModel = viewModel()

            NewsListScreen(
                viewModel = newsViewModel,
                onArticleClick = { article ->
                    navController.navigate("news_detail/${article.article_id}") // ✅ Updated
                }
            )
        }
        composable(
            route = "news_detail/{articleId}",
            arguments = listOf(navArgument("articleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")

            val newsDetailViewModel: NewsDetailViewModel = viewModel()
            articleId?.let {
                newsDetailViewModel.fetchArticleDetails(it)
            }

            NewsDetailScreen(articleId = articleId, viewModel = newsDetailViewModel)
        }
    }
}
