package com.example.hope.reminder.ui

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.hope.reminder.data.RepeatOption
import com.example.hope.reminder.data.database.Task
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class TaskState (
    val tasks: List<Task> = emptyList(),
    val title: String = "",
    val content: String = "",
    val repeatOption: RepeatOption = RepeatOption.NONE,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val time: LocalTime? = null,
    val isAddingTask: Boolean = false,
    val dateSelected: LocalDate = LocalDate.now()
)
