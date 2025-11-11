package com.example.datastore01

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
// import file DataStore bạn vừa viết:
import com.example.datastore01.ui.darkFlow
import com.example.datastore01.ui.setDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DarkModeApp() }
    }
}

@Composable
fun DarkModeApp() {
    val ctx = LocalContext.current
    val isDark by darkFlow(ctx).collectAsState(initial = false)
    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(Modifier.fillMaxSize()) {
            DarkModeScreen(ctx = ctx, isDark = isDark)
        }
    }
}

@Composable
fun DarkModeScreen(ctx: Context, isDark: Boolean) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isDark) "Chế độ tối: BẬT" else "Chế độ tối: TẮT")
        Spacer(Modifier.height(12.dp))
        Switch(
            checked = isDark,
            onCheckedChange = { newValue ->
                scope.launch { setDark(ctx, newValue) }
            }
        )
    }
}
