package com.developer.randomusers.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesHttpResponse (
  val latitude  : String? = null,
  val longitude : String? = null
)