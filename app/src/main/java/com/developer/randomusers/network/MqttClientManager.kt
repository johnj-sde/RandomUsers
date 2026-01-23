package com.developer.randomusers.network

import android.util.Log
import com.developer.randomusers.model.MqttState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage


const val BROKER_URI = "tcp://broker.emqx.io:1883"

class MqttClientManager(): MqttClientManagerInterface {

    companion object {
        const val QUIESCE_TIMEOUT = 1000L
    }

    val TAG = MqttClientManager::class.java.toString()

    private val clientId = MqttClient.generateClientId()
    private val mqttClient = MqttAsyncClient(BROKER_URI, clientId, null)
    val topic = "randomusers/${clientId}/topic"

    private fun connectMqtt() {
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
                        Log.d(TAG, "MQTT connection success")
                        subscribeToTopic()
                    }

                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                        // 2. CONNECTION FAILED: Handle retry logic here
                        Log.e(TAG, "MQTT connection failed", exception)
                    }
                })
            } catch (e: MqttException) {
                Log.e(TAG, "MqttException during connect", e)
            }
        }
    }

    private fun subscribeToTopic() {
        val topics = arrayOf(topic)
        val qos = intArrayOf(1)

        try {
            mqttClient.subscribe(topics, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.d(TAG, "MQTT client subscribed successfully to topics")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e(TAG, "MQTT subscription failed", exception)
                }
            })
        } catch (e: MqttException) {
            Log.e(TAG, "MqttException during subscribe", e)
        }
    }

    override fun publish(payload: String, qos: Int) {
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

    private fun unsubscribeFromTopic(){
        mqttClient.unsubscribe(topic)
    }

    private fun disconnect() {
        if (!mqttClient.isConnected) return
        unsubscribeFromTopic()
        mqttClient.disconnect(QUIESCE_TIMEOUT, null, object: IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(TAG, "MQTT disconnect Succeeded")
            }

            override fun onFailure(
                asyncActionToken: IMqttToken?,
                exception: Throwable?
            ) {
                Log.e(TAG, "MQTT disconnect failed", exception)
            }
        })

    }

    override fun connectAndSubscribe(): Flow<MqttState> = callbackFlow {
        val callback = object : MqttCallback {
            override fun messageArrived(t: String?, message: MqttMessage) {
                val payloadBytes = message.payload
                Log.d(TAG, "received MQTT message \"${String(payloadBytes)}\" in MqttCallback")
                val messageString = String(payloadBytes)
                trySend(MqttState.NewMessageReceived(messageString))
            }
            override fun connectionLost(cause: Throwable?) {
                trySend(MqttState.MqttConnectionLost)
                close(cause)
            }
            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                token?.let {
                    trySend(MqttState.DeliveryComplete(it))
                }
            }
        }
        mqttClient.setCallback(callback)
        connectMqtt()

        awaitClose {
            disconnect()
        }
    }

}