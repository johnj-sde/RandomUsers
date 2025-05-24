package com.developer.randomusers.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.developer.randomusers.database.model.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity")
    fun getAll(): List<UserEntity>

    @Insert
    fun insertAll(vararg users: UserEntity)

}