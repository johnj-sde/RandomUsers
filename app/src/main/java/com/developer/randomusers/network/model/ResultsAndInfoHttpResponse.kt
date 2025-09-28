package com.developer.randomusers.network.model

import com.google.gson.annotations.SerializedName

data class ResultsAndInfoHttpResponse (

  @SerializedName("results" ) var userHttpResponses : List<UserHttpResponse> = emptyList(),
  @SerializedName("info"    ) var infoHttpResponse    : InfoHttpResponse?    = InfoHttpResponse()

)