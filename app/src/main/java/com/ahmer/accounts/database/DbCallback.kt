package com.ahmer.accounts.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.entity.TransEntity
import com.ahmer.accounts.utils.Constants
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
        val mPersonsEntityLists: List<PersonsEntity> by lazy {
            listOf(
                PersonsEntity(
                    name = "Ahmer Afzal",
                    address = "Street No. 1, House No. 548, Darbar Road",
                    phone = "03023339589",
                    email = "ahmerafzal@yahoo.com",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Rida Hasan",
                    address = "Street No. 2",
                    phone = "",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Maham Hasan",
                    address = "Street No. 2",
                    phone = "",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Arfa Hasan",
                    address = "Street No. 2",
                    phone = "",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Umar Riaz",
                    address = "Dharanwala",
                    phone = "03005095665",
                    email = "umarriaz665@gmail.com",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Sajjad Hussain Bhutta",
                    address = "Street No. 2, House No. 547",
                    phone = "03024159211",
                    email = "sajjad.bhutta@live.com",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Imtiaz Bhutta",
                    address = "",
                    phone = "03014652092",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Ijaz Bhutta",
                    address = "",
                    phone = "03024585268",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Abbas Bhutta",
                    address = "",
                    phone = "03002039589",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Yasir Shahid",
                    address = "",
                    phone = "03057039270",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Faisal Shahid",
                    address = "",
                    phone = "03117511575",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Adil Shahid",
                    address = "",
                    phone = "03106900089",
                    email = "",
                    notes = "",
                ),
                PersonsEntity(
                    name = "Umar Farooq",
                    address = "Chak No. 63f, Hasilpur",
                    phone = "03012882943",
                    email = "",
                    notes = "Computer Operator at Bajwa's Collection",
                ),
            )
        }
        val mTransEntityLists: List<TransEntity> by lazy {
            listOf(
                TransEntity(
                    personId = 1,
                    date = 1724673275000,
                    type = Constants.TYPE_CREDIT,
                    description = "",
                    amount = "563.5",
                    created = 1724673275000,
                ),
                TransEntity(
                    personId = 1,
                    date = 1724748875000,
                    type = Constants.TYPE_CREDIT,
                    description = "Add",
                    amount = "1503.55",
                    created = 1724748875000,
                ),
                TransEntity(
                    personId = 1,
                    date = 1724829875000,
                    type = Constants.TYPE_DEBIT,
                    description = "",
                    amount = "203.5",
                    created = 1724829875000,
                ),
                TransEntity(
                    personId = 2,
                    date = 1724924673000,
                    type = Constants.TYPE_CREDIT,
                    description = "",
                    amount = "874.5",
                    created = 1724924673000,
                ),
                TransEntity(
                    personId = 2,
                    date = 1725020076000,
                    type = Constants.TYPE_DEBIT,
                    description = "Minus",
                    amount = "325.54",
                    created = 1725020076000,
                ),
                TransEntity(
                    personId = 3,
                    date = 1725115956000,
                    type = Constants.TYPE_CREDIT,
                    description = "",
                    amount = "413.5",
                    created = 1725115956000,
                ),
                TransEntity(
                    personId = 1,
                    date = 1725173556000,
                    type = Constants.TYPE_DEBIT,
                    description = "",
                    amount = "784.56",
                    created = 1725173556000,
                ),
                TransEntity(
                    personId = 1,
                    date = 1725254556000,
                    type = Constants.TYPE_CREDIT,
                    description = "",
                    amount = "147.16",
                    created = 1725254556000,
                ),
                TransEntity(
                    personId = 4,
                    date = 1725334365000,
                    type = Constants.TYPE_CREDIT,
                    description = "",
                    amount = "523.51",
                    created = 1725334365000,
                ),
                TransEntity(
                    personId = 4,
                    date = 1725413565000,
                    type = Constants.TYPE_DEBIT,
                    description = "",
                    amount = "213.15",
                    created = 1725413565000,
                ),
                TransEntity(
                    personId = 3,
                    date = 1725484365000,
                    type = Constants.TYPE_CREDIT,
                    description = "",
                    amount = "2893.15",
                    created = 1725484365000,
                ),
                TransEntity(
                    personId = 2,
                    date = 1725646365000,
                    type = Constants.TYPE_DEBIT,
                    description = "",
                    amount = "1213.15",
                    created = 1725646365000,
                ),
                TransEntity(
                    personId = 1,
                    date = 1725726765000,
                    type = Constants.TYPE_DEBIT,
                    description = "",
                    amount = "893.15",
                    created = 1725726765000,
                ),
                TransEntity(
                    personId = 4,
                    date = 1725790965000,
                    type = Constants.TYPE_CREDIT,
                    description = "",
                    amount = "113.15",
                    created = 1725790965000,
                ),
            )
        }
        mScope.launch(Dispatchers.IO) {
            mPersonsEntityLists.forEach { personDao.get().insertOrUpdate(personsEntity = it) }
            mTransEntityLists.forEach { transDao.get().insertOrUpdate(transEntity = it) }
        }
    }
}