package com.developer.randomusers.model

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null
)