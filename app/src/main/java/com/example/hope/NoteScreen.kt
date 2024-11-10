package com.example.hope

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hope.ui.components.CalendarView
import com.example.hope.ui.components.JournalEntryInput
import com.example.hope.ui.components.MonthPickerDialog
import com.example.hope.ui.components.MonthSelector
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("ReturnFromAwaitPointerEventScope")
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

    var isMonthChanged by remember { mutableStateOf(false) }
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
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        if (!isMonthChanged) { // Kiểm tra nếu chưa thay đổi tháng
                            if (dragAmount > 0) {
                                // Vuốt lên - Lùi về tháng trước
                                if (currentMonth > currentMonth.minusMonths(1)) {
                                    currentMonth = currentMonth.minusMonths(1)
                                    isMonthChanged = true // Đánh dấu đã thay đổi tháng
                                }
                            } else if (dragAmount < 0) {
                                // Vuốt xuống - Tiến tới tháng tiếp theo
                                if (currentMonth < YearMonth.now()) {
                                    currentMonth = currentMonth.plusMonths(1)
                                    isMonthChanged = true // Đánh dấu đã thay đổi tháng
                                }
                            }
                        }
                    },
                    onDragEnd = {
                        isMonthChanged = false // Reset lại sau khi kết thúc thao tác vuốt
                    }
                )
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
                    .fillMaxHeight()
                    .nestedScroll(object : NestedScrollConnection {
                        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                            if (!isMonthChanged) {
                                if (available.y > 70) { // Vuốt xuống
                                    if (currentMonth > currentMonth.minusMonths(1)) {
                                        currentMonth = currentMonth.minusMonths(1)
                                        isMonthChanged = true // Đánh dấu đã thay đổi tháng
                                    }
                                } else if (available.y < -70) { // Vuốt lên
                                    if (currentMonth < YearMonth.now()) {
                                        currentMonth = currentMonth.plusMonths(1)
                                        isMonthChanged = true // Đánh dấu đã thay đổi tháng
                                    }
                                }
                            }
                            return Offset.Zero
                        }

                        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                            isMonthChanged = false // Reset lại sau khi kết thúc thao tác vuốt
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