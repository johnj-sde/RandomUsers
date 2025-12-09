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
    private var isBound = false

    // Defines callbacks for service binding, passed to bindService()
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MqttForegroundService.MqttBinder
            mqttClientManager = binder.getClientManager()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mqttClientManager = null
            isBound = false
        }
    }

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

    private fun startMqttService() {
        val serviceIntent = Intent(context, MqttForegroundService::class.java)

        // Android O (API 26) and higher require startForegroundService()
        // if the service is going to run in the foreground.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

     override fun disconnectFromBroker() {

        unbindService()

        val intent = Intent(context, MqttForegroundService::class.java)
        context.stopService(intent)
     }

    private fun unbindService() {
        if (isBound) {
            context.unbindService(connection)

            isBound = false
            mqttClientManager = null
        }
    }
}