package com.expensetracker.app.data.local

import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.model.TransactionType

object DefaultCategories {

    fun seed(): List<CategoryEntity> = listOf(
        // รายจ่าย
        CategoryEntity(name = "อาหาร", type = TransactionType.EXPENSE, icon = "ic_food", color = 0xFFFF9800, isDefault = true, sortOrder = 0),
        CategoryEntity(name = "เดินทาง", type = TransactionType.EXPENSE, icon = "ic_car", color = 0xFF2196F3, isDefault = true, sortOrder = 1),
        CategoryEntity(name = "ช้อปปิ้ง", type = TransactionType.EXPENSE, icon = "ic_cart", color = 0xFFE91E63, isDefault = true, sortOrder = 2),
        CategoryEntity(name = "ค่าน้ำค่าไฟ", type = TransactionType.EXPENSE, icon = "ic_electricity_bill", color = 0xFFFFC107, isDefault = true, sortOrder = 3),
        CategoryEntity(name = "สุขภาพ", type = TransactionType.EXPENSE, icon = "ic_health", color = 0xFFF44336, isDefault = true, sortOrder = 4),
        CategoryEntity(name = "บันเทิง", type = TransactionType.EXPENSE, icon = "ic_gaming", color = 0xFF9C27B0, isDefault = true, sortOrder = 5),
        CategoryEntity(name = "อื่นๆ", type = TransactionType.EXPENSE, icon = "ic_package", color = 0xFF9E9E9E, isDefault = true, sortOrder = 6),

        // รายรับ
        CategoryEntity(name = "เงินเดือน", type = TransactionType.INCOME, icon = "ic_briefcase", color = 0xFF1E8E7E, isDefault = true, sortOrder = 0),
        CategoryEntity(name = "โบนัส", type = TransactionType.INCOME, icon = "ic_gift", color = 0xFF4CAF50, isDefault = true, sortOrder = 1),
        CategoryEntity(name = "ขายของ", type = TransactionType.INCOME, icon = "ic_wallet", color = 0xFF00BCD4, isDefault = true, sortOrder = 2),
        CategoryEntity(name = "ลงทุน", type = TransactionType.INCOME, icon = "ic_investment", color = 0xFF8BC34A, isDefault = true, sortOrder = 3),
        CategoryEntity(name = "อื่นๆ", type = TransactionType.INCOME, icon = "ic_cash", color = 0xFF607D8B, isDefault = true, sortOrder = 4),
    )
}
