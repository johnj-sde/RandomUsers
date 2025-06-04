package com.developer.randomusers.network.model

import com.google.gson.annotations.SerializedName


data class RegisteredHttpResponse (

  @SerializedName("date" ) val date : String? = null,
  @SerializedName("age"  ) var age  : Int?    = null

)