package com.developer.randomusers.model

import com.google.gson.annotations.SerializedName

data class Picture(
    var large     : String? = null,
    var medium    : String? = null,
    var thumbnail : String? = null
)