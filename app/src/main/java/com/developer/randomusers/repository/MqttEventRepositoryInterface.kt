package com.developer.randomusers.repository

interface MqttEventRepositoryInterface {

    fun connectAndBind()

    fun disconnectFromBroker()

    fun publishCommand(payload: String, qos: Int = 1)
}