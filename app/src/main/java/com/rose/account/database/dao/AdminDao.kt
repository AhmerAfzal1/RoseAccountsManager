package com.rose.account.database.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface AdminDao {

    @RawQuery
    fun checkPoint(supportSQLiteQuery: SupportSQLiteQuery): Int
}