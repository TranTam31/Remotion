package com.example.hope.reminder.data.repository

import com.example.hope.reminder.data.database.Task
import com.example.hope.reminder.data.database.TaskOccurrence
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getOccurrencesByDate(date: LocalDate): List<TaskOccurrence>
    suspend fun insertTask(task: Task): Long
    suspend fun insertTaskOccurrence(taskOccurrence: TaskOccurrence)
}