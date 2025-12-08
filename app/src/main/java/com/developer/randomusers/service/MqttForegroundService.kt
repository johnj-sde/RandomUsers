package com.developer.randomusers.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.developer.randomusers.R
import com.developer.randomusers.network.MqttClientManager


class MqttForegroundService : Service() {

    companion object {
        @Volatile
        var IS_RUNNING = false
    }

    private val NOTIFICATION_ID = 101
    private val CHANNEL_ID = "MqttChannel"

    private val binder = MqttBinder()
    private lateinit var clientManager: MqttClientManager

    override fun onCreate() {
        super.onCreate()
        IS_RUNNING = true
        startForeground(NOTIFICATION_ID, createNotification())
        clientManager = MqttClientManager()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        clientManager.connectMqtt()
        return START_STICKY // Service restarts if killed
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        IS_RUNNING = false
    }

    // --- Foreground Notification ---
    private fun createNotification() =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MQTT Service")
            .setContentText("Listening for data updates...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use a proper icon
            .build().also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(CHANNEL_ID, "MQTT Connection", NotificationManager.IMPORTANCE_LOW)
                    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    manager.createNotificationChannel(channel)
                }
            }

    inner class MqttBinder : Binder() {
        fun getClientManager(): MqttClientManager = clientManager
    }
}