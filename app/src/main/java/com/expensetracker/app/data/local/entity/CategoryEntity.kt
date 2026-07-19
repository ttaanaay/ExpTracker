package com.expensetracker.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.expensetracker.app.data.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val icon: String,      // emoji or icon key, e.g. "🍜"
    val color: Long,       // ARGB color as Long
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)
