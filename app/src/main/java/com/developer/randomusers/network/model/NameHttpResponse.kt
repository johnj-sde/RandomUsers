package com.developer.randomusers.network.model

import com.developer.randomusers.model.Name
import kotlinx.serialization.Serializable

@Serializable
data class NameHttpResponse(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null
)

fun NameHttpResponse.toName(): Name {
    return Name(
        title = title,
        first = first,
        last = last
    )
}