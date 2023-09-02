package com.ahmer.accounts.dl

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmer.accounts.database.AppDatabase
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.UserDao
import com.ahmer.accounts.database.repository.UserRepository
import com.ahmer.accounts.database.repository.UserRepositoryImp
import com.ahmer.accounts.preferences.PreferencesManager
import com.ahmer.accounts.usecase.user.AddUserUseCase
import com.ahmer.accounts.usecase.user.DeleteUserUseCase
import com.ahmer.accounts.usecase.user.GetAllUsersUseCase
import com.ahmer.accounts.usecase.user.GetUserByIdUseCase
import com.ahmer.accounts.usecase.user.UserUseCase
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
    fun providesUserRepository(userDao: UserDao): UserRepository = UserRepositoryImp(userDao)

    @Provides
    @Singleton
    fun providesUserUseCase(repository: UserRepository): UserUseCase {
        return UserUseCase(
            AddUserUseCase(repository),
            DeleteUserUseCase(repository),
            GetAllUsersUseCase(repository),
            GetUserByIdUseCase(repository)
        )
    }
}