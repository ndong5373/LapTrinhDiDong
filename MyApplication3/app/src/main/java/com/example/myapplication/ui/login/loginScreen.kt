package com.example.myapplication.ui.login


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun ComposeInteractiveDemo(modifier: Modifier = Modifier) {
    // ----- STATE -----
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var newsChecked by rememberSaveable { mutableStateOf(false) }
    var notificationsOn by rememberSaveable { mutableStateOf(true) }
    val radioOptions = listOf("Option A", "Option B", "Option C")
    var selectedRadio by rememberSaveable { mutableStateOf(radioOptions.first()) }
    var volume by rememberSaveable { mutableStateOf(50f) }
    var clicks by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //  TextField / OutlinedTextField
        Text(text = "Nhập liệu", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        //   Buttons
        Text(text = "Buttons", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { clicks++ }) { Text("Đăng nhập ($clicks)") }
            OutlinedButton(onClick = { username = ""; password = "" }) { Text("Clear") }
        }

        // Các nút lựa chọn
        Text(text = "Lựa chọn", style = MaterialTheme.typography.titleMedium)
        // Checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = newsChecked, onCheckedChange = { newsChecked = it })
            Text("Nhận bản tin (Checkbox)", modifier = Modifier.padding(start = 8.dp))
        }
        // Switch
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = notificationsOn, onCheckedChange = { notificationsOn = it })
            Text("Bật thông báo (Switch)", modifier = Modifier.padding(start = 8.dp))
        }
        // Radio group
        Column {
            Text("Chọn một (RadioButton)")
            radioOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    RadioButton(
                        selected = selectedRadio == option,
                        onClick = { selectedRadio = option }
                    )
                    Text(option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        //  Slider
        Text(text = "Slider", style = MaterialTheme.typography.titleMedium)
        Column {
            Slider(
                value = volume,
                onValueChange = { volume = it },
                valueRange = 0f..100f,
                // 10 nấc rời rạc (0..100 mỗi 10 đơn vị)
                steps = 9
            )
            Text("Âm lượng: ${volume.toInt()}%")
        }


    }
}

@Preview(showBackground = true)
@Composable
private fun ComposeInteractiveDemoPreview() {
    MaterialTheme {
        Surface { ComposeInteractiveDemo() }
    }
}