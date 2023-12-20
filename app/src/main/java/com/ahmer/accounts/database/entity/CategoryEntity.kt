package com.ahmer.accounts.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(
    tableName = Constants.DB_TABLE_CATEGORY,
    indices = [Index(value = arrayOf("id"), unique = true)]
)
@Parcelize
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String = "",
    val type: String = "",
) : Parcelable