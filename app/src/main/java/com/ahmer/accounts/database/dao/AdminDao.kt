package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface AdminDao {

    /**
     * Executes a SQLite checkpoint operation using a raw query.
     * Typically used for Write-Ahead Logging (WAL) mode maintenance.
     *
     * @param query The raw SQLite query to execute for checkpointing
     * @return Integer result code from SQLite (0 = success, non-zero = error code)
     */
    @RawQuery
    fun executeCheckpoint(query: SupportSQLiteQuery): Int
}