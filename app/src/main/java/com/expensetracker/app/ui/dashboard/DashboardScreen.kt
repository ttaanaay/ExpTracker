package com.expensetracker.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.components.TransactionRow
import com.expensetracker.app.ui.theme.ExpenseRed
import com.expensetracker.app.ui.theme.IncomeGreen
import com.expensetracker.app.ui.theme.BalanceBlue
import com.expensetracker.app.util.formatAmount
import com.expensetracker.app.util.formatMonthYearThai

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onSeeAllClick: () -> Unit,
    onTransactionClick: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "สวัสดีตอนเช้า ☀️",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatMonthYearThai(java.time.LocalDate.now()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "ภาพรวมเดือนนี้ · ${formatMonthYearThai(state.month.atDay(1))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SummaryRow(label = "รายรับ", amount = state.income, color = IncomeGreen, arrowUp = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    SummaryRow(label = "รายจ่าย", amount = state.expense, color = ExpenseRed, arrowUp = false)
                    Spacer(modifier = Modifier.height(8.dp))
                    SummaryRow(label = "คงเหลือ", amount = state.balance, color = BalanceBlue, arrowUp = null)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "สรุปรายการล่าสุด",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = onSeeAllClick) {
                    Text("ดูทั้งหมด")
                }
            }
        }

        items(state.recent, key = { it.transaction.id }) { item ->
            TransactionRow(item = item, onClick = { onTransactionClick(item.transaction.id) })
        }

        if (state.recent.isEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "ยังไม่มีรายการในเดือนนี้",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun SummaryRow(label: String, amount: Double, color: Color, arrowUp: Boolean?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val symbol = when (arrowUp) {
                true -> "↑"
                false -> "↓"
                null -> "●"
            }
            Text(text = symbol, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width8())
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            text = "${formatAmount(amount)} บาท",
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private fun Modifier.width8() = this.then(Modifier.padding(end = 8.dp))
