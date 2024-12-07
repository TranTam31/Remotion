package com.example.hope.reminder.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AlarmActivity : ComponentActivity() {

    private var taskId: Long = -1
    private var title: String? = null
    private var content: String? = null
    private var time: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d("Alarm Screen", "có chạy không nè")
            AlarmScreen()
        }

        // Nhận dữ liệu từ intent
        taskId = intent.getLongExtra("taskId", -1)
        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")
        time = intent.getStringExtra("time")
    }

    @Composable
    fun AlarmScreen() {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title.orEmpty(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = content.orEmpty(), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = time.orEmpty(), fontSize = 18.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                finish()  // Đóng activity (tắt báo thức)
            }) {
                Text(text = "Dismiss")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                snoozeAlarm(context)
            }) {
                Text(text = "Snooze")
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun snoozeAlarm(context: Context) {
        val snoozeTime = 10 * 60 * 1000L // 10 phút (có thể lấy từ cài đặt)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TaskAlarmReceiver::class.java).apply {
            putExtra("taskId", taskId)
            putExtra("title", title)
            putExtra("content", content)
            putExtra("time", time)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Tính toán thời gian mới
        val snoozeTimeInMillis = System.currentTimeMillis() + snoozeTime

        // Đặt báo thức lại sau thời gian snooze
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            snoozeTimeInMillis,
            pendingIntent
        )

        finish()  // Đóng activity sau khi đặt lại báo thức
    }
}
