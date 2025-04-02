package com.example.trendalert.auth.presentation.sign_in

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.zIndex
import com.example.trendalert.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.trendalert.ui.theme.TrendAlertTheme
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    
    // Configure Google Sign In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
    }
    
    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    onSignInClick()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Sign in failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Sign in failed: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Brand Colors (matching NewsListScreen)
    val trendAlertBlue = Color(0xFF0088CC)
    val trendAlertDarkBlue = Color(0xFF006699)
    val trendAlertLightBlue = Color(0xFF00AAFF)

    val headerColors = listOf(
        trendAlertDarkBlue,
        trendAlertBlue,
        trendAlertLightBlue
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = TrendAlertTheme.getBackgroundColor()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Animated Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = headerColors,
                            start = Offset(translateAnim.value, 0f),
                            end = Offset(translateAnim.value + 500f, 500f)
                        )
                    )
                    .zIndex(-1f)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Section with Logo
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Logo without the glass effect surface
                        Image(
                            painter = painterResource(id = R.drawable.trendalert_logo),
                            contentDescription = "TrendAlert Logo",
                            modifier = Modifier
                                .size(300.dp)
                                .padding(bottom = 24.dp),
                            contentScale = ContentScale.Fit
                        )

                        // Welcome Text section
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp)
                        ) {
                            Text(
                                text = "Step into TrendAlert",
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (isSystemInDarkTheme()) 
                                    Color.Black.copy(alpha = 0.9f)
                                    else Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )

                            Text(
                                text = "Your Daily News Companion",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSystemInDarkTheme()) 
                                    Color.Black.copy(alpha = 0.7f)
                                    else Color.White.copy(alpha = 0.9f),
                                maxLines = 1,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                // Bottom Section with Sign In Button
                Column(
                    modifier = Modifier
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Glass effect card with dark theme support
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(28.dp),
                                spotColor = if (isSystemInDarkTheme()) 
                                    Color.White.copy(alpha = 0.1f) 
                                    else TrendAlertTheme.trendAlertBlue.copy(alpha = 0.25f)
                            ),
                        shape = RoundedCornerShape(28.dp),
                        color = TrendAlertTheme.getSurfaceColor()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Get Started",
                                style = MaterialTheme.typography.titleLarge,
                                color = if (isSystemInDarkTheme()) 
                                    TrendAlertTheme.trendAlertLightBlue
                                    else TrendAlertTheme.trendAlertBlue,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Button(
                                onClick = {
                                    val signInIntent = googleSignInClient.signInIntent
                                    launcher.launch(signInIntent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSystemInDarkTheme())
                                        trendAlertBlue
                                        else trendAlertBlue
                                ),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .width(280.dp)
                                    .height(48.dp)
                                    .shadow(4.dp, RoundedCornerShape(24.dp))
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    // Google Logo in Circle
                                    Surface(
                                        modifier = Modifier.size(20.dp),
                                        shape = CircleShape,
                                        color = if (isSystemInDarkTheme())
                                            Color(0xFF1E1E1E)
                                            else Color.White
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.google_logo),
                                            contentDescription = "Google Logo",
                                            modifier = Modifier.padding(3.dp),
                                            colorFilter = null // Remove the color filter to show original Google logo colors
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Sign in with Google",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSystemInDarkTheme())
                                            Color.Black
                                            else Color.White,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }
                    }

                    // Additional info text
                    Text(
                        text = "Stay updated with the latest news",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSystemInDarkTheme()) 
                            Color.Black.copy(alpha = 0.6f)
                            else Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        // Error Message
        LaunchedEffect(key1 = state.signInError) {
            state.signInError?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

//data class SignInState(
//    val signInError: String? = null
//)