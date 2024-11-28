package com.example.hope.reminder.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hope.NoteApplication
import com.example.hope.reminder.data.RepeatOption
import com.example.hope.reminder.data.database.Task
import com.example.hope.reminder.data.database.TaskOccurrence
import com.example.hope.reminder.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _tasks = taskRepository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @RequiresApi(Build.VERSION_CODES.O)
    private val _state = MutableStateFlow(TaskState())

    @RequiresApi(Build.VERSION_CODES.O)
    val state = combine(_state, _tasks) { state, tasks ->
        state.copy(
            tasks = tasks
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    @SuppressLint("NewApi")
    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.SetTitle -> {
                _state.update { it.copy(title = event.title) }
            }
            is TaskEvent.SetContent -> {
                _state.update { it.copy(content = event.content) }
            }
            is TaskEvent.SetRepeatOption -> {
                _state.update { it.copy(repeatOption = event.repeatOption) }
            }
            is TaskEvent.SetStartDate -> {
                _state.update { it.copy(startDate = event.startDate) }
            }
            is TaskEvent.SetEndDate -> {
                _state.update { it.copy(endDate = event.endDate) }
            }
            TaskEvent.ShowDialog -> {
                _state.update { it.copy(isAddingTask = true) }
            }
            TaskEvent.HideDialog -> {
                _state.update { it.copy(isAddingTask = false) }
            }
            TaskEvent.SaveTask -> {
                val title = state.value.title
                val repeatOption = state.value.repeatOption
                val startDate = state.value.startDate
                val endDate = state.value.endDate
                val time = state.value.time
                val content = state.value.content

                if (title.isBlank() || startDate == null) return

                viewModelScope.launch {
                    val taskId = taskRepository.insertTask(
                        Task(
                            title = title,
                            repeatOption = repeatOption,
                            startDate = startDate,
                            endDate = endDate
                        )
                    )
                    // Tạo TaskOccurrence dựa trên repeatOption
                    if (repeatOption == RepeatOption.DAILY && endDate != null) {
                        var current = startDate // Bắt đầu từ ngày startDate
                        while (!current?.isAfter(endDate)!!) { // Kiểm tra nếu current chưa vượt qua endDate
                            current.let {
                                TaskOccurrence(
                                    taskId = taskId,
                                    date = it, // Giữ nguyên kiểu LocalDate
                                    time = time,
                                    content = content
                                )
                            }.let {
                                taskRepository.insertTaskOccurrence(
                                    it
                                )
                            }
                            current = current.plusDays(1) // Tăng current lên 1 ngày
                        }


                    } else {
                        taskRepository.insertTaskOccurrence(
                            TaskOccurrence(
                                taskId = taskId,
                                date = startDate,
                                time = time,
                                content = content
                            )
                        )
                    }
                }
                _state.update {
                    it.copy(
                        isAddingTask = false,
                        title = "",
                        repeatOption = RepeatOption.NONE,
                        startDate = null,
                        endDate = null,
                        time = null,
                        content = "",
                    )
                }
            }

            is TaskEvent.SetTime -> {
                _state.update { it.copy(time = event.time) }
            }

            is TaskEvent.SetDateSelected -> {
                _state.update { it.copy(dateSelected = event.dateSelected) }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTaskByDate(date: LocalDate): List<TaskOccurrence> {
        return taskRepository.getOccurrencesByDate(date)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NoteApplication)
                val taskRepository = application.container.taskRepository
                TaskViewModel(taskRepository = taskRepository)
            }
        }
    }
}