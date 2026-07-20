package com.expensetracker.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.model.TransactionWithCategory
import com.expensetracker.app.ui.icons.CategoryIcons
import com.expensetracker.app.ui.theme.ExpenseRed
import com.expensetracker.app.ui.theme.IncomeGreen
import com.expensetracker.app.util.formatAmountWithSign
import com.expensetracker.app.util.formatTime

@Composable
fun CategoryIconBadge(
    icon: String,
    color: Color,
    size: androidx.compose.ui.unit.Dp = 40.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(color.copy(alpha = 0.15f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = CategoryIcons.resolve(icon)),
            contentDescription = null,
            modifier = Modifier.size(size * 0.62f)
        )
    }
}

@Composable
fun TransactionRow(
    item: TransactionWithCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val isIncome = item.transaction.type == TransactionType.INCOME
    val categoryColor = item.category?.color?.let { Color(it) } ?: Color.Gray
    val icon = item.category?.icon ?: CategoryIcons.DEFAULT
    val name = item.category?.name ?: "ไม่มีหมวดหมู่"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CategoryIconBadge(icon = icon, color = categoryColor)
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(12.dp))
            Column {
                Text(text = name, style = MaterialTheme.typography.bodyLarge)
                if (item.transaction.note.isNotBlank()) {
                    Text(
                        text = item.transaction.note,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatAmountWithSign(item.transaction.amount, isIncome),
                color = if (isIncome) IncomeGreen else ExpenseRed,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = formatTime(item.transaction.timeMinutes),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
