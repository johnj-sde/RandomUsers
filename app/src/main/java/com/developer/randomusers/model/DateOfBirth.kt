package com.developer.randomusers.model

import kotlinx.serialization.Serializable

@Serializable
data class DateOfBirth (
  val date : String? = null,
  val age  : Int?    = null
)