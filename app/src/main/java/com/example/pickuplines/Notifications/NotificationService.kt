package com.example.pickuplines.Notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scheduleNotification(this)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
