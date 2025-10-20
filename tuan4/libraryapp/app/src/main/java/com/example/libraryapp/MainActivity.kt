package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// ---------- Data models ----------
data class Book(val id: String, val title: String)
data class Student(
    val id: String,
    var name: String,
    val borrowed: MutableSet<String> = mutableSetOf() // set bookIds
)

// ---------- Activity ----------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LibraryApp() }
    }
}

// ---------- App root ----------
@Composable
fun LibraryApp() {
    var books by remember {
        mutableStateOf(listOf(Book("b1", "Sách 01"), Book("b2", "Sách 02")))
    }
    var students by remember {
        mutableStateOf(
            listOf(
                Student("s1", "Nguyen Van A", mutableSetOf("b1", "b2")),
                Student("s2", "Nguyen Thi B", mutableSetOf("b1")),
                Student("s3", "Nguyen Van C", mutableSetOf())
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var currentStudentIdx by remember { mutableIntStateOf(0) }

    // đảm bảo index hợp lệ khi xóa/thêm sinh viên
    if (students.isNotEmpty()) {
        currentStudentIdx = currentStudentIdx.coerceIn(0, students.lastIndex)
    }

    val tabs = listOf("Quản lý", "DS Sách", "Sinh viên")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { i, title ->
                    NavigationBarItem(
                        selected = currentIndex == i,
                        onClick = { currentIndex = i },
                        icon = {},
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { inner ->
        Box(Modifier.padding(inner)) {
            when (currentIndex) {
                0 -> ManageScreen(
                    books = books,
                    students = students,
                    currentStudentIdx = currentStudentIdx,
                    onChangeStudentName = { newName ->
                        students = students.toMutableList().also { it[currentStudentIdx].name = newName }
                    },
                    onPickStudent = { idx -> currentStudentIdx = idx },
                    onToggleBorrow = { bookId, checked ->
                        students = students.toMutableList().also {
                            val s = it[currentStudentIdx]
                            if (checked) s.borrowed.add(bookId) else s.borrowed.remove(bookId)
                        }
                    },
                    onAddBorrow = { bookId ->
                        students = students.toMutableList().also { it[currentStudentIdx].borrowed.add(bookId) }
                    }
                )
                1 -> BooksScreen(
                    books = books,
                    onAddBook = { title ->
                        val nextId = "b" + (books.size + 1)
                        books = books + Book(nextId, title)
                    },
                    onDeleteBook = { id ->
                        // xóa khỏi kho + trả sách khỏi tất cả sinh viên
                        books = books.filterNot { it.id == id }
                        students = students.map { s -> s.copy(borrowed = (s.borrowed - id).toMutableSet()) }
                    }
                )
                2 -> StudentsScreen(
                    students = students,
                    onAddStudent = { name ->
                        val nextId = "s" + (students.size + 1)
                        students = students + Student(nextId, name)
                    },
                    onDeleteStudent = { id ->
                        students = students.filterNot { it.id == id }
                        if (currentStudentIdx >= students.size)
                            currentStudentIdx = (students.size - 1).coerceAtLeast(0)
                    },
                    onJumpToManage = { idx ->
                        currentStudentIdx = idx
                        currentIndex = 0
                    }
                )
            }
        }
    }
}

// ---------- Screen 1: Quản lý (mượn sách cho SV đang chọn) ----------
@Composable
fun ManageScreen(
    books: List<Book>,
    students: List<Student>,
    currentStudentIdx: Int,
    onChangeStudentName: (String) -> Unit,
    onPickStudent: (Int) -> Unit,
    onToggleBorrow: (bookId: String, checked: Boolean) -> Unit,
    onAddBorrow: (bookId: String) -> Unit
) {
    if (students.isEmpty()) {
        EmptyState("Chưa có sinh viên.\nVào tab “Sinh viên” để thêm nhé.")
        return
    }
    val student = students[currentStudentIdx]
    var nameField by remember { mutableStateOf(student.name) }
    var showChooseStudent by remember { mutableStateOf(false) }
    var showAddBook by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Hệ thống Quản lý Thư viện", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        // Ô chọn/đổi sinh viên
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = nameField,
                onValueChange = { nameField = it },
                label = { Text("Sinh viên") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { onChangeStudentName(nameField) }) { Text("Thay đổi") }
        }
        TextButton(onClick = { showChooseStudent = true }) { Text("Đổi sinh viên khác…") }

        HorizontalDivider()

        Text("Danh sách sách", fontWeight = FontWeight.SemiBold)

        val borrowed = student.borrowed
        if (borrowed.isEmpty()) {
            Card(Modifier.fillMaxWidth()) {
                Text(
                    "Bạn chưa mượn quyển sách nào\nNhấn ‘Thêm’ để bắt đầu hành trình đọc sách!",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(books.filter { it.id in borrowed }) { book ->
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var checked by remember { mutableStateOf(true) }
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                onToggleBorrow(book.id, it) // bỏ chọn = trả sách
                            }
                        )
                        OutlinedTextField(
                            value = book.title,
                            onValueChange = {},
                            enabled = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { showAddBook = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Thêm") }

        // Dialog chọn sinh viên
        if (showChooseStudent) {
            SimplePickerDialog(
                title = "Chọn sinh viên",
                items = students.map { it.name },
                onDismiss = { showChooseStudent = false },
                onPick = { idx ->
                    onPickStudent(idx)
                    nameField = students[idx].name
                }
            )
        }
        // Dialog thêm sách mượn
        if (showAddBook) {
            val available = books.filterNot { it.id in borrowed }
            SimplePickerDialog(
                title = if (available.isEmpty()) "Không còn sách để mượn" else "Chọn sách để mượn",
                items = available.map { it.title },
                onDismiss = { showAddBook = false },
                onPick = { i ->
                    if (available.isNotEmpty()) onAddBorrow(available[i].id)
                }
            )
        }
    }
}

// ---------- Screen 2: DS Sách ----------
@Composable
fun BooksScreen(
    books: List<Book>,
    onAddBook: (String) -> Unit,
    onDeleteBook: (String) -> Unit
) {
    var showAdd by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Danh sách Sách", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (books.isEmpty()) {
            EmptyState("Chưa có sách.\nNhấn Thêm để tạo.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(books) { b ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(b.title)
                            TextButton(onClick = { onDeleteBook(b.id) }) { Text("Xóa") }
                        }
                    }
                }
            }
        }
        Button(onClick = { showAdd = true }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Thêm") }

        if (showAdd) {
            TextInputDialog(
                title = "Thêm sách",
                hint = "Nhập tên sách…",
                onDismiss = { showAdd = false },
                onConfirm = { name ->
                    if (name.isNotBlank()) onAddBook(name.trim())
                }
            )
        }
    }
}

// ---------- Screen 3: Sinh viên ----------
@Composable
fun StudentsScreen(
    students: List<Student>,
    onAddStudent: (String) -> Unit,
    onDeleteStudent: (String) -> Unit,
    onJumpToManage: (Int) -> Unit
) {
    var showAdd by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Danh sách Sinh viên", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (students.isEmpty()) {
            EmptyState("Chưa có sinh viên.\nNhấn Thêm để tạo.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(students) { s ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(s.name, fontWeight = FontWeight.SemiBold)
                                Text("Đang mượn: ${s.borrowed.size}")
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(onClick = { onJumpToManage(students.indexOf(s)) }) { Text("Quản lý") }
                                TextButton(onClick = { onDeleteStudent(s.id) }) { Text("Xóa") }
                            }
                        }
                    }
                }
            }
        }
        Button(onClick = { showAdd = true }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Thêm") }

        if (showAdd) {
            TextInputDialog(
                title = "Thêm sinh viên",
                hint = "Nhập tên sinh viên…",
                onDismiss = { showAdd = false },
                onConfirm = { name ->
                    if (name.isNotBlank()) onAddStudent(name.trim())
                }
            )
        }
    }
}

// ---------- Small reusable UI ----------
@Composable
fun EmptyState(message: String) {
    Card(Modifier.fillMaxWidth()) {
        Text(
            message,
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SimplePickerDialog(
    title: String,
    items: List<String>,
    onDismiss: () -> Unit,
    onPick: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(dismissOnClickOutside = true)) {
        Surface(shape = MaterialTheme.shapes.large, tonalElevation = 2.dp) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(title, fontWeight = FontWeight.Bold)
                if (items.isEmpty()) {
                    Text("Không có mục nào.")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismiss) { Text("Đóng") }
                    }
                } else {
                    LazyColumn(Modifier.heightIn(max = 300.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(items.indices.toList()) { i ->
                            TextButton(onClick = { onPick(i); onDismiss() }, modifier = Modifier.fillMaxWidth()) {
                                Text(items[i], textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismiss) { Text("Hủy") }
                    }
                }
            }
        }
    }
}

@Composable
fun TextInputDialog(
    title: String,
    hint: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.large, tonalElevation = 2.dp) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(title, fontWeight = FontWeight.Bold)
                OutlinedTextField(value = text, onValueChange = { text = it }, placeholder = { Text(hint) })
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Hủy") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onConfirm(text); onDismiss() }) { Text("OK") }
                }
            }
        }
    }
}
