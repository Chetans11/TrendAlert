package com.example.trendalert.components

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun ArticleWebView(
    url: String,
    modifier: Modifier = Modifier
) {
    val isDarkMode = isSystemInDarkTheme()
    val darkModeScript = """
        (function() {
            if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                document.body.style.backgroundColor = '#121212';
                document.body.style.color = '#FFFFFF';
                document.body.style.setProperty('color-scheme', 'dark');
                
                // Style links
                const links = document.getElementsByTagName('a');
                for (let link of links) {
                    link.style.color = '#BB86FC';
                }
                
                // Style headings
                const headings = document.querySelectorAll('h1, h2, h3, h4, h5, h6');
                for (let heading of headings) {
                    heading.style.color = '#FFFFFF';
                }
                
                // Style paragraphs
                const paragraphs = document.getElementsByTagName('p');
                for (let p of paragraphs) {
                    p.style.color = '#E0E0E0';
                }
            }
        })();
    """.trimIndent()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        view?.evaluateJavascript(darkModeScript, null)
                    }
                }
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadsImagesAutomatically = true
                }
            }
        },
        modifier = modifier,
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}