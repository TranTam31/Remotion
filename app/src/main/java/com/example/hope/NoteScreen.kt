package com.example.hope

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hope.ui.components.CalendarView
import com.example.hope.ui.components.JournalEntryInput
import com.example.hope.ui.components.MonthPickerDialog
import com.example.hope.ui.components.MonthSelector
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    check: (LocalDate) -> Boolean
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val today = LocalDate.now()
    var isViewingEntry by remember { mutableStateOf(false) }
    var showMonthPicker by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(NoteEvent.SetDate(today))
                    onEvent(NoteEvent.ShowDialog)
                    isViewingEntry = check(today)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .pointerInput(Unit) { // Vuốt màn hình chung để chuyển tháng
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < 0) { // Vuốt lên để chuyển tháng trước
                        currentMonth = currentMonth.minusMonths(1)
                    } else if (dragAmount > 0 && currentMonth < YearMonth.now()) { // Vuốt xuống để chuyển tháng sau
                        currentMonth = currentMonth.plusMonths(1)
                    }
                }
            }
        ) {
            Text(
                text = "Mood Tracker",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            MonthSelector(
                currentMonth = currentMonth,
                onPreviousMonth = { if (currentMonth > currentMonth.minusMonths(1)) currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { if (currentMonth < YearMonth.now()) currentMonth = currentMonth.plusMonths(1) },
                onMonthSelect = { showMonthPicker = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .nestedScroll(object : NestedScrollConnection {
                        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                            if (available.y > 0) { // Vuốt xuống
                                currentMonth = currentMonth.plusMonths(1).coerceAtMost(YearMonth.now())
                            } else if (available.y < 0) { // Vuốt lên
                                currentMonth = currentMonth.minusMonths(1)
                            }
                            return Offset.Zero
                        }
                    })
            ) {
                CalendarView(
                    check = check,
                    currentMonth = currentMonth,
                    today = today,
                    onDateSelected = { date ->
                        if (date <= today) {
                            onEvent(NoteEvent.SetDate(date))
                            onEvent(NoteEvent.ShowDialog)
                            isViewingEntry = check(date)
                        }
                    }
                )
            }

            if (showMonthPicker) {
                MonthPickerDialog(
                    initialMonth = currentMonth,
                    onMonthSelected = { selectedMonth ->
                        currentMonth = selectedMonth
                        showMonthPicker = false
                    },
                    onDismiss = { showMonthPicker = false }
                )
            }

            // Hộp thoại cho việc thêm/xem nhật ký
            if (state.isAddingNote) {
                AlertDialog(
                    onDismissRequest = { onEvent(NoteEvent.HideDialog) },
                    title = {
                        Text(text = if (isViewingEntry) "Xem nhật ký" else "Thêm nhật ký")
                    },
                    text = {
                        if (isViewingEntry) {
//                            val entry = check(today)
//                            val noteText = state.content ?: ""
                            Text(text = "Đã có note nhé")
                        } else {
                            JournalEntryInput(
                                date = state.date,
                                content = state.content,
                                onEntrySaved = {
                                    onEvent(NoteEvent.SaveNote)
                                    onEvent(NoteEvent.HideDialog)
                                },
                                onContentChange = { newContent ->
                                    onEvent(NoteEvent.SetContent(newContent))
                                }
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { onEvent(NoteEvent.HideDialog) }) {
                            Text("Đóng")
                        }
                    }
                )
            }
        }
    }
}