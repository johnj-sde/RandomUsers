package com.developer.randomusers.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.developer.randomusers.database.model.UserEntity
import com.developer.randomusers.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity")
    fun getAll(): Flow<List<UserEntity>>

    @Insert
    fun insertAll(users: List<UserEntity>)

}