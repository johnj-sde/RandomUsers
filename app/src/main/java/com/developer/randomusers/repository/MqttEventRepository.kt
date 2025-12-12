package com.developer.randomusers.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.developer.randomusers.network.MqttClientManager
import com.developer.randomusers.service.MqttForegroundService

class MqttEventRepository(private val context: Context): MqttEventRepositoryInterface {

    private var mqttClientManager: MqttClientManager? = null

    override fun connectAndBind() {
        val intent = Intent(context, MqttForegroundService::class.java)
        if (MqttForegroundService.IS_RUNNING) {
            if (!isBound) {
                context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
            return
        }
        startMqttService()
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun publishCommand(payload: String, qos: Int) {
        if (isBound && mqttClientManager != null) {
            mqttClientManager?.publish(payload, qos)
        } else {
            // Handle case where service is not yet bound (e.g., buffer the message or log error)
            // If the connection is not yet bound, you must call connectAndBind() first.
        }
    }

     override fun disconnectFromBroker() {

        unbindService()

        val intent = Intent(context, MqttForegroundService::class.java)
        context.stopService(intent)
     }
}