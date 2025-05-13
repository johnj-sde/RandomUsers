package com.developer.randomusers.model

import com.google.gson.annotations.SerializedName


data class ResultsAndInfo (

  @SerializedName("results" ) var results : ArrayList<Result> = arrayListOf(),
  @SerializedName("info"    ) var info    : Info?              = Info()

)