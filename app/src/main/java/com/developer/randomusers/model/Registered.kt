package com.developer.randomusers.model

import com.google.gson.annotations.SerializedName


data class Registered (

  @SerializedName("date" ) val date : String? = null,
  @SerializedName("age"  ) var age  : Int?    = null

)