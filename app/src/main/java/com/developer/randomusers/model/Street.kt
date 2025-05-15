package com.developer.randomusers.model

import com.google.gson.annotations.SerializedName


data class Street (

  @SerializedName("number" ) val number : Int?    = null,
  @SerializedName("name"   ) val name   : String? = null

)