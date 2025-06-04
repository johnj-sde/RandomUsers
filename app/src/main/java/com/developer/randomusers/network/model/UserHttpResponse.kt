package com.developer.randomusers.network.model

import com.developer.randomusers.database.model.UserEntity
import com.google.gson.annotations.SerializedName


data class UserHttpResponse (

    @SerializedName("gender"     ) val gender     : String?     = null,
    @SerializedName("name"       ) val nameHttpResponse       : NameHttpResponse?       = null,
    @SerializedName("location"   ) val locationHttpResponse   : LocationHttpResponse?   = null,
    @SerializedName("email"      ) val email      : String?     = null,
    @SerializedName("login"      ) val loginHttpResponse      : LoginHttpResponse?      = LoginHttpResponse(),
    @SerializedName("dob"        ) val dob        : DateOfBirthHttpResponse?        = DateOfBirthHttpResponse(),
    @SerializedName("registered" ) val registeredHttpResponse : RegisteredHttpResponse? = RegisteredHttpResponse(),
    @SerializedName("phone"      ) val phone      : String?     = null,
    @SerializedName("cell"       ) val cell       : String?     = null,
    @SerializedName("id"         ) val id         : IdHttpResponse,
    @SerializedName("picture"    ) val pictureHttpResponse    : PictureHttpResponse?    = PictureHttpResponse(),
    @SerializedName("nat"        ) val nat        : String?     = null

)

fun UserHttpResponse.toUserEntity(): UserEntity {
  return UserEntity(
      id = this.id,
      gender = this.gender,
      nameHttpResponse = this.nameHttpResponse,
      email = this.email,
      loginHttpResponse = this.loginHttpResponse,
      dateOfBirth = this.dob,
      phone = this.phone,
      cell = this.cell,
      pictureHttpResponse = this.pictureHttpResponse,
      nationality = this.nat
  )
}
