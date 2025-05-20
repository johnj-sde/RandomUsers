package com.developer.randomusers.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates (
  val latitude  : String? = null,
  val longitude : String? = null
)