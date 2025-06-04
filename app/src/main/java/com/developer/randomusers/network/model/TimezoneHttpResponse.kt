package com.developer.randomusers.network.model

import com.google.gson.annotations.SerializedName


data class TimezoneHttpResponse (

  @SerializedName("offset"      ) var offset      : String? = null,
  @SerializedName("description" ) var description : String? = null

)