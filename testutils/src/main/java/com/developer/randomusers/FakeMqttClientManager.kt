package com.developer.randomusers

import com.developer.randomusers.model.MqttState
import com.developer.randomusers.network.MqttClientManagerInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMqttClientManager(
    val flowOfMqttState: Flow<MqttState>
): MqttClientManagerInterface {

    override fun connectAndSubscribe(): Flow<MqttState> {
        return flowOfMqttState
    }

    override fun publish(payload: String, qos: Int) {
        println("publishing $payload in publish function of ${FakeMqttClientManager::class.java}")
    }

}