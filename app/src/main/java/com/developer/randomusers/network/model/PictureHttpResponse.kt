package com.developer.randomusers.network.model

import com.developer.randomusers.model.Picture
import com.google.gson.annotations.SerializedName


data class PictureHttpResponse (

  @SerializedName("large"     ) var large     : String? = null,
  @SerializedName("medium"    ) var medium    : String? = null,
  @SerializedName("thumbnail" ) var thumbnail : String? = null

)

fun PictureHttpResponse.toPicture(): Picture {
  return Picture(
    large = large,
    medium = medium,
    thumbnail = thumbnail
  )
}