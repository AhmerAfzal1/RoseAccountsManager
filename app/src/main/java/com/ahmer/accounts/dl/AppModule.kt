package com.ahmer.accounts.dl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmer.accounts.database.AppDatabase
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.dao.TransactionDao
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.database.repository.PersonRepositoryImp
import com.ahmer.accounts.database.repository.TransactionRepository
import com.ahmer.accounts.database.repository.TransactionRepositoryImp
import com.ahmer.accounts.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for providing application-level dependencies using Dagger Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the application database instance.
     *
     * @param context Application context used for creating the database.
     * @return The Room database instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        //import javax.inject.Provider
        //personProvider: Provider<PersonDao>, transactionProvider: Provider<TransactionDao>
    ): AppDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        klass = AppDatabase::class.java,
        name = Constants.DB_NAME,
    ).setJournalMode(journalMode = RoomDatabase.JournalMode.TRUNCATE) //For backup in single file
        .fallbackToDestructiveMigration(dropAllTables = true)
        //.addCallback(callback = DbCallback(personDao = personProvider, transDao = transactionProvider))
        .build()


    /**
     * Provides a DataStore instance for storing preferences.
     *
     * @param context Application context used to access preferences.
     * @return The DataStore instance for preferences.
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(name = Constants.DATA_STORE_NAME)
        }

    /**
     * Provides the AdminDao instance.
     *
     * @param database The Room database instance.
     * @return The AdminDao instance.
     */
    @Provides
    @Singleton
    fun provideAdminDao(database: AppDatabase): AdminDao = database.adminDao()

    /**
     * Provides the PersonDao instance.
     *
     * @param database The Room database instance.
     * @return The PersonDao instance.
     */
    @Provides
    @Singleton
    fun providePersonDao(database: AppDatabase): PersonDao = database.personDao()

    /**
     * Provides the TransactionDao instance.
     *
     * @param database The Room database instance.
     * @return The TransactionDao instance.
     */
    @Provides
    @Singleton
    fun provideTransactionDao(database: AppDatabase): TransactionDao = database.transactionDao()

    /**
     * Provides the PersonRepository instance.
     *
     * @param personDao The PersonDao instance.
     * @return The PersonRepository instance.
     */
    @Provides
    @Singleton
    fun providePersonRepository(personDao: PersonDao): PersonRepository =
        PersonRepositoryImp(personDao = personDao)

    /**
     * Provides the TransactionRepository instance.
     *
     * @param transactionDao The TransactionDao instance.
     * @return The TransactionRepository instance.
     */
    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository =
        TransactionRepositoryImp(transactionDao = transactionDao)
}