package com.developer.randomusers.repository

interface MqttEventRepositoryInterface {

    fun connectAndBind()

    fun disconnectFromBroker()
}