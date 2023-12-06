package com.ahmer.accounts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.CategoryDao
import com.ahmer.accounts.database.dao.ExpenseDao
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.entity.CategoryEntity
import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.entity.TransEntity

@Database(
    entities = [
        CategoryEntity::class,
        ExpenseEntity::class,
        PersonsEntity::class,
        TransEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adminDao(): AdminDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun personDao(): PersonDao
    abstract fun transDao(): TransDao
}