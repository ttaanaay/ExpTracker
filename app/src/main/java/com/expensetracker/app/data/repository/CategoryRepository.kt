package com.expensetracker.app.data.repository

import com.expensetracker.app.data.local.dao.CategoryDao
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val dao: CategoryDao) {

    fun getAll(): Flow<List<CategoryEntity>> = dao.getAll()

    fun getByType(type: TransactionType): Flow<List<CategoryEntity>> = dao.getByType(type)

    suspend fun getById(id: Long): CategoryEntity? = dao.getById(id)

    suspend fun add(category: CategoryEntity): Long = dao.insert(category)

    suspend fun update(category: CategoryEntity) = dao.update(category)

    /**
     * Returns false (and does not delete) if the category still has transactions attached,
     * so the caller can warn the user instead of silently orphaning data.
     */
    suspend fun delete(category: CategoryEntity): Boolean {
        val usage = dao.transactionCountForCategory(category.id)
        if (usage > 0) return false
        dao.delete(category)
        return true
    }

    suspend fun forceDelete(category: CategoryEntity) = dao.delete(category)

    suspend fun transactionCount(categoryId: Long): Int = dao.transactionCountForCategory(categoryId)
}
