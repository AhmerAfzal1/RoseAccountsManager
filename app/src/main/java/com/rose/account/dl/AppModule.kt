package com.rose.account.dl

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rose.account.database.AppDatabase
import com.rose.account.database.dao.AdminDao
import com.rose.account.database.dao.UserDao
import com.rose.account.database.repository.UserRepository
import com.rose.account.preferences.PreferencesManager
import com.rose.account.utils.Constants
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
        //usersProvider: Provider<UserDao>
    ): AppDatabase = Room.databaseBuilder(
        context.applicationContext, AppDatabase::class.java, Constants.DATABASE_NAME
    ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE) //For backup in single file
        .fallbackToDestructiveMigration()
        //.addCallback(UsersCallback(usersProvider))
        .build()

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): PreferencesManager =
        PreferencesManager(context)

    @Provides
    @Singleton
    fun providesUsersDao(database: AppDatabase): UserDao = database.usersDao()

    @Provides
    @Singleton
    fun providesAdminDao(database: AppDatabase): AdminDao = database.adminDao()

    @Provides
    @Singleton
    fun providesUserRepository(userDao: UserDao): UserRepository = UserRepository(userDao)
}