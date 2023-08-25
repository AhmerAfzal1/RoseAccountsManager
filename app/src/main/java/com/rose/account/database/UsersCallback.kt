package com.rose.account.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rose.account.database.dao.UsersDao
import com.rose.account.database.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class UsersCallback(private val usersDao: Provider<UsersDao>) : RoomDatabase.Callback() {

    private val mScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val mUserModelLists = listOf(
            UserModel(
                name = "Ahmer Afzal",
                address = "Street No. 1, House No. 548, Darbar Road",
                phone = "03023339589",
                email = "ahmerafzal@yahoo.com",
                comments = ""
            ),
            UserModel(
                name = "Rida Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Maham Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Arfa Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Umar Riaz",
                address = "Dharanwala",
                phone = "03005095665",
                email = "umarriaz665@gmail.com",
                comments = ""
            ),
            UserModel(
                name = "Sajjad Hussain Bhutta",
                address = "Street No. 2, House No. 547",
                phone = "03024159211",
                email = "sajjad.bhutta@live.com",
                comments = ""
            ),
            UserModel(
                name = "Imtiaz Bhutta",
                address = "",
                phone = "03014652092",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Ijaz Bhutta",
                address = "",
                phone = "03024585268",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Abbas Bhutta",
                address = "",
                phone = "03002039589",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Yasir Shahid",
                address = "",
                phone = "03057039270",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Faisal Shahid",
                address = "",
                phone = "03117511575",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Adil Shahid",
                address = "",
                phone = "03106900089",
                email = "",
                comments = ""
            ),
            UserModel(
                name = "Umar Farooq",
                address = "Chak No. 63f, Hasilpur",
                phone = "03012882943",
                email = "",
                comments = "Computer Operator at Bajwa's Collection"
            ),
        )
        mScope.launch(Dispatchers.IO) {
            mUserModelLists.forEach { usersDao.get().insertOrUpdate(it) }
        }
    }
}