package com.developer.randomusers.repository

import com.developer.randomusers.network.MqttClientManager

class MqttEventRepository(private val mqttClientManager: MqttClientManager): MqttEventRepositoryInterface {
    override fun connectMqttClient() {
        mqttClientManager.connectMqtt()
    }

    override fun publishCommand(payload: String, qos: Int) {
        mqttClientManager.publish(payload, qos)
    }

    override fun disconnectMqttClient() {
        mqttClientManager.disconnect()
    }
}