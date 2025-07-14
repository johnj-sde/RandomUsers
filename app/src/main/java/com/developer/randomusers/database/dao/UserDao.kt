package com.developer.randomusers.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.developer.randomusers.database.model.DeletedIdEntity
import com.developer.randomusers.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity")
    fun getAll(): Flow<List<UserEntity>>

    @Insert(onConflict = IGNORE)
    fun insertAll(users: List<UserEntity>)

    @Query("DELETE FROM userentity WHERE identity_name =:name AND identity_value =:value")
    fun deleteUserById(name: String, value: String)

    @Insert
    fun insertDeletedId(id: DeletedIdEntity)

    @Query("SELECT * FROM deletedidentity WHERE identity_name =:name AND identity_value =:value")
    fun findDeletedId(name: String, value: String): List<DeletedIdEntity>

}