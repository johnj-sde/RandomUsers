package com.developer.randomusers.network

import android.util.Log
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage


const val BROKER_URI = "tcp://broker.emqx.io:1883"

class MqttClientManager() {

    val TAG = MqttClientManager::class.java.toString()

    private val clientId = MqttClient.generateClientId()
    private val mqttClient = MqttAsyncClient(BROKER_URI, clientId, null)
    val topic = "randomusers/${clientId}/topic"

    init {
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                // Reconnect logic goes here (e.g., exponential backoff)
            }
            override fun messageArrived(topic: String, message: MqttMessage) {
                val payload = message.payload
                Log.d(TAG, "received MQTT message \"${String(payload)}\" in MqttCallback")
            }
            override fun deliveryComplete(token: org.eclipse.paho.client.mqttv3.IMqttDeliveryToken?) {}
        })
    }

    fun connectMqtt() {
        val options = MqttConnectOptions()
        options.isCleanSession = true
        options.keepAliveInterval = 60 // Keep connection alive by sending PING every 60 seconds
        options.connectionTimeout = 30 // Connection timeout of 30 seconds

        if (!mqttClient.isConnected) {
            try {
                // Attempt to connect asynchronously
                val token: IMqttToken = mqttClient.connect(options, null, object :
                    IMqttActionListener {

                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        // 1. CONNECTION SUCCESSFUL: Subscribe immediately
                        subscribeToTopics()
                    }

                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                        // 2. CONNECTION FAILED: Handle retry logic here
                        Log.e(TAG, "MQTT Connection failed", exception)
                    }
                })
            } catch (e: MqttException) {
                Log.e(TAG, "MqttException during connect", e)
            }
        }
    }

    private fun subscribeToTopics() {
        val topics = arrayOf("device/data/status", topic)
        val qos = intArrayOf(1, 1)

        try {
            mqttClient.subscribe(topics, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.d(TAG, "MQTT client subscribed successfully to topics")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e(TAG, "MQTT Subscription failed", exception)
                }
            })
        } catch (e: MqttException) {
            Log.e(TAG, "MqttException during subscribe", e)
        }
    }

    fun publish(payload: String, qos: Int = 1) {
        if (mqttClient.isConnected) {
            try {
                val topic = topic
                val message = MqttMessage(payload.toByteArray())
                message.qos = 1
                mqttClient.publish(topic, message)

            } catch (e: MqttException) {
                e.printStackTrace()
                Log.e(TAG, "MQTT publish failed with ${e.message}")
            }
        } else {
            // Handle the case where you are disconnected (e.g., attempt to reconnect)
        }
    }

}