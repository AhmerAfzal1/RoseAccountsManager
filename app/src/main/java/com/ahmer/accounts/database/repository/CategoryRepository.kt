package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insertOrUpdate(categoryEntity: CategoryEntity)
    suspend fun delete(categoryEntity: CategoryEntity)
    fun allCategories(): Flow<List<CategoryEntity>>
}