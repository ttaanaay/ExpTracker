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
// 12 สีแรกคือของเดิม (คงไว้ไม่เปลี่ยน กันหมวดหมู่เก่าเปลี่ยนสี) + เพิ่มอีก 20 สีที่กระจาย hue
// ให้ต่างกันชัดเจน รวมเป็น 32 สี เวลาสุ่มให้หมวดหมู่จะไม่ซ้ำกันมากขึ้น เหมาะกับกราฟที่มีหลายหมวดหมู่
val CategoryColorHex: List<Long> = listOf(
    // 12 สีเดิม
    0xFFFF9800, 0xFF2196F3, 0xFFE91E63, 0xFFFFC107,
    0xFFF44336, 0xFF9C27B0, 0xFF4CAF50, 0xFF00BCD4,
    0xFF8BC34A, 0xFF607D8B, 0xFF3F51B5, 0xFF795548,
    // 20 สีใหม่ (กระจาย hue เท่าๆ กันเพื่อให้แยกแยะง่ายในกราฟ)
    0xFFE645BE, 0xFF67E6E0, 0xFF6A22E6, 0xFFC73C5A,
    0xFFB1C75A, 0xFF1EC786, 0xFF3632A6, 0xFF62A64B,
    0xFFA61966, 0xFFE6D045, 0xFFB067E6, 0xFF22E65A,
    0xFF3C65C7, 0xFFC75ABA, 0xFF1EC7A9, 0xFF4E32A6,
    0xFF9FA64B, 0xFF19A65E, 0xFF83E645, 0xFF67E67B
)

val CategoryColors = CategoryColorHex.map { Color(it) }
