package com.example.hope.reminder.data.database

import androidx.room.*
import java.time.LocalDate

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskOccurrence(taskOccurrence: TaskOccurrence)

    @Query("SELECT * FROM TaskOccurrence WHERE date = :date")
    suspend fun getOccurrencesByDate(date: LocalDate): List<TaskOccurrence>

    @Query("SELECT * FROM Task")
    suspend fun getAllTasks(): List<Task>
}