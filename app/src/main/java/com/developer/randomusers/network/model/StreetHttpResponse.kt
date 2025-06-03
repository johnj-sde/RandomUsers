package com.developer.randomusers.network.model

import com.google.gson.annotations.SerializedName


data class StreetHttpResponse (

  @SerializedName("number" ) val number : Int?    = null,
  @SerializedName("name"   ) val name   : String? = null

)