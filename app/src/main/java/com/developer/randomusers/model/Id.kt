package com.developer.randomusers.model

import com.developer.randomusers.database.model.IdEntity
import com.google.gson.annotations.SerializedName


data class Id (

  @SerializedName("name"  ) val name  : String,
  @SerializedName("value" ) val value : String

)

fun Id.toIdEntity(): IdEntity {
  return IdEntity(
    name = this.name,
    value = this.value
  )
}