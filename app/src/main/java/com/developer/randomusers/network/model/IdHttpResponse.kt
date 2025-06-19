package com.developer.randomusers.network.model

import com.developer.randomusers.database.model.IdEntity
import com.developer.randomusers.model.Id
import com.google.gson.annotations.SerializedName


data class IdHttpResponse (
  @SerializedName("name"  ) val name  : String,
  @SerializedName("value" ) val value : String
)

fun IdHttpResponse.toIdEntity(): IdEntity {
  return IdEntity(
    name = name,
    value = value
  )
}