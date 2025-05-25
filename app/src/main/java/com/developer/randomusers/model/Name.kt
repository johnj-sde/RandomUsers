package com.developer.randomusers.model

import com.developer.randomusers.database.model.NameEntity
import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null
)

fun Name.toNameEntity(): NameEntity {
    return NameEntity(
        title = this.title,
        first = this.first,
        last = this.last
    )
}