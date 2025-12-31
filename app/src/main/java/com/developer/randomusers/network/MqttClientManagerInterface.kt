package com.developer.randomusers.network

import com.developer.randomusers.model.MqttState
import kotlinx.coroutines.flow.Flow

interface MqttClientManagerInterface {

    fun connectAndSubscribe(): Flow<MqttState>

    fun publish(payload: String, qos: Int = 1)
}