// Đã sửa package name thành Tuan02
package com.example.tuan02

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Đã sửa import Theme thành Tuan02Theme
import com.example.tuan02.ui.theme.Tuan02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Đã sửa tên Theme thành Tuan02Theme
            Tuan02Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeworkScreen()
                }
            }
        }
    }
}

@Composable
fun HomeworkScreen() {
    // State cho ô nhập Họ và tên
    var nameValue by remember { mutableStateOf("") }
    // State cho ô nhập Tuổi
    var ageValue by remember { mutableStateOf("") }
    // State để hiển thị kết quả cuối cùng
    var resultMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "THỰC HÀNH 01", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nameValue,
            onValueChange = { nameValue = it },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ageValue,
            onValueChange = { ageValue = it },
            label = { Text("Tuổi") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val name = nameValue.trim()
                val age = ageValue.toIntOrNull()

                if (name.isBlank()) {
                    resultMessage = "Lỗi: Vui lòng nhập họ và tên."
                } else if (age == null || age < 0) {
                    resultMessage = "Lỗi: Tuổi không hợp lệ."
                } else {
                    val category = when (age) {
                        in 0..1 -> "Em bé"
                        in 2..5 -> "Trẻ em"
                        in 6..65 -> "Người lớn"
                        else -> "Người già"
                    }
                    resultMessage = "Họ và tên: $name\nTuổi: $age\nTrạng thái: $category"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kiểm tra")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (resultMessage != null) {
            Text(
                text = resultMessage!!,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeworkScreenPreview() {
    // Đã sửa tên Theme thành Tuan02Theme
    Tuan02Theme {
        HomeworkScreen()
    }
}