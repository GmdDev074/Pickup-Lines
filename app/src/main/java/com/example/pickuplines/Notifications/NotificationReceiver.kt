package com.example.pickuplines.Notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pickuplines.Utils.getRandomPickupLine

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = "Pickup Line"
        val message = getRandomPickupLine()
        showNotification(context, title, message)
    }
}
