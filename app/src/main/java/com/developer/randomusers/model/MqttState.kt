package com.developer.randomusers.model

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken

sealed class MqttState {
    data class NewMessageReceived(val message: String): MqttState()
    data object MqttConnectionLost: MqttState()
    data class DeliveryComplete(val token: IMqttDeliveryToken): MqttState()
    data object ConnectionUninitiated: MqttState()
}