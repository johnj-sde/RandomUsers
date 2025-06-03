package com.developer.randomusers.network.model

import kotlinx.serialization.Serializable

@Serializable
data class DateOfBirthHttpResponse (
  val date : String? = null,
  val age  : Int?    = null
)