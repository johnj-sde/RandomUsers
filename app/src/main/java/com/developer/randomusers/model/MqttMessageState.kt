package com.developer.randomusers.model

data class MqttMessageState(
    val message: String,
    val isConnectionUninitiated: Boolean
)
