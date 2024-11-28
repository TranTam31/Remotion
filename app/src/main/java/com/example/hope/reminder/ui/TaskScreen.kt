package com.example.hope.reminder.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hope.reminder.data.database.TaskOccurrence
import com.example.hope.reminder.ui.components.EntryInput
import com.example.hope.reminder.ui.components.TaskList
import com.example.hope.reminder.ui.components.WeekSelector
import com.example.hope.reminder.ui.components.WeekView
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val state by viewModel.state.collectAsState()

    var startOfWeek by remember {
        mutableStateOf(
            LocalDate.now().minusDays((LocalDate.now().dayOfWeek.value % 7).toLong())
        )
    }

    val today = LocalDate.now()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(TaskEvent.ShowDialog) }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            WeekSelector(
                startOfWeek = startOfWeek,
                onPreviousWeek = {
                    startOfWeek = startOfWeek.minusWeeks(1)
                },
                onNextWeek = {
                    startOfWeek = startOfWeek.plusWeeks(1)
                }
            )

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxHeight()
            ) {
                WeekView(
                    startOfWeek = startOfWeek,
                    selectedDate = state.dateSelected,
                    onDateSelected = { date ->
                        viewModel.onEvent(TaskEvent.SetDateSelected(date))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                var taskList by remember { mutableStateOf<List<TaskOccurrence>>(emptyList()) }
                LaunchedEffect(state.dateSelected) {
                    taskList = viewModel.getTaskByDate(state.dateSelected)
                }
                TaskList(tasks = taskList)
            }
        }
        if (state.isAddingTask) {
            AlertDialog(
                onDismissRequest = { viewModel.onEvent(TaskEvent.HideDialog) },
                title = { Text("Thêm Task") },
                text = {
                    EntryInput(viewModel)
                },
                confirmButton = {
                    Button(onClick = { viewModel.onEvent(TaskEvent.SaveTask) }) {
                        Text("Lưu")
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.onEvent(TaskEvent.HideDialog) }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}