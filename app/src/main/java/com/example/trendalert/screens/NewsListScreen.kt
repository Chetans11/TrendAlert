package com.example.trendalert.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.example.trendalert.components.Article
import com.example.trendalert.components.NewsCard
import com.example.trendalert.data.NewsRepository
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import com.example.trendalert.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import android.content.Intent
import android.content.Context
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import coil.compose.AsyncImage
import com.example.trendalert.auth.presentation.sign_in.UserData
import com.example.trendalert.viewmodels.SavedArticlesViewModel
import com.example.trendalert.ui.theme.TrendAlertTheme

enum class NewsCategory {
    Top, World, Business, Crime, Domestic, Education, Entertainment, Environmental,
    Lifestyle, Other, Politics, Science, Sports, Technology, Tourism;

    fun displayName(): String = when (this) {
        Other -> "Others"
        else -> name.capitalize(Locale.current)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewsListScreen(
    onArticleClick: (String) -> Unit,
    context: Context,
    newsRepository: NewsRepository,
    userData: UserData?,
    savedArticlesViewModel: SavedArticlesViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf(NewsCategory.Top) }
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    // Function to fetch news
    suspend fun fetchNews() {
        try {
            val categoryName = when (selectedCategory) {
                NewsCategory.Top -> null
                NewsCategory.World -> "world"
                NewsCategory.Business -> "business"
                NewsCategory.Crime -> "crime"
                NewsCategory.Domestic -> "domestic"
                NewsCategory.Education -> "education"
                NewsCategory.Entertainment -> "entertainment"
                NewsCategory.Environmental -> "environment"
                NewsCategory.Lifestyle -> "lifestyle"
                NewsCategory.Other -> "other"
                NewsCategory.Politics -> "politics"
                NewsCategory.Science -> "science"
                NewsCategory.Sports -> "sports"
                NewsCategory.Technology -> "technology"
                NewsCategory.Tourism -> "tourism"
            }

            val newArticles = if (categoryName == null) {
                newsRepository.getTopNews()
            } else {
                newsRepository.getNewsByCategory(categoryName)
            }

            if (newArticles.isNotEmpty()) {
                articles = newArticles
                coroutineScope.launch {
                    listState.scrollToItem(0)
                }
            } else {
                println("Debug: No articles returned from API for category: $categoryName")
            }
        } catch (e: Exception) {
            println("Debug: Error fetching news: ${e.message}")
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    // Initial load only - now considers selected category
    LaunchedEffect(Unit) {
        if (articles.isEmpty()) {
            isLoading = true
            fetchNews()
        }
    }

    // Request focus when search becomes active
    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            delay(100)
            focusRequester.requestFocus()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = TrendAlertTheme.getBackgroundColor()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Enhanced Animated Header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .shadow(8.dp),
                color = TrendAlertTheme.getSurfaceColor()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    TrendAlertTheme.trendAlertDarkBlue,
                                    TrendAlertTheme.trendAlertBlue,
                                    TrendAlertTheme.trendAlertLightBlue
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(500f, 500f),
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.trendalert_logo),
                                contentDescription = "TrendAlert Logo",
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp),
                                contentScale = ContentScale.Fit
                            )

                            // New profile photo implementation
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { onArticleClick("profile") }
                            ) {
                                if (userData?.profilePictureUrl != null) {
                                    AsyncImage(
                                        model = userData.profilePictureUrl,
                                        contentDescription = "Profile Picture",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.2f)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                color = Color.White.copy(alpha = 0.2f),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Profile",
                                            tint = Color.White,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Update the Categories Row with reduced padding
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Search with glass effect
                            Surface(
                                modifier = Modifier.width(if (isSearchActive) 240.dp else 48.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White.copy(alpha = 0.95f),
                                tonalElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .height(48.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { isSearchActive = !isSearchActive },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = "Search",
                                            tint = TrendAlertTheme.trendAlertBlue
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = isSearchActive,
                                        enter = expandHorizontally() + fadeIn(),
                                        exit = shrinkHorizontally() + fadeOut()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxHeight()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(horizontal = 8.dp)
                                                    .fillMaxHeight(),
                                                contentAlignment = Alignment.CenterStart
                                            ) {
                                                BasicTextField(
                                                    value = searchQuery,
                                                    onValueChange = { newValue -> searchQuery = newValue },
                                                    singleLine = true,
                                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .focusRequester(focusRequester),
                                                    keyboardOptions = KeyboardOptions(
                                                        imeAction = ImeAction.Done
                                                    ),
                                                    keyboardActions = KeyboardActions(
                                                        onDone = {
                                                            focusManager.clearFocus()
                                                            isSearchActive = false
                                                        }
                                                    )
                                                )
                                                if (searchQuery.isEmpty()) {
                                                    Text(
                                                        text = "Search news...",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                    )
                                                }
                                            }

                                            if (searchQuery.isNotEmpty()) {
                                                IconButton(
                                                    onClick = { searchQuery = "" },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        Icons.Default.Close,
                                                        contentDescription = "Clear search",
                                                        tint = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Categories with enhanced styling
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 0.dp)
                            ) {
                                items(NewsCategory.values()) { category ->
                                    val isSelected = category == selectedCategory
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (category != selectedCategory) {
                                                selectedCategory = category
                                                coroutineScope.launch {
                                                    isRefreshing = true
                                                    fetchNews()
                                                    isRefreshing = false
                                                }
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = category.displayName(),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                                )
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color.White,
                                            selectedLabelColor = TrendAlertTheme.trendAlertBlue,
                                            containerColor = Color.White.copy(alpha = 0.15f),
                                            labelColor = Color.White
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            borderColor = Color.White.copy(alpha = 0.3f),
                                            selectedBorderColor = Color.White,
                                            enabled = true,
                                            selected = isSelected
                                        ),
                                        modifier = Modifier.animateContentSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Enhanced News List
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        fetchNews()
                        isRefreshing = false
                    }
                },
                indicator = { state, refreshTrigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshTrigger,
                        backgroundColor = TrendAlertTheme.trendAlertBlue,
                        contentColor = Color.White,
                        scale = true
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(TrendAlertTheme.getBackgroundColor())
            ) {
                if (isLoading && articles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(TrendAlertTheme.getBackgroundColor()),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = TrendAlertTheme.trendAlertBlue
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(TrendAlertTheme.getBackgroundColor())
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val filteredArticles = articles.filter { article ->
                            searchQuery.isEmpty() ||
                                    article.title.contains(searchQuery, ignoreCase = true) ||
                                    article.description.contains(searchQuery, ignoreCase = true)
                        }

                        if (filteredArticles.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isLoading) "Loading articles..."
                                        else if (searchQuery.isNotEmpty()) "No articles found matching your search"
                                        else "No articles available for this category",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = TrendAlertTheme.trendAlertBlue
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            items(
                                items = filteredArticles,
                                key = { it.url }
                            ) { article ->
                                NewsCard(
                                    article = article.copy(
                                        isBookmarked = savedArticlesViewModel.isArticleSaved(article.url)
                                    ),
                                    onSaveClick = {
                                        if (savedArticlesViewModel.isArticleSaved(article.url)) {
                                            savedArticlesViewModel.removeArticle(article)
                                        } else {
                                            savedArticlesViewModel.addArticle(article)
                                        }
                                    },
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
                                    onArticleClick = onArticleClick,
                                    modifier = Modifier
                                        .animateItemPlacement()
                                        .graphicsLayer {
                                            shadowElevation = 4f
                                            shape = RoundedCornerShape(16.dp)
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}