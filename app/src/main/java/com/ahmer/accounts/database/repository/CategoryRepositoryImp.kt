package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.CategoryDao
import com.ahmer.accounts.database.entity.CategoryEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class CategoryRepositoryImp @Inject constructor(private val categoryDao: CategoryDao) :
    CategoryRepository {
    override suspend fun insertOrUpdate(categoryEntity: CategoryEntity) {
        return withContext(Dispatchers.IO) {
            categoryDao.insertOrUpdate(categoryEntity = categoryEntity)
        }
    }

    override suspend fun delete(categoryEntity: CategoryEntity) {
        return withContext(Dispatchers.IO) {
            categoryDao.delete(categoryEntity = categoryEntity)
        }
    }

    override fun allCategories(): Flow<List<CategoryEntity>> = categoryDao.allCategories()

}