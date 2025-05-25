package com.developer.randomusers.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NameEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String? = null,
    val first: String? = null,
    val last: String? = null
)