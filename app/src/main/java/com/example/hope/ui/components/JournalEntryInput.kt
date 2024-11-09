package com.example.hope.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalEntryInput(
    date: LocalDate,
    content: String,
    onEntrySaved: (String) -> Unit,
    onContentChange: (String) -> Unit // Thêm callback để thay đổi nội dung
) {
    Column {
        Text(
            text = "Nhật ký ngày ${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = content,
            onValueChange = { onContentChange(it) }, // Gọi hàm để thay đổi nội dung
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nhập nội dung nhật ký") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onEntrySaved(content)
                onContentChange("") // Reset lại `content` khi lưu
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Lưu")
        }
    }
}