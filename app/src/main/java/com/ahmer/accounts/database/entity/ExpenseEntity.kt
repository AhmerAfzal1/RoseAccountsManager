package com.ahmer.accounts.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = Constants.DB_TABLE_EXPENSE, indices = [Index(value = arrayOf("id"), unique = true)]
)
@Parcelize
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = 0L,
    val type: String = "",
    val category: String = "",
    val amount: String = "",
    val description: String = "",
    val created: Long = System.currentTimeMillis(),
    val createdOn: String = HelperUtils.getDateTime(
        time = created, pattern = Constants.PATTERN_CHART
    ),
) : Parcelable