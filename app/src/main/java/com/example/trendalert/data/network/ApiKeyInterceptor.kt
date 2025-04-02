// ApiKeyInterceptor.kt
package com.example.trendalert.data.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        // Modify the URL to add the API key as a query parameter
        val urlWithApiKey = originalRequest.url.newBuilder()
            .addQueryParameter("apiKey", apiKey)  // Add the API key as a query parameter
            .build()

        // Create a new request with the modified URL
        val newRequest = originalRequest.newBuilder()
            .url(urlWithApiKey)  // Replace original URL with the one containing the API key
            .build()

        return chain.proceed(newRequest)  // Proceed with the modified request
    }
}
