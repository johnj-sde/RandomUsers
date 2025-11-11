package com.developer.randomusers

import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.database.dao.UserDao

class FakeAppDatabase(val userDao: FakeUserDao): AppDatabaseInterface {
    override fun userDao(): UserDao {
        return userDao
    }
}