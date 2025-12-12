package com.developer.randomusers.repository

interface MqttEventRepositoryInterface {

    fun connectMqttClient()

    fun publishCommand(payload: String, qos: Int = 1)

    fun disconnectMqttClient()
}