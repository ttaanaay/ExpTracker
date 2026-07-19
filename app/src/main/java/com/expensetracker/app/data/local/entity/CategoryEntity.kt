package com.expensetracker.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.expensetracker.app.data.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val icon: String,      // ชื่อไอคอน (drawable resource key), เช่น "ic_food" — ดู CategoryIcons.kt
    val color: Long,       // ARGB color as Long
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)
