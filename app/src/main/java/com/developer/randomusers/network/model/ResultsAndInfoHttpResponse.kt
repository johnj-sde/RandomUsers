package com.developer.randomusers.network.model

import com.google.gson.annotations.SerializedName


data class ResultsAndInfoHttpResponse (

  @SerializedName("results" ) var userHttpResponses : ArrayList<UserHttpResponse> = arrayListOf(),
  @SerializedName("info"    ) var infoHttpResponse    : InfoHttpResponse?              = InfoHttpResponse()

)