package com.example.hope.reminder.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hope.reminder.data.RepeatOption
import com.example.hope.reminder.ui.TaskEvent
import com.example.hope.reminder.ui.TaskViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EntryInput(
    viewModel: TaskViewModel
) {
    val state by viewModel.state.collectAsState()
    Column {
        // Tiêu đề Task
        TextField(
            value = state.title,
            onValueChange = { viewModel.onEvent(TaskEvent.SetTitle(it)) },
            label = { Text("Tiêu đề") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Nội dung Task
        TextField(
            value = state.content ?: "",
            onValueChange = { viewModel.onEvent(TaskEvent.SetContent(it)) },
            label = { Text("Nội dung") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Selector cho RepeatOption
        var expanded by remember { mutableStateOf(false) }
        Box {
            TextField(
                value = state.repeatOption.toString(),
                onValueChange = {},
                label = { Text("Lặp lại") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Repeat Option")
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Không lặp lại") },
                    onClick = {
                        viewModel.onEvent(TaskEvent.SetRepeatOption(RepeatOption.NONE))
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Lặp lại hằng ngày") },
                    onClick = {
                        viewModel.onEvent(TaskEvent.SetRepeatOption(RepeatOption.DAILY))
                        expanded = false
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Chọn giờ thực hiện
        val timeDialogState = rememberMaterialDialogState()
        Button(onClick = {
            timeDialogState.show()
        }) {
            Text(text = "Pick time")
        }
        Text(text = state.time.toString())
        Spacer(modifier = Modifier.height(8.dp))

        MaterialDialog(
            dialogState = timeDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                }
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.now(),
                title = "Pick a time"
            ) {
                viewModel.onEvent(TaskEvent.SetTime(it))
            }
        }
        val startDateDialogState = rememberMaterialDialogState()
        Button(onClick = {
            startDateDialogState.show()
        }) {
            Text(text = "Pick start date")
        }
        Text(text = state.startDate.toString())
        MaterialDialog(
            dialogState = startDateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
                allowedDateValidator = {
                    it.dayOfMonth % 2 == 1
                }
            ) {
                viewModel.onEvent(TaskEvent.SetStartDate(it))
            }
        }
        // Chọn ngày bắt đầu và kết thúc (chỉ hiển thị khi lặp lại)
        if (state.repeatOption == RepeatOption.DAILY) {

            val endDateDialogState = rememberMaterialDialogState()

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                endDateDialogState.show()
            }) {
                Text(text = "Pick end date")
            }
            Text(text = state.endDate.toString())

            MaterialDialog(
                dialogState = endDateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                    }
                    negativeButton(text = "Cancel")
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date",
                    allowedDateValidator = {
                        it.dayOfMonth % 2 == 1
                    }
                ) {
                    viewModel.onEvent(TaskEvent.SetEndDate(it))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}