package com.example.trendalert.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import coil.compose.rememberAsyncImagePainter
import com.example.trendalert.components.Article
import com.example.trendalert.ui.theme.TrendAlertTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: Article,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
    isSaved: Boolean
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {

                    IconButton(onClick = onSaveClick) {
                        Icon(
                            if (isSaved) Icons.Default.Star else Icons.Outlined.Star,
                            "Save",
                            tint = if (isSaved) TrendAlertTheme.trendAlertBlue else TrendAlertTheme.getTextColor()
                        )
                    }
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TrendAlertTheme.getSurfaceColor(),
                    titleContentColor = TrendAlertTheme.getTextColor(),
                    navigationIconContentColor = TrendAlertTheme.getTextColor(),
                    actionIconContentColor = TrendAlertTheme.getTextColor()
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .background(TrendAlertTheme.getBackgroundColor())
        ) {
            // Article Image
            article.urlToImage?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Article Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Source and Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.source.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TrendAlertTheme.getTextSecondaryColor()
                    )
                    Text(
                        text = formatDate(article.publishedAt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TrendAlertTheme.getTextSecondaryColor()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = TrendAlertTheme.getTextColor()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TrendAlertTheme.getTextColor()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Content
                Text(
                    text = article.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TrendAlertTheme.getTextColor()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Read More Button
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TrendAlertTheme.trendAlertBlue
                    )
                ) {
                    Text(
                        text = "Read Full Article",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
} 