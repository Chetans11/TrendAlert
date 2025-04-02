package com.example.trendalert.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trendalert.navigation.Screen
import com.example.trendalert.ui.theme.TrendAlertTheme

data class LanguageOption(
    val code: String,
    val firstLetter: String,
    val displayName: String,
    val nativeName: String
)

@Composable
fun LanguagePreferencesScreen(
    navController: NavController,
    onLanguageSelected: (String) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (isDarkTheme) Color(0xFF121212) else TrendAlertTheme.getBackgroundColor()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isDarkTheme) {
                            listOf(
                                Color(0xFF1E1E1E),
                                Color(0xFF121212)
                            )
                        } else {
                            listOf(
                                TrendAlertTheme.trendAlertDarkBlue,
                                TrendAlertTheme.trendAlertBlue
                            )
                        }
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select your Language",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                val languages = listOf(
                    LanguageOption("en", "E","English", "English"),
                    LanguageOption("hi", "हि","Hindi", "हिंदी"),
                    LanguageOption("kn", "ಕ", "Kannada", "ಕನ್ನಡ"),
                    LanguageOption("te", "తె","Telugu", "తెలుగు"),
                    LanguageOption("ml", "മ", "Malayalam", "മലയാളം"),
                    LanguageOption("mr", "म","Marathi", "मराठी"),
                    LanguageOption("ta", "த", "Tamil", "தமிழ்"),
                    LanguageOption("as", "অ","Assamese", "অসমীয়া"),
                    LanguageOption("bn", "বা","Bengali", "বাংলা"),
                    LanguageOption("gu", "ગુ","Gujarati", "ગુજરાતી"),
                    LanguageOption("or", "ଓ","Oriya", "ଓଡ଼ିଆ"),
                    LanguageOption("pa", "ਪੰ","Punjabi", "ਪੰਜਾਬੀ"),
                    LanguageOption("ur", "ا","Urdu", "اردو")
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(languages) { language ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clickable { 
                                    onLanguageSelected(language.code)
                                    navController.navigate(Screen.NewsList.route) {
                                        popUpTo(Screen.LanguagePreferences.route) { inclusive = true }
                                    }
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDarkTheme) 
                                Color(0xFF2D2D2D) 
                                else Color.White,
                            border = BorderStroke(
                                1.dp,
                                if (isDarkTheme) 
                                    Color.White.copy(alpha = 0.1f) 
                                    else TrendAlertTheme.trendAlertLightBlue.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = language.firstLetter,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) 
                                        Color.White 
                                        else TrendAlertTheme.trendAlertBlue
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = language.nativeName,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = if (isDarkTheme) 
                                        Color.White.copy(alpha = 0.8f) 
                                        else TrendAlertTheme.trendAlertDarkBlue
                                )
                            }
                        }
                    }
                }

                // Privacy Policy and User Agreement
                Text(
                    text = "By continuing, you accept the User Agreement & Privacy Policy",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
} 