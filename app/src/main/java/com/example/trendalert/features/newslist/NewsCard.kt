package com.example.trendalert.features.newslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.trendalert.features.shared.Article
import com.example.trendalert.ui.theme.TrendAlertTheme

@Composable
fun NewsCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .padding(4.dp)
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // ✅ Updated field: `image_url`
            article.image_url?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                // ✅ Use `title`
                Text(text = article.title, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(4.dp))

                // ✅ Use `description`
                Text(
                    text = article.description ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ✅ Display source name and date
                Text(
                    text = "${article.source_id} • ${article.pubDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsCardPreview() {
    TrendAlertTheme {
        val sampleArticle = Article(
            article_id = "1",
            title = "Sample News Title",
            description = "This is a brief description of the sample news article.",
            link = "https://example.com/sample-article",
            source_id = "Example News",
            source_url = "https://example.com",
            source_icon = "https://example.com/icon.png",
            image_url = "https://example.com/sample-image.jpg",
            category = listOf("Technology"),
            pubDate = "2025-02-19",
            country = listOf("IN"),
            language = "en"
        )
        NewsCard(article = sampleArticle, onClick = {})
    }
}
