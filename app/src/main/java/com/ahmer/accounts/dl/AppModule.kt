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
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.database.repository.PersonRepositoryImp
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.database.repository.TransRepositoryImp
import com.ahmer.accounts.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
        //personsProvider: Provider<PersonDao>, transProvider: Provider<TransDao>
    ): AppDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        klass = AppDatabase::class.java,
        name = Constants.DB_NAME,
    ).setJournalMode(journalMode = RoomDatabase.JournalMode.TRUNCATE) //For backup in single file
        .fallbackToDestructiveMigration()
        //.addCallback(callback = DbCallback(personDao = personsProvider, transDao = transProvider))
        .build()

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(name = Constants.DATA_STORE_NAME)
        }

    @Provides
    @Singleton
    fun providesAdminDao(database: AppDatabase): AdminDao = database.adminDao()

    @Provides
    @Singleton
    fun providesPersonsDao(database: AppDatabase): PersonDao = database.personDao()

    @Provides
    @Singleton
    fun providesTransDao(database: AppDatabase): TransDao = database.transDao()

    @Provides
    @Singleton
    fun providesPersonRepository(personDao: PersonDao): PersonRepository =
        PersonRepositoryImp(personDao = personDao)

    @Provides
    @Singleton
    fun providesTransRepository(transDao: TransDao): TransRepository =
        TransRepositoryImp(transDao = transDao)
}