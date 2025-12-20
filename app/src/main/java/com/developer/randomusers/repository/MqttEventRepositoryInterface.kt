package com.developer.randomusers.repository

import kotlinx.coroutines.flow.Flow

interface MqttEventRepositoryInterface {

    fun connectAndSubscribe(): Flow<String>

    fun publishCommand(payload: String, qos: Int = 1)
}