package com.example.hope.reminder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hope.reminder.data.database.TaskOccurrence

@Composable
fun TaskList(tasks: List<TaskOccurrence>) {
    if (tasks.isEmpty()) {
        Text(
            text = "Không có công việc nào cho ngày này.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            tasks.forEach { task ->
                TaskItem(task)
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskOccurrence) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.content ?: "Không có nội dung")
            Text(text = "Giờ: ${task.time ?: "Không đặt"}")
            Text(text = "Trạng thái: ${if (task.isCompleted) "Đã hoàn thành" else "Chưa hoàn thành"}")
        }
    }
}