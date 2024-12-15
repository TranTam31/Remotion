
import com.example.hope.reminder.data.RepeatOption
import com.example.hope.reminder.data.database.Task
import com.example.hope.reminder.data.database.TaskDao
import com.example.hope.reminder.data.database.TaskDay
import com.example.hope.reminder.data.repository.TaskRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDate


class TaskTest {
    private lateinit var taskDao: TaskDao
    private lateinit var taskRepository: TaskRepository

    @Before
    fun setUp() {
        taskDao = mock(TaskDao::class.java)
        taskRepository = mock(TaskRepository::class.java)
    }
    @Test
    fun testTask() {
        val task = Task(
            taskId = 1,
            title = "title",
            repeatOption = RepeatOption.DAILY,
            startDate = null,
            endDate = null
        )
        assertEquals("title", task.title)
        assertEquals(RepeatOption.DAILY, task.repeatOption)
    }

    @Test
    fun getTaskDayById() = runTest {
        val taskDay = TaskDay(
            taskDayId = 1,
            taskId = 1,
            date = LocalDate.now(),
            time = null,
            content = "content",
            isCompleted = false
        )
        `when`(taskDao.getTaskDayById(taskDay.taskDayId)).thenReturn(taskDay)
        val result = taskDao.getTaskDayById(taskDay.taskDayId)
        assertEquals(taskDay, result)
    }
    @Test
    fun getTaskById() = runTest {
        val task = Task(
            taskId = 1,
            title = "title",
            repeatOption = RepeatOption.DAILY,
            startDate = null,
            endDate = null
        )
        `when`(taskDao.getTaskById(task.taskId)).thenReturn(task)
        val result = taskDao.getTaskById(task.taskId)
        assertEquals(task, result)
    }
}