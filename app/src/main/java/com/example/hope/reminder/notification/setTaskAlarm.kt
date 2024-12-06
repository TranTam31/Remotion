package com.example.hope.reminder.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.hope.reminder.data.database.TaskDay
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ServiceCast", "ScheduleExactAlarm")
fun setTaskAlarm(taskDay: TaskDay, context: Context) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
        putExtra("taskId", taskDay.taskId)
        putExtra("title", "Task: ${taskDay.taskId}") // Ví dụ title
        putExtra("content", taskDay.content ?: "No content")
        putExtra("time", taskDay.time?.toString() ?: "")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        taskDay.taskId.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Tính toán thời gian báo thức
    val targetTime = LocalDateTime.of(taskDay.date, taskDay.time?.withSecond(0)?.withNano(0) ?: LocalTime.NOON.withSecond(0).withNano(0))
    val delayMillis = Duration.between(LocalDateTime.now(), targetTime).toMillis()

    // Đặt alarm nếu thời gian hợp lệ
    if (delayMillis > 0) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + delayMillis,
            pendingIntent
        )
    }
}

