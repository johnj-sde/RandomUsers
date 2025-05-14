package com.developer.randomusers.model

import com.google.gson.annotations.SerializedName


data class User (

  @SerializedName("gender"     ) val gender     : String?     = null,
  @SerializedName("name"       ) val name       : Name?       = null,
  @SerializedName("location"   ) val location   : Location?   = Location(),
  @SerializedName("email"      ) val email      : String?     = null,
  @SerializedName("login"      ) val login      : Login?      = Login(),
  @SerializedName("dob"        ) val dob        : Dob?        = Dob(),
  @SerializedName("registered" ) val registered : Registered? = Registered(),
  @SerializedName("phone"      ) val phone      : String?     = null,
  @SerializedName("cell"       ) val cell       : String?     = null,
  @SerializedName("id"         ) val id         : Id         ,
  @SerializedName("picture"    ) val picture    : Picture?    = Picture(),
  @SerializedName("nat"        ) val nat        : String?     = null

)

fun User.getFullName(): String {

  return name?.let { titledName ->
    titledName.last?.let { lastName ->
      if (titledName.title==null && titledName.first==null) {
        "Anonymous"
      } else {
        titledName.first?.let { firstName ->
          titledName.title?.let { title ->
            "$title $firstName $lastName"
          } ?: "$firstName $lastName"
        } ?: titledName.title?.let { title ->
          "$title $lastName"
        } ?: "Anonymous"
      }
    } ?: "Anonymous"
  } ?: "Anonymous"
}