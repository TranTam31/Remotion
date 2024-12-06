package com.example.hope.reminder.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DismissNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("taskId", -1)

        // Cập nhật trạng thái nếu cần (ví dụ đánh dấu hoàn thành)
        // Cập nhật database nếu cần, chẳng hạn set isCompleted = true cho TaskDay

        // Xóa thông báo
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(taskId.toInt())
    }
}
