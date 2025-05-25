package com.developer.randomusers.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["name","value"])
data class IdEntity(
    @ColumnInfo("name"  ) val name  : String,
    @ColumnInfo("value" ) val value : String
)