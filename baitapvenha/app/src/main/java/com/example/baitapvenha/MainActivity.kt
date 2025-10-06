package com.example.baitapvenha // Thay bằng package của bạn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baitapvenha.ui.theme.BaitapvenhaTheme // Thay bằng Theme của bạn

@Composable
fun ExerciseScreen() {
    // State đã được đổi đúng thành numberList
    var textValue by remember { mutableStateOf("") }
    var numberList by remember { mutableStateOf(listOf<Int>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Thực hành 02", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Nhập vào số lượng") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                val number = textValue.toIntOrNull()

                // SỬA LỖI 1: Reset numberList thay vì itemCount
                numberList = listOf()
                errorMessage = null

                when {
                    number == null -> {
                        errorMessage = "Dữ liệu bạn nhập không hợp lệ"
                    }
                    number == 0 -> {
                        errorMessage = "Dữ liệu không tồn tại"
                    }
                    number < 0 -> {
                        errorMessage = "Số lượng phải là số dương"
                    }
                    // SỬA LỖI 1: Tạo ra danh sách và gán cho numberList
                    else -> {
                        numberList = (1..number).toList()
                    }
                }
            }) {
                Text("Tạo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red, fontSize = 16.sp)
        } else {
            // SỬA LỖI 2: LazyColumn bây giờ sẽ lặp qua numberList
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Dùng items(items = ...) để làm việc với danh sách
                items(items = numberList, key = { it }) { number ->
                    // Truyền vào hành động onClick để xóa item
                    RedListItem(
                        number = number,
                        onClick = { numberToDelete ->
                            numberList = numberList.filter { it != numberToDelete }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun RedListItem(number: Int, onClick: (Int) -> Unit) { // Thêm tham số onClick
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Red, shape = RoundedCornerShape(8.dp))
            .clickable { onClick(number) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = number.toString(), color = Color.White, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    BaitapvenhaTheme {
        ExerciseScreen()
    }
}