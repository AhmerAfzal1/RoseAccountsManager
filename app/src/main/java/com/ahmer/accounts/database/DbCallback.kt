package com.ahmer.accounts.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.dao.UserDao
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class DbCallback(
    private val userDao: Provider<UserDao>,
    private val transDao: Provider<TransDao>
) : RoomDatabase.Callback() {
    private val mScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val mUserModelLists = listOf(
            UserModel(
                name = "Ahmer Afzal",
                address = "Street No. 1, House No. 548, Darbar Road",
                phone = "03023339589",
                email = "ahmerafzal@yahoo.com",
                notes = ""
            ),
            UserModel(
                name = "Rida Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Maham Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Arfa Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Umar Riaz",
                address = "Dharanwala",
                phone = "03005095665",
                email = "umarriaz665@gmail.com",
                notes = ""
            ),
            UserModel(
                name = "Sajjad Hussain Bhutta",
                address = "Street No. 2, House No. 547",
                phone = "03024159211",
                email = "sajjad.bhutta@live.com",
                notes = ""
            ),
            UserModel(
                name = "Imtiaz Bhutta",
                address = "",
                phone = "03014652092",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Ijaz Bhutta",
                address = "",
                phone = "03024585268",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Abbas Bhutta",
                address = "",
                phone = "03002039589",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Yasir Shahid",
                address = "",
                phone = "03057039270",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Faisal Shahid",
                address = "",
                phone = "03117511575",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Adil Shahid",
                address = "",
                phone = "03106900089",
                email = "",
                notes = ""
            ),
            UserModel(
                name = "Umar Farooq",
                address = "Chak No. 63f, Hasilpur",
                phone = "03012882943",
                email = "",
                notes = "Computer Operator at Bajwa's Collection"
            ),
        )
        val mTransModelLists = listOf(
            TransModel(
                userId = 1,
                date = "07 Sep 2023",
                type = "Credit",
                description = "",
                amount = "563.5"
            ),
            TransModel(
                userId = 1,
                date = "08 Sep 2023",
                type = "Credit",
                description = "Add",
                amount = "1503.55"
            ),
            TransModel(
                userId = 1,
                date = "09 Sep 2023",
                type = "Debit",
                description = "",
                amount = "203.5"
            ),
            TransModel(
                userId = 2,
                date = "06 Sep 2023",
                type = "Credit",
                description = "",
                amount = "874.5"
            ),
            TransModel(
                userId = 2,
                date = "07 Sep 2023",
                type = "Debit",
                description = "Minus",
                amount = "325.54"
            ),
            TransModel(
                userId = 3,
                date = "07 Sep 2023",
                type = "Credit",
                description = "",
                amount = "413.5"
            ),
        )
        mScope.launch(Dispatchers.IO) {
            mUserModelLists.forEach { userDao.get().insertOrUpdate(it) }
            mTransModelLists.forEach { transDao.get().insertOrUpdate(it) }
        }
    }
}