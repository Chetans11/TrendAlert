package com.example.trendalert.commons

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendAlertContainer() {
    val navController = rememberNavController()

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("TrendAlert") }) },
            content = {
                TrendAlertNavHost(navController = navController)
            }
        )
    }
}
