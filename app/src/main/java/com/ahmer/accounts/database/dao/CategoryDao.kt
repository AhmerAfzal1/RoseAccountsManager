package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ahmer.accounts.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Upsert
    suspend fun insertOrUpdate(categoryEntity: CategoryEntity)

    @Upsert
    suspend fun insertOrUpdate(listCategoryEntity: List<CategoryEntity>)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Query(value = "SELECT * FROM Category")
    fun allCategories(): Flow<List<CategoryEntity>>

    @Query(value = "SELECT * FROM Category WHERE id = :categoryId")
    fun categoryById(categoryId: Int): Flow<CategoryEntity>
}