package com.example.trendalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.trendalert.navigation.NavGraph
import com.example.trendalert.ui.theme.TrendAlertTheme
import com.example.trendalert.auth.presentation.sign_in.GoogleAuthUiClient
import com.example.trendalert.navigation.Screen
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrendAlertTheme {
                // Check if user is already signed in
                val currentUser = FirebaseAuth.getInstance().currentUser
                MainScreen(
                    googleAuthUiClient = googleAuthUiClient,
                    startDestination = if (currentUser != null) {
                        Screen.NewsList.route
                    } else {
                        Screen.SignIn.route
                    }
                )
            }
        }
    }
}

@Composable
private fun MainScreen(
    googleAuthUiClient: GoogleAuthUiClient,
    startDestination: String
) {
    TrendAlertTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavGraph(
                navController = navController,
                startDestination = startDestination,
                googleAuthUiClient = googleAuthUiClient
            )
        }
    }
}
