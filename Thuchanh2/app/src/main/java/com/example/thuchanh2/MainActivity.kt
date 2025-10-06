// THAY ĐỔI 1: Package name bây giờ là của dự án mới
package com.example.thuchanh2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// THAY ĐỔI 2: Import Theme của dự án mới (Thuchanh2Theme)
import com.example.thuchanh2.ui.theme.Thuchanh2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // THAY ĐỔI 3: Gọi Theme của dự án mới
            Thuchanh2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Gọi màn hình bài tập ở đây
                    EmailValidatorScreen()
                }
            }
        }
    }
}

@Composable
fun EmailValidatorScreen() {
    // State để lưu trữ email người dùng nhập
    var emailValue by remember { mutableStateOf("") }
    // State để lưu trữ thông báo (lỗi hoặc thành công)
    var validationMessage by remember { mutableStateOf<String?>(null) }
    // State để lưu màu sắc của thông báo (đỏ cho lỗi, xanh cho thành công)
    var messageColor by remember { mutableStateOf(Color.Red) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Thực hành 02", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = emailValue,
            onValueChange = { emailValue = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (validationMessage != null) {
            Text(
                text = validationMessage!!,
                color = messageColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                if (emailValue.isBlank()) {
                    validationMessage = "Email không hợp lệ"
                    messageColor = Color.Red
                } else if (!emailValue.contains("@")) {
                    validationMessage = "Email không đúng định dạng"
                    messageColor = Color.Red
                } else {
                    validationMessage = "Bạn đã nhập email hợp lệ"
                    messageColor = Color(0xFF008000) // Màu xanh lá cây
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kiểm tra")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EmailValidatorScreenPreview() {
    // THAY ĐỔI 3: Gọi Theme của dự án mới
    Thuchanh2Theme {
        EmailValidatorScreen()
    }
}