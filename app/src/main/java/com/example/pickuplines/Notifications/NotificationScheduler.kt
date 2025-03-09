package com.example.pickuplines.Notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun scheduleNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val triggerTime = System.currentTimeMillis() // Trigger immediately
    val interval = 2 * 60 * 60 * 1000L // 2 hours in milliseconds

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        interval,
        pendingIntent
    )
}
