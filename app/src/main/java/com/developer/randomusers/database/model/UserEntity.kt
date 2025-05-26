package com.developer.randomusers.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.developer.randomusers.model.DateOfBirth
import com.developer.randomusers.model.Id
import com.developer.randomusers.model.Login
import com.developer.randomusers.model.Name
import com.developer.randomusers.model.Picture

@Entity
data class UserEntity(
    @PrimaryKey @Embedded val id: Id,
    @ColumnInfo(name = "gender") val gender: String? = null,
    @Embedded val name: Name? = null,
    //  @Embedded val location   : Location?   = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @Embedded val login: Login? = Login(),
    @Embedded val dateOfBirth: DateOfBirth? = DateOfBirth(),
    //  @Embedded val registered : Registered? = Registered(),
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "cell") val cell: String? = null,
    @Embedded val picture: Picture? = Picture(),
    @ColumnInfo(name = "nationality") val nationality: String? = null

)

fun UserEntity.getFullName(): String {

    return name?.let { titledName ->
        titledName.last?.let { lastName ->
            if (titledName.title == null && titledName.first == null) {
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