package com.expensetracker.app.data.repository

import com.expensetracker.app.data.local.dao.CategorySum
import com.expensetracker.app.data.local.dao.TransactionDao
import com.expensetracker.app.data.local.entity.TransactionEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {

    fun getAll(): Flow<List<TransactionWithCategory>> = dao.getAll()

    fun getRecent(limit: Int = 10): Flow<List<TransactionWithCategory>> = dao.getRecent(limit)

    fun getBetweenDates(startDay: Long, endDay: Long): Flow<List<TransactionWithCategory>> =
        dao.getBetweenDates(startDay, endDay)

    fun getForDay(day: Long): Flow<List<TransactionWithCategory>> = dao.getForDay(day)

    suspend fun getById(id: Long): TransactionWithCategory? = dao.getById(id)

    fun search(
        query: String,
        typeFilter: TransactionType?,
        startDay: Long?,
        endDay: Long?
    ): Flow<List<TransactionWithCategory>> = dao.search(query, typeFilter, startDay, endDay)

    fun sumByTypeBetween(type: TransactionType, startDay: Long, endDay: Long): Flow<Double> =
        dao.sumByTypeBetween(type, startDay, endDay)

    fun sumByCategoryBetween(type: TransactionType, startDay: Long, endDay: Long): Flow<List<CategorySum>> =
        dao.sumByCategoryBetween(type, startDay, endDay)

    suspend fun add(transaction: TransactionEntity): Long = dao.insert(transaction)

    suspend fun update(transaction: TransactionEntity) = dao.update(transaction)

    suspend fun delete(transaction: TransactionEntity) = dao.delete(transaction)

    suspend fun getAllForExport(): List<TransactionEntity> = dao.getAllForExport()

    suspend fun replaceAll(transactions: List<TransactionEntity>) {
        dao.deleteAll()
        dao.insertAll(transactions)
    }
}
