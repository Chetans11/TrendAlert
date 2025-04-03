package com.example.trendalert.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trendalert.screens.NewsListScreen
import com.example.trendalert.screens.ProfileScreen
import com.example.trendalert.screens.SavedArticlesScreen
import com.example.trendalert.screens.LanguagePreferencesScreen
import com.example.trendalert.screens.ArticleDetailScreen
import com.example.trendalert.auth.presentation.sign_in.SignInScreen
import com.example.trendalert.auth.presentation.sign_in.SignInState
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.example.trendalert.data.NewsRepository
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trendalert.auth.presentation.sign_in.GoogleAuthUiClient
import com.example.trendalert.viewmodels.SavedArticlesViewModel
import com.example.trendalert.components.Article
import android.content.Intent

sealed class Screen(val route: String) {
    object SignIn : Screen("sign_in")
    object NewsList : Screen("newsList")
    object Profile : Screen("profile")
    object SavedArticles : Screen("saved_articles")
    object LanguagePreferences : Screen("language_preferences")
    object ArticleDetail : Screen("article/{url}") {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "article/$encodedUrl"
        }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val context = LocalContext.current
    val newsRepository = remember { NewsRepository() }
    val savedArticlesViewModel = remember { SavedArticlesViewModel() }
    var signInState by remember { mutableStateOf(SignInState()) }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                state = signInState,
                onSignInClick = {
                    navController.navigate(Screen.NewsList.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.NewsList.route) {
            NewsListScreen(
                onArticleClick = { url ->
                    if (url == "profile") {
                        navController.navigate(Screen.Profile.route)
                    } else {
                        navController.navigate(Screen.ArticleDetail.createRoute(url))
                    }
                },
                context = context,
                newsRepository = newsRepository,
                userData = googleAuthUiClient.getSignedInUser(),
                savedArticlesViewModel = savedArticlesViewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                userData = googleAuthUiClient.getSignedInUser(),
                googleAuthUiClient = googleAuthUiClient
            )
        }

        composable(Screen.SavedArticles.route) {
            SavedArticlesScreen(
                navController = navController,
                viewModel = savedArticlesViewModel
            )
        }

        composable(route = Screen.LanguagePreferences.route) {
            LanguagePreferencesScreen(
                navController = navController,
                onLanguageSelected = { languageCode ->
                    newsRepository.setLanguage(languageCode)
                }
            )
        }

        composable(
            route = Screen.ArticleDetail.route,
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: return@composable
            val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            
            // Fetch article details from repository
            val article = remember(decodedUrl) {
                newsRepository.getArticleByUrl(decodedUrl)
            }

            article?.let { article ->
                ArticleDetailScreen(
                    article = article,
                    onBackClick = { navController.navigateUp() },
                    onShareClick = {
                        val shareText = """
                            ${article.title}
                            
                            Read more: ${article.url}
                            
                            Shared via TrendAlert
                        """.trimIndent()
                        
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, article.title)
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Article"))
                    },
                    onSaveClick = {
                        if (savedArticlesViewModel.isArticleSaved(article.url)) {
                            savedArticlesViewModel.removeArticle(article)
                        } else {
                            savedArticlesViewModel.addArticle(article)
                        }
                    },
                    isSaved = savedArticlesViewModel.isArticleSaved(article.url)
                )
            }
        }
    }
} 