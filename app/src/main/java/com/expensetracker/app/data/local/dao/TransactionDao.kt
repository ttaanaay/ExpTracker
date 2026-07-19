package com.expensetracker.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.expensetracker.app.data.local.entity.TransactionEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

data class CategorySum(val categoryId: Long?, val total: Double)

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY dateEpochDay DESC, timeMinutes DESC, id DESC")
    fun getAll(): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY dateEpochDay DESC, timeMinutes DESC, id DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query(
        """SELECT * FROM transactions 
           WHERE dateEpochDay BETWEEN :startDay AND :endDay
           ORDER BY dateEpochDay DESC, timeMinutes DESC, id DESC"""
    )
    fun getBetweenDates(startDay: Long, endDay: Long): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE dateEpochDay = :day ORDER BY timeMinutes DESC, id DESC")
    fun getForDay(day: Long): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionWithCategory?

    @Transaction
    @Query(
        """SELECT t.* FROM transactions t
           LEFT JOIN categories c ON t.categoryId = c.id
           WHERE 
             (:typeFilter IS NULL OR t.type = :typeFilter)
             AND (:startDay IS NULL OR t.dateEpochDay >= :startDay)
             AND (:endDay IS NULL OR t.dateEpochDay <= :endDay)
             AND (
                 :query = '' 
                 OR t.note LIKE '%' || :query || '%' 
                 OR c.name LIKE '%' || :query || '%'
                 OR CAST(t.amount AS TEXT) LIKE '%' || :query || '%'
             )
           ORDER BY t.dateEpochDay DESC, t.timeMinutes DESC, t.id DESC"""
    )
    fun search(
        query: String,
        typeFilter: TransactionType?,
        startDay: Long?,
        endDay: Long?
    ): Flow<List<TransactionWithCategory>>

    @Query(
        """SELECT COALESCE(SUM(amount), 0) FROM transactions 
           WHERE type = :type AND dateEpochDay BETWEEN :startDay AND :endDay"""
    )
    fun sumByTypeBetween(type: TransactionType, startDay: Long, endDay: Long): Flow<Double>

    @Query(
        """SELECT categoryId, SUM(amount) as total FROM transactions
           WHERE type = :type AND dateEpochDay BETWEEN :startDay AND :endDay
           GROUP BY categoryId
           ORDER BY total DESC"""
    )
    fun sumByCategoryBetween(type: TransactionType, startDay: Long, endDay: Long): Flow<List<CategorySum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY dateEpochDay ASC, timeMinutes ASC")
    suspend fun getAllForExport(): List<TransactionEntity>

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)
}
