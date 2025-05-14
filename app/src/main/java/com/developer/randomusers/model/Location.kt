package com.developer.randomusers.model

import com.google.gson.annotations.SerializedName


data class Location (

  @SerializedName("street"      ) val street      : Street?      = Street(),
  @SerializedName("city"        ) val city        : String?      = null,
  @SerializedName("state"       ) val state       : String?      = null,
  @SerializedName("country"     ) val country     : String?      = null,
  @SerializedName("postcode"    ) val postcode    : Int?         = null,
  @SerializedName("coordinates" ) var coordinates : Coordinates? = Coordinates(),
  @SerializedName("timezone"    ) var timezone    : Timezone?    = Timezone()

)


fun Location.displayLocation(): String? {
  return this.country?.let { country ->
    this.state?.let {  state ->
      this.city?.let { city ->
        this.street?.let { street ->
          this.postcode?.let { postcode ->
            "$street\n$city, $state $postcode\n$country"
          } ?:  "$street\n$city $state\n$country"
        } ?: "$city, $state \n$country"
      } ?: "$state, $country"
    } ?: country
  }
}