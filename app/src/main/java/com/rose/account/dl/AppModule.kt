package com.rose.account.dl

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rose.account.database.AppDatabase
import com.rose.account.database.dao.AdminDao
import com.rose.account.database.dao.UserDao
import com.rose.account.database.repository.UserRepositoryImpl
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
        @ApplicationContext mContext: Context,
        //mUsersProvider: Provider<UsersDao>
    ): AppDatabase = Room.databaseBuilder(
        mContext, AppDatabase::class.java, Constants.DATABASE_NAME
    ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE) //For backup in single file
        .fallbackToDestructiveMigration()
        //.addCallback(UsersCallback(mUsersProvider))
        .build()

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext mContext: Context): PreferencesManager =
        PreferencesManager(mContext)

    @Provides
    @Singleton
    fun providesUsersDao(mDatabase: AppDatabase): UserDao = mDatabase.usersDao()

    @Provides
    @Singleton
    fun providesAdminDao(mDatabase: AppDatabase): AdminDao = mDatabase.adminDao()

    @Provides
    @Singleton
    fun providesUserRepository(mUserDao: UserDao): UserRepositoryImpl = UserRepositoryImpl(mUserDao)
}