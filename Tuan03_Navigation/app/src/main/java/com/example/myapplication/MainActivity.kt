package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.ui.theme.MyApplicationTheme

// --------- Routes
sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object List : Screen("list")
    data object Detail : Screen("detail/{type}") {
        fun of(type: String) = "detail/$type"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = Screen.Welcome.route) {

                    composable(Screen.Welcome.route) {
                        WelcomeScreen(onStart = { nav.navigate(Screen.List.route) })
                    }

                    composable(Screen.List.route) {
                        ComponentsListScreen(onOpen = { type ->
                            nav.navigate(Screen.Detail.of(type))
                        })
                    }

                    composable(
                        Screen.Detail.route,
                        arguments = listOf(navArgument("type") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: "Text"
                        TextDetailScreen(title = "$type Detail")
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF2A2A2A)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo từ Android Developers (ổn định, không bị chặn)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://developer.android.com/static/codelabs/jetpack-compose-animation/img/jetpack_compose_logo_with_rocket.png?hl=vi")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(android.R.drawable.stat_sys_download),
                    error = painterResource(android.R.drawable.stat_notify_error),
                    modifier = Modifier.size(160.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(16.dp))
                Text("Jetpack Compose", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                    fontSize = 14.sp, lineHeight = 20.sp, color = Color(0xFF666666)
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) { Text("I’m ready", fontSize = 16.sp) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsListScreen(onOpen: (String) -> Unit) {
    val items = listOf(
        "Text" to "Displays text",
        "Image" to "Displays an image",
        "TextField" to "Input field for text",
        "PasswordField" to "Input field for passwords",
        "Column" to "Arranges elements vertically",
        "Row" to "Arranges elements horizontally",
    )
    Scaffold(topBar = { TopAppBar(title = { Text("UI Components List") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            items.forEach { (title, subtitle) ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F1FF),
                    onClick = { onOpen(title) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(title, fontWeight = FontWeight.SemiBold)
                            Text(subtitle, fontSize = 12.sp, color = Color(0xFF5F6E82))
                        }
                        Text("›", fontSize = 22.sp, color = Color(0xFF5F6E82))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDetailScreen(title: String) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 20.dp, vertical = 24.dp)) {
            val demo = buildAnnotatedString {
                append("The ")
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("quick") }
                append(" ")
                withStyle(SpanStyle(color = Color(0xFFB57400), fontWeight = FontWeight.Bold)) { append("Brown") }
                append("\nfox ")
                withStyle(SpanStyle(letterSpacing = 6.sp)) { append("jumps") }
                append(" ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("over") }
                append("\n")
                append("the ")
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("lazy") }
                append(" dog.")
            }
            Text(demo, fontSize = 28.sp, lineHeight = 36.sp)
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcome() { MyApplicationTheme { WelcomeScreen {} } }
