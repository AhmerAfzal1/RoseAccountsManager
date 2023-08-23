package com.rose.account.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rose.account.database.dao.UsersDao
import com.rose.account.database.model.UsersModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class UsersCallback(private val mUsersDao: Provider<UsersDao>) : RoomDatabase.Callback() {

    private val mScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val mUsersModelLists = listOf(
            UsersModel(
                name = "Ahmer Afzal",
                address = "Street No. 1, House No. 548, Darbar Road",
                phone = "03023339589",
                email = "ahmerafzal@yahoo.com",
                comments = ""
            ), UsersModel(
                name = "Rida Hasan",
                address = "",
                phone = "",
                email = "",
                comments = ""
            ), UsersModel(
                name = "Maham Hasan",
                address = "",
                phone = "",
                email = "",
                comments = ""
            ), UsersModel(
                name = "Arfa Hasan",
                address = "",
                phone = "",
                email = "",
                comments = ""
            )
        )
        mScope.launch(Dispatchers.IO) {
            mUsersModelLists.forEach { mUsersDao.get().insertOrUpdate(it) }
        }
    }
}