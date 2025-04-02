package com.example.trendalert.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.trendalert.components.Article
import com.example.trendalert.ui.theme.TrendAlertTheme
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun NewsCard(
    article: Article,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    onArticleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isBookmarked by remember { mutableStateOf(article.isBookmarked) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onArticleClick(article.url) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) 
                Color(0xFF1E1E1E) 
                else TrendAlertTheme.getSurfaceColor(),
            contentColor = if (isSystemInDarkTheme()) 
                Color.White 
                else TrendAlertTheme.getTextColor()
        )
    ) {
        Column {
            // Image with rounded corners
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = article.urlToImage,
                        onLoading = { /* You could add a shimmer effect here */ }
                    ),
                    contentDescription = "Article image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Content Container with reduced padding
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Title with proper theme color
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 6.dp),
                    color = if (isSystemInDarkTheme()) 
                        Color.White 
                        else TrendAlertTheme.getTextColor()
                )

                // Source and icons in the same row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Source text
                    Text(
                        text = article.source.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSystemInDarkTheme()) 
                            Color.White.copy(alpha = 0.7f) 
                            else TrendAlertTheme.getTextSecondaryColor(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Icons
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                isBookmarked = !isBookmarked
                                onSaveClick()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                                tint = if (isBookmarked) 
                                    TrendAlertTheme.trendAlertBlue 
                                    else TrendAlertTheme.getTextSecondaryColor(),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = onShareClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share article",
                                tint = TrendAlertTheme.getTextSecondaryColor(),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}