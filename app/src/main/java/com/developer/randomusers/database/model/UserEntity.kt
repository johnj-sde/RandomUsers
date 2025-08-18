package com.developer.randomusers.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.developer.randomusers.model.User
import com.developer.randomusers.network.model.DateOfBirthHttpResponse
import com.developer.randomusers.network.model.IdHttpResponse
import com.developer.randomusers.network.model.LoginHttpResponse
import com.developer.randomusers.network.model.NameHttpResponse
import com.developer.randomusers.network.model.PictureHttpResponse
import com.developer.randomusers.network.model.toName
import com.developer.randomusers.network.model.toPicture

@Entity(
    primaryKeys = ["identity_name", "identity_value"]
)
data class UserEntity(
    @Embedded val id: IdEntity,
    @ColumnInfo(name = "gender") val gender: String? = null,
    @Embedded val nameHttpResponse: NameHttpResponse? = null,
    //  @Embedded val location   : Location?   = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @Embedded val loginHttpResponse: LoginHttpResponse? = LoginHttpResponse(),
    @Embedded val dateOfBirth: DateOfBirthHttpResponse? = DateOfBirthHttpResponse(),
    //  @Embedded val registered : Registered? = Registered(),
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "cell") val cell: String? = null,
    @Embedded val pictureHttpResponse: PictureHttpResponse? = PictureHttpResponse(),
    @ColumnInfo(name = "nationality") val nationality: String? = null,
    val wasDeleted: Boolean = false
)

fun UserEntity.toUser(): User {
    return User(
        id = this.id.toId(),
        phone = this.phone,
        email = this.email,
        picture = this.pictureHttpResponse?.toPicture(),
        name = this.nameHttpResponse?.toName(),
        gender = this.gender
    )
}