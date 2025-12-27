package com.developer.randomusers.repository

import com.developer.randomusers.model.MqttState
import com.developer.randomusers.network.MqttClientManager
import kotlinx.coroutines.flow.Flow

class MqttEventRepository(private val mqttClientManager: MqttClientManager): MqttEventRepositoryInterface {
    override fun connectAndSubscribe(): Flow<MqttState> {
        return mqttClientManager.connectAndSubscribe()
    }

    override fun publishCommand(payload: String, qos: Int) {
        mqttClientManager.publish(payload, qos)
    }
}