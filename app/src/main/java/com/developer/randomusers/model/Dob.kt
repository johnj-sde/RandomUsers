package com.developer.randomusers.model

import kotlinx.serialization.Serializable

@Serializable
data class Dob (
  val date : String? = null,
  val age  : Int?    = null
)