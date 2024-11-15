package com.example.hope.mood_tracker.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
    emotion: Int,
    onEntrySaved: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onEmotionChange: (Int) -> Unit
) {
    Column {
        Text(
            text = "Nhật ký ngày ${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
//        for (emotionHere in 1..5) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                RadioButton(
//                    selected = emotion == emotion,  // Kiểm tra xem đây có phải lựa chọn hiện tại không
//                    onClick = { selectedMood = emotion },  // Khi người dùng click, thay đổi giá trị của selectedMood
//                )
//            }
//        }
        for (emotionHere in 1..5) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = emotion == emotionHere,  // Kiểm tra xem đây có phải lựa chọn hiện tại không
                    onClick = { onEmotionChange(emotionHere) },  // Khi người dùng click, thay đổi giá trị cảm xúc
                )
                Text(text = "Cảm xúc $emotionHere")
            }
        }

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