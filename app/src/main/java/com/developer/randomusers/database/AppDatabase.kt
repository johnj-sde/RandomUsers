package com.developer.randomusers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.database.dao.UserDao
import com.developer.randomusers.database.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), AppDatabaseInterface {
    abstract override fun userDao(): UserDao
}