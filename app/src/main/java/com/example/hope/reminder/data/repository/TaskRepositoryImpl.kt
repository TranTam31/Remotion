package com.example.hope.reminder.data.repository

import com.example.hope.reminder.data.database.Task
import com.example.hope.reminder.data.database.TaskDao
import com.example.hope.reminder.data.database.TaskDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getAllTasks(): Flow<List<TaskDay>> {
        return taskDao.getAllTasks()
    }

    override suspend fun getTaskDayByDate(date: LocalDate): List<TaskDay> {
        return taskDao.getTaskDayByDate(date)
    }

    override suspend fun getTaskById(id: Long): Task {
        return taskDao.getTaskById(id)
    }

    override suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    override suspend fun insertTaskDay(taskDay: TaskDay) {
        taskDao.insertTaskDay(taskDay)
    }

    override suspend fun deleteTaskDay(taskDay: TaskDay) {
        taskDao.deleteTaskDay(taskDay)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    override suspend fun countTaskDaysByTaskId(taskId: Long): Int {
        return taskDao.countTaskDaysByTaskId(taskId)
    }

    override suspend fun toggleIsCompleted(taskDayId: Long) {
        return taskDao.toggleIsCompleted(taskDayId)
    }

    override suspend fun updateTaskDay(taskDay: TaskDay) {
        taskDao.updateTaskDay(taskDay)
    }
}