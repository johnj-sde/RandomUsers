package com.developer.randomusers.database

import com.developer.randomusers.database.dao.UserDao

interface AppDatabaseInterface {

    fun userDao(): UserDao

}