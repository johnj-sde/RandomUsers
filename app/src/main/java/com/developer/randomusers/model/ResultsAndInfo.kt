package com.developer.randomusers.model

import com.google.gson.annotations.SerializedName


data class ResultsAndInfo (

  @SerializedName("results" ) var users : ArrayList<User> = arrayListOf(),
  @SerializedName("info"    ) var info    : Info?              = Info()

)