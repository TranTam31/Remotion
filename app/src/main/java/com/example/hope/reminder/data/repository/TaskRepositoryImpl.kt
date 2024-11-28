package com.example.hope.reminder.data.repository

import com.example.hope.reminder.data.database.Task
import com.example.hope.reminder.data.database.TaskDao
import com.example.hope.reminder.data.database.TaskOccurrence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return flow {
            emit(taskDao.getAllTasks()) // Emit danh sách Task từ DAO
        }
    }

    override suspend fun getOccurrencesByDate(date: LocalDate): List<TaskOccurrence> {
        return taskDao.getOccurrencesByDate(date)
    }

    override suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    override suspend fun insertTaskOccurrence(taskOccurrence: TaskOccurrence) {
        taskDao.insertTaskOccurrence(taskOccurrence)
    }
}