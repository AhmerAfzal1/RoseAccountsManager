package com.rose.account.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rose.account.database.dao.AdminDao
import com.rose.account.database.dao.UsersDao
import com.rose.account.database.model.UserModel

@Database(entities = [UserModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun adminDao(): AdminDao
    abstract fun usersDao(): UsersDao
}