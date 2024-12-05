package com.example.hope.reminder.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hope.reminder.data.database.TaskDay
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.hope.reminder.data.database.Task
import com.example.hope.reminder.ui.TaskEvent
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskList(
    tasks: List<Pair<Task, TaskDay>>,
    onEvent: (TaskEvent) -> Unit
) {
    if (tasks.isEmpty()) {
        Text(
            text = "Không có công việc nào cho ngày này.",
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(tasks) { task ->
                TaskItem(
                    task,
                    onEvent = onEvent
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Pair<Task, TaskDay>,
    onEvent: (TaskEvent) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var taskChoose by remember { mutableStateOf<Task?>(null) }
    var taskDayChoose by remember { mutableStateOf<TaskDay?>(null) }

    // Dialog xác nhận xóa
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xóa công việc") },
            text = { Text("Bạn có chắc chắn muốn xóa ${taskDayChoose?.content} này?") },
            confirmButton = {
                TextButton(onClick = {
                    taskDayChoose?.let { TaskEvent.DeleteTaskDay(it) }?.let { onEvent(it) }
                    showDeleteDialog = false
                }) {
                    Text("Xóa TaskDay")
                }
                TextButton(onClick = {
                    taskChoose?.let { TaskEvent.DeleteTask(it) }?.let { onEvent(it) }
                    showDeleteDialog = false
                }) {
                    Text("Xóa Task chung")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Dialog chỉnh sửa task
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Chỉnh sửa công việc") },
            text = {
                Column {
                    Text("Chỉnh sửa nội dung hoặc giờ của công việc")
                    // Thêm TextField nếu cần để chỉnh sửa task
                }
            },
            confirmButton = {
                TextButton(onClick = {
//                    onEdit()
                    showEditDialog = false
                }) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // SwipeToDismiss Box state
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.progress) {
        if (dismissState.progress > 0.3f && dismissState.progress != 1f) {
            taskChoose = task.first
            taskDayChoose = task.second
            showDeleteDialog = true
            dismissState.reset()
        }
    }


    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { showEditDialog = true }
                    ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val hourFormatter = DateTimeFormatter.ofPattern("HH")
                    val minFormatter = DateTimeFormatter.ofPattern("mm")

                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = task.second.time?.format(hourFormatter) ?: "",
                            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Thin),
                            color = if (task.second.isCompleted) Color.Gray else Color.Black, // Màu mờ nếu hoàn thành, màu đen nếu chưa hoàn thành
                            textDecoration = if (task.second.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = task.second.time?.format(minFormatter) ?: "",
                            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Thin),
                            color = if (task.second.isCompleted) Color.Gray else Color.Black, // Màu mờ nếu hoàn thành, màu đen nếu chưa hoàn thành
                            textDecoration = if (task.second.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        // Tiêu đề với cỡ chữ lớn và đậm
                        Text(
                            text = task.first.title,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (task.second.isCompleted) Color.Gray else Color.Black, // Màu mờ nếu hoàn thành, màu đen nếu chưa hoàn thành
                                textDecoration = if (task.second.isCompleted) TextDecoration.LineThrough else TextDecoration.None, // Gạch ngang nếu hoàn thành
                            ),
                            modifier = Modifier.padding(bottom = 1.dp)
                        )

                        // Nội dung (hoặc "Không có nội dung" nếu null)
                        Text(
                            text = task.second.content ?: "Không có nội dung",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Light,
                                color = if (task.second.isCompleted) Color.Gray else Color.Black, // Màu mờ nếu hoàn thành, màu đen nếu chưa hoàn thành
                                textDecoration = if (task.second.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            )
                        )

                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Checkbox(
                        checked = task.second.isCompleted,
                        onCheckedChange = {
                            onEvent(TaskEvent.ToggleIsCompleted(task.second.occurrenceId))
                        },
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
        },
        backgroundContent = {
            // Thêm AnimatedVisibility và opacity effect
            val dismissProgress = dismissState.progress

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Biểu tượng xóa, với hiệu ứng opacity dần dần
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .graphicsLayer {
                            alpha = dismissProgress // opacity thay đổi khi vuốt
                        },
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = dismissProgress)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Thêm một biểu tượng khác để tạo sự cân đối
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .graphicsLayer {
                            alpha = dismissProgress // opacity thay đổi khi vuốt
                        },
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = dismissProgress)
                )
            }
        }
    )
}