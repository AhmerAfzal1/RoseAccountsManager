package com.ahmer.accounts.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
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
    val date: String = "",
    val type: String = "",
    val description: String = "",
    val amount: String = "",
    val created: Long = System.currentTimeMillis(),
) : Parcelable
