package com.expensetracker.app.ui.theme

import androidx.compose.ui.graphics.Color

val TealPrimary = Color(0xFF1E8E7E)
val TealPrimaryDark = Color(0xFF4FBBA8)
val ExpenseRed = Color(0xFFE64848)
val IncomeGreen = Color(0xFF1E8E7E)
val BalanceBlue = Color(0xFF2F80ED)

val BackgroundLight = Color(0xFFF3F6F5)
val SurfaceLight = Color(0xFFFFFFFF)
val BackgroundDark = Color(0xFF121615)
val SurfaceDark = Color(0xFF1E2422)

val TextPrimaryLight = Color(0xFF1B1F1E)
val TextSecondaryLight = Color(0xFF6B7573)
val TextPrimaryDark = Color(0xFFECEFEE)
val TextSecondaryDark = Color(0xFFA0ABA8)

// Raw ARGB hex values (used as the persisted "color" field on CategoryEntity).
// Kept separate from Compose's Color type since Color.value is not a plain ARGB Long.
val CategoryColorHex: List<Long> = listOf(
    0xFFFF9800, 0xFF2196F3, 0xFFE91E63, 0xFFFFC107,
    0xFFF44336, 0xFF9C27B0, 0xFF4CAF50, 0xFF00BCD4,
    0xFF8BC34A, 0xFF607D8B, 0xFF3F51B5, 0xFF795548
)

val CategoryColors = CategoryColorHex.map { Color(it) }
