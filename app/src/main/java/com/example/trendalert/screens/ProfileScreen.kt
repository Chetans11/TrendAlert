package com.example.trendalert.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trendalert.auth.presentation.sign_in.GoogleAuthUiClient
import com.example.trendalert.auth.presentation.sign_in.UserData
import kotlinx.coroutines.launch
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.trendalert.ui.theme.TrendAlertTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userData: UserData?,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val scope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    
    val headerColors = listOf(
        TrendAlertTheme.trendAlertDarkBlue,
        TrendAlertTheme.trendAlertBlue,
        TrendAlertTheme.trendAlertLightBlue
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TrendAlertTheme.getBackgroundColor())
        ) {
            // Top Bar with gradient
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = headerColors,
                                start = Offset(0f, 0f),
                                end = Offset(500f, 500f)
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Back button row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { navController.navigateUp() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }

                        // Profile section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Profile photo
                            if (userData?.profilePictureUrl != null) {
                                AsyncImage(
                                    model = userData.profilePictureUrl,
                                    contentDescription = "Profile picture",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = userData?.username?.firstOrNull()?.toString() ?: "?",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White
                                    )
                                }
                            }

                            // Name section
                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = userData?.username ?: "Guest User",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Content section with weight to push sign out button to bottom
            Column(
                modifier = Modifier
                    .weight(1f) // This will push the sign out button to the bottom
                    .fillMaxWidth()
                    .background(TrendAlertTheme.getBackgroundColor())
                    .padding(16.dp)
            ) {
                SettingsItem(
                    title = "Language Preferences",
                    subtitle = "Manage your language settings",
                    onClick = { navController.navigate("language_preferences") },
                    showDivider = true
                )

                SettingsItem(
                    title = "Saved Articles",
                    subtitle = "View your saved articles",
                    onClick = { navController.navigate("saved_articles") },
                    showDivider = true
                )
            }

            // Sign Out Button in a surface to match theme
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TrendAlertTheme.getBackgroundColor()),
                color = TrendAlertTheme.getBackgroundColor()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                googleAuthUiClient.signOut()
                                navController.navigate("sign_in") {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (isDarkTheme) 
                                Color(0xFF2D2D2D) 
                                else TrendAlertTheme.getSurfaceColor(),
                            contentColor = if (isDarkTheme) 
                                Color.White 
                                else TrendAlertTheme.trendAlertBlue
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Sign Out",
                                tint = if (isDarkTheme) 
                                    Color.White 
                                    else TrendAlertTheme.trendAlertBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sign Out",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = if (isDarkTheme) 
                                    Color.White 
                                    else TrendAlertTheme.trendAlertBlue
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showDivider: Boolean = false
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    Column {
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkTheme) 
                        Color.White 
                        else TrendAlertTheme.getTextColor()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDarkTheme) 
                        Color.White.copy(alpha = 0.7f) 
                        else TrendAlertTheme.getTextSecondaryColor()
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 0.75.dp,
                color = if (isDarkTheme) 
                    Color.White.copy(alpha = 0.1f) 
                    else TrendAlertTheme.getDividerColor()
            )
        }
    }
} 