package com.developer.randomusers.model

data class User(
    val id: Id,
    val phone: String? = null,
    val email: String? = null,
    val picture: Picture? = null,
    val name: Name? = null,
    val gender: String? = null
)

fun User.getFullName(): String {

    return name?.let { titledName ->
        titledName.last?.let { lastName ->
            if (titledName.title==null && titledName.first==null) {
                "Anonymous"
            } else {
                titledName.first?.let { firstName ->
                    titledName.title?.let { title ->
                        "$title $firstName $lastName"
                    } ?: "$firstName $lastName"
                } ?: titledName.title?.let { title ->
                    "$title $lastName"
                } ?: "Anonymous"
            }
        } ?: "Anonymous"
    } ?: "Anonymous"
}

fun User.matchesName(searchText: String): Boolean{
    return this.name?.first?.lowercase()?.contains(searchText.lowercase()) == true
            ||
            this.name?.last?.lowercase()?.contains(searchText.lowercase()) == true
}