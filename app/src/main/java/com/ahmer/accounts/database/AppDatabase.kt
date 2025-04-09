package com.ahmer.accounts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.dao.TransactionDao
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.entity.TransactionEntity

/**
 * The main database for the application, serving as the central access point to the persisted data.
 * This database includes tables for persons and transactions, and provides DAOs for accessing and
 * managing this data.
 *
 * @property adminDao Provides access methods for administrative operations.
 * @property personDao Provides access methods for person-related operations.
 * @property transactionDao Provides access methods for transaction-related operations.
 */
@Database(
    entities = [
        PersonEntity::class,
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Retrieves the Data Access Object (DAO) for administrative operations.
     *
     * @return An instance of [AdminDao] for performing administrative tasks.
     */
    abstract fun adminDao(): AdminDao

    /**
     * Retrieves the Data Access Object (DAO) for person-related operations.
     *
     * @return An instance of [PersonDao] for managing person data.
     */
    abstract fun personDao(): PersonDao

    /**
     * Retrieves the Data Access Object (DAO) for transaction-related operations.
     *
     * @return An instance of [TransactionDao] for managing transaction data.
     */
    abstract fun transactionDao(): TransactionDao
}