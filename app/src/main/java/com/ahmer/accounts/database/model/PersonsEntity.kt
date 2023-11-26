package com.ahmer.accounts.database.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import kotlinx.parcelize.Parcelize

@Keep
@Entity(
    tableName = Constants.DATABASE_TABLE_PERSONS,
    indices = [Index(value = arrayOf("id"), unique = true)]
)
@Parcelize
data class PersonsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val notes: String = "",
    val created: Long = System.currentTimeMillis(),
    val updated: Long = 0.toLong()
) : Parcelable