package com.ahmer.accounts.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class DbCallback(
    private val personDao: Provider<PersonDao>,
    private val transDao: Provider<TransDao>
) : RoomDatabase.Callback() {
    private val mScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val mPersonsEntityLists = listOf(
            PersonsEntity(
                name = "Ahmer Afzal",
                address = "Street No. 1, House No. 548, Darbar Road",
                phone = "03023339589",
                email = "ahmerafzal@yahoo.com",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Rida Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Maham Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Arfa Hasan",
                address = "Street No. 2",
                phone = "",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Umar Riaz",
                address = "Dharanwala",
                phone = "03005095665",
                email = "umarriaz665@gmail.com",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Sajjad Hussain Bhutta",
                address = "Street No. 2, House No. 547",
                phone = "03024159211",
                email = "sajjad.bhutta@live.com",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Imtiaz Bhutta",
                address = "",
                phone = "03014652092",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Ijaz Bhutta",
                address = "",
                phone = "03024585268",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Abbas Bhutta",
                address = "",
                phone = "03002039589",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Yasir Shahid",
                address = "",
                phone = "03057039270",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Faisal Shahid",
                address = "",
                phone = "03117511575",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Adil Shahid",
                address = "",
                phone = "03106900089",
                email = "",
                notes = "",
                balance = "0",
            ),
            PersonsEntity(
                name = "Umar Farooq",
                address = "Chak No. 63f, Hasilpur",
                phone = "03012882943",
                email = "",
                notes = "Computer Operator at Bajwa's Collection",
                balance = "0",
            ),
        )
        val mTransEntityLists = listOf(
            TransEntity(
                personId = 1,
                date = "07 Sep 2023",
                type = "Credit",
                description = "",
                amount = "563.5"
            ),
            TransEntity(
                personId = 1,
                date = "08 Sep 2023",
                type = "Credit",
                description = "Add",
                amount = "1503.55"
            ),
            TransEntity(
                personId = 1,
                date = "09 Sep 2023",
                type = "Debit",
                description = "",
                amount = "203.5"
            ),
            TransEntity(
                personId = 2,
                date = "06 Sep 2023",
                type = "Credit",
                description = "",
                amount = "874.5"
            ),
            TransEntity(
                personId = 2,
                date = "07 Sep 2023",
                type = "Debit",
                description = "Minus",
                amount = "325.54"
            ),
            TransEntity(
                personId = 3,
                date = "07 Sep 2023",
                type = "Credit",
                description = "",
                amount = "413.5"
            ),
            TransEntity(
                personId = 1,
                date = "11 Oct 2023",
                type = "Debit",
                description = "",
                amount = "784.56"
            ),
            TransEntity(
                personId = 1,
                date = "12 Oct 2023",
                type = "Credit",
                description = "",
                amount = "147.16"
            ),
            TransEntity(
                personId = 4,
                date = "10 Oct 2023",
                type = "Credit",
                description = "",
                amount = "523.51"
            ),
            TransEntity(
                personId = 4,
                date = "10 Oct 2023",
                type = "Debit",
                description = "",
                amount = "213.15"
            ),
        )
        mScope.launch(Dispatchers.IO) {
            mPersonsEntityLists.forEach { personDao.get().insertOrUpdate(personsEntity = it) }
            mTransEntityLists.forEach { transDao.get().insertOrUpdate(transEntity = it) }
        }
    }
}