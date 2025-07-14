package com.developer.randomusers.database.model

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    primaryKeys = ["identity_name", "identity_value"]
)
data class DeletedIdEntity(
    @Embedded val id: IdEntity
)