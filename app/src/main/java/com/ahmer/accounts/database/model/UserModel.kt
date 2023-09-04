package com.ahmer.accounts.database.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import kotlinx.parcelize.Parcelize

@Keep
@Entity(
    tableName = Constants.DATABASE_USER_TABLE,
    indices = [Index(value = arrayOf("_id"), unique = true)]
)
@Parcelize
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Long = 0.toLong(),

    @ColumnInfo(name = "Name")
    val name: String = "",

    @ColumnInfo(name = "Address")
    val address: String = "",

    @ColumnInfo(name = "Phone")
    val phone: String = "",

    @ColumnInfo(name = "Email")
    val email: String = "",

    @ColumnInfo(name = "Notes")
    val notes: String = "",

    @ColumnInfo(name = "Created")
    val created: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "LastModified")
    val modified: Long = 0.toLong(),
) : Parcelable