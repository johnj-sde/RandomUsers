package com.developer.randomusers.repository

import com.developer.randomusers.model.MqttState
import kotlinx.coroutines.flow.Flow

interface MqttEventRepositoryInterface {

    fun connectAndSubscribe(): Flow<MqttState>

    fun publishCommand(payload: String, qos: Int = 1)
}