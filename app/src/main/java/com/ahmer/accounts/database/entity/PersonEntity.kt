package com.ahmer.accounts.database.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a person in the database.
 *
 * This entity is used for storing person details and is annotated with Room and Parcelable annotations.
 */
@Keep
@Entity(
    tableName = Constants.DB_TABLE_PERSON,
    indices = [Index(value = ["id"], unique = true)]
)

@Parcelize
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val notes: String = "",
    val created: Long = System.currentTimeMillis(),
    val updated: Long = 0L
) : Parcelable