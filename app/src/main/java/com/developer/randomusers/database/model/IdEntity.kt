package com.developer.randomusers.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.developer.randomusers.model.Id

@Entity(
    primaryKeys = ["identity_name", "identity_value"]
)
data class IdEntity(
    @ColumnInfo(name = "identity_name") val name: String,
    @ColumnInfo(name = "identity_value") val value: String
)

fun IdEntity.toId(): Id {
    return Id(
        name = name,
        value = value
    )
}