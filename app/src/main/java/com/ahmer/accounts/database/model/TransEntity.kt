package com.ahmer.accounts.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = Constants.DATABASE_TRANSACTION_TABLE, foreignKeys = [ForeignKey(
        entity = PersonsEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("personId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(value = ["id"], unique = true), Index(value = ["personId"])]
)
@Parcelize
data class TransEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val personId: Int = 0,
    val date: Long = 0,
    val type: String = "",
    val description: String = "",
    val amount: String = "",
    val created: Long = System.currentTimeMillis(),
) : Parcelable {
    val shortDate: String
        get() = HelperUtils.getDateTime(time = date, pattern = Constants.DATE_SHORT_PATTERN)
}