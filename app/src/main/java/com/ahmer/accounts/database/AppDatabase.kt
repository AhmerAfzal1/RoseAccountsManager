package com.ahmer.accounts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.dao.UserDao
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.UserModel

@Database(entities = [UserModel::class, TransModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adminDao(): AdminDao
    abstract fun transDao(): TransDao
    abstract fun usersDao(): UserDao
}