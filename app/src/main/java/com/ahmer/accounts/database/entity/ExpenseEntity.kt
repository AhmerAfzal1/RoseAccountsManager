package com.ahmer.accounts.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import java.io.Serializable

@Entity(
    tableName = Constants.DB_TABLE_EXPENSE, foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("category", "type"),
        childColumns = arrayOf("category", "type"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
    )], indices = [Index(value = ["id"], unique = true), Index(value = ["category", "type"])]
)
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
) : Serializable