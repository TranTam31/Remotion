package com.example.hope.reminder.ui

import com.example.hope.reminder.data.RepeatOption
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEvent {
    object SaveTask : TaskEvent
    data class SetTitle(val title: String) : TaskEvent
    data class SetContent(val content: String) : TaskEvent
    data class SetRepeatOption(val repeatOption: RepeatOption) : TaskEvent
    data class SetStartDate(val startDate: LocalDate?) : TaskEvent
    data class SetEndDate(val endDate: LocalDate?) : TaskEvent
    data class SetTime(val time: LocalTime) : TaskEvent // Thời gian
    data class SetDateSelected(val dateSelected: LocalDate): TaskEvent
    object ShowDialog : TaskEvent
    object HideDialog : TaskEvent
}