package com.ahmer.accounts.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = Constants.DB_TABLE_EXPENSE, foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(value = ["id"], unique = true), Index(value = ["category"])]
)
@Parcelize
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: Int = 0,
    val title: String = "",
    val date: Long = 0L,
    val type: String = "",
    val amount: String = "",
    val notes: String = "",
    val created: Long = System.currentTimeMillis(),
    val createdOn: String = HelperUtils.getDateTime(
        time = created, pattern = Constants.PATTERN_CHART
    ),
) : Parcelable