package com.example.trendalert.features.newsdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun NewsDetailScreen(articleId: String?, viewModel: NewsDetailViewModel = viewModel()) {
    val article by viewModel.article

    Column(modifier = Modifier.padding(16.dp)) {
        article?.let {
            Text(text = it.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            it.image_url?.let { imageUrl ->  // ✅ Updated field
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Article Image",
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = it.description ?: "No description available")
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Source: ${it.source_id}", style = MaterialTheme.typography.bodySmall) // ✅ Updated
            Text(text = "Published: ${it.pubDate}", style = MaterialTheme.typography.bodySmall) // ✅ Updated
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Read more: ${it.link}", color = MaterialTheme.colorScheme.primary) // ✅ Updated
        } ?: run {
            Text(text = "Loading...")
        }
    }
}
