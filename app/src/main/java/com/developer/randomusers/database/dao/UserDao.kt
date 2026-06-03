package com.developer.randomusers.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.developer.randomusers.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity")
    fun getAll(): Flow<List<UserEntity>>

    @Insert(onConflict = IGNORE)
    fun insertAll(users: List<UserEntity>)

    @Query("UPDATE userentity SET wasDeleted=1 WHERE identity_name =:name AND identity_value =:value")
    fun markUserWithIdAsDeleted(name: String, value: String)

    @Query("UPDATE userentity SET isFavorite=NOT isFavorite WHERE identity_name =:name AND identity_value =:value")
    fun toggleUserWithIdAsFavorite(name: String, value: String)
}

