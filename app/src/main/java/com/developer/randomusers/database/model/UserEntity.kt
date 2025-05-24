package com.developer.randomusers.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.developer.randomusers.model.Dob
import com.developer.randomusers.model.Id
import com.developer.randomusers.model.Location
import com.developer.randomusers.model.Login
import com.developer.randomusers.model.Name
import com.developer.randomusers.model.Picture
import com.developer.randomusers.model.Registered

@Entity
data class UserEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="gender"     ) val gender     : String?     = null,
//    @ColumnInfo(name="name"       ) val name       : Name?       = null,
//    @ColumnInfo(name="location"   ) val location   : Location?   = null,
    @ColumnInfo(name="email"      ) val email      : String?     = null,
 //   @ColumnInfo(name="login"      ) val login      : Login?      = Login(),
 //   @ColumnInfo(name="dob"        ) val dob        : Dob?        = Dob(),
 //   @ColumnInfo(name="registered" ) val registered : Registered? = Registered(),
    @ColumnInfo(name="phone"      ) val phone      : String?     = null,
    @ColumnInfo(name="cell"       ) val cell       : String?     = null,
 //   @ColumnInfo(name="id"         ) val id         : Id,
  //  @ColumnInfo(name="picture"    ) val picture    : Picture?    = Picture(),
    @ColumnInfo(name="nat"        ) val nat        : String?     = null

)