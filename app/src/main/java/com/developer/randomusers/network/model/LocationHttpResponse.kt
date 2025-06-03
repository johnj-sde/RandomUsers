package com.developer.randomusers.network.model

import com.google.gson.annotations.SerializedName

data class LocationHttpResponse (

  @SerializedName("street"      ) val streetHttpResponse      : StreetHttpResponse?      = null,
  @SerializedName("city"        ) val city        : String?      = null,
  @SerializedName("state"       ) val state       : String?      = null,
  @SerializedName("country"     ) val country     : String?      = null,
  @SerializedName("postcode"    ) val postcode    : String?         = null,
  @SerializedName("coordinates" ) var coordinatesHttpResponse : CoordinatesHttpResponse? = CoordinatesHttpResponse(),
  @SerializedName("timezone"    ) var timezoneHttpResponse    : TimezoneHttpResponse?    = TimezoneHttpResponse()

)

fun LocationHttpResponse.displayLocation(): String? {
  return this.country?.let { country ->
    this.state?.let {  state ->
      this.city?.let { city ->
        this.streetHttpResponse?.let { street ->
          if (street.name!= null && street.number!=null) {
            this.postcode?.let { postcode ->
              "${street.number} ${street.name}\n$city, $state $postcode\n$country"
            } ?:  "${street.number} ${street.name}\n$city, $state\n$country"
          } else {
            "$city, $state \n$country"
          }
        } ?: "$city, $state \n$country"
      } ?: "$state, $country"
    } ?: country
  }
}