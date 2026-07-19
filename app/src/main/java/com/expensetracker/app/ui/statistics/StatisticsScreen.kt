package com.expensetracker.app.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.ui.components.DonutChart
import com.expensetracker.app.ui.components.DonutSlice
import com.expensetracker.app.ui.components.fallbackSliceColor
import com.expensetracker.app.ui.theme.ExpenseRed
import com.expensetracker.app.ui.theme.IncomeGreen
import com.expensetracker.app.util.formatAmount
import com.expensetracker.app.util.formatMonthYearThai

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel) {
    val state by viewModel.uiState.collectAsState()
    val slices = if (state.selectedType == TransactionType.EXPENSE) state.expenseSlices else state.incomeSlices
    val total = if (state.selectedType == TransactionType.EXPENSE) state.totalExpense else state.totalIncome

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        item {
            Text("สถิติ", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.changeMonth(state.month.minusMonths(1)) }) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "เดือนก่อนหน้า")
                }
                Text(formatMonthYearThai(state.month.atDay(1)), style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { viewModel.changeMonth(state.month.plusMonths(1)) }) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = "เดือนถัดไป")
                }
            }
            Spacer()
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TypePill(
                    label = "รายรับ",
                    selected = state.selectedType == TransactionType.INCOME,
                    color = IncomeGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setSelectedType(TransactionType.INCOME) }
                )
                TypePill(
                    label = "รายจ่าย",
                    selected = state.selectedType == TransactionType.EXPENSE,
                    color = ExpenseRed,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setSelectedType(TransactionType.EXPENSE) }
                )
            }
            Spacer(24.dp)
        }

        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                if (slices.isEmpty()) {
                    Text(
                        "ยังไม่มีข้อมูลในเดือนนี้",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 40.dp)
                    )
                } else {
                    DonutChart(
                        slices = slices.mapIndexed { index, s ->
                            DonutSlice(
                                value = s.amount.toFloat(),
                                color = s.category?.color?.let { Color(it) } ?: fallbackSliceColor(index)
                            )
                        },
                        centerLabel = formatAmount(total),
                        centerSubLabel = "บาท"
                    )
                }
            }
            Spacer(24.dp)
        }

        items(slices) { slice ->
            val color = slice.category?.color?.let { Color(it) } ?: Color.Gray
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color, CircleShape)
                    )
                    Spacer(8.dp, horizontal = true)
                    Text(slice.category?.icon ?: "📦")
                    Spacer(4.dp, horizontal = true)
                    Text(slice.category?.name ?: "ไม่มีหมวดหมู่", style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${formatAmount(slice.amount)}  ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(slice.percent * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }

        item { Spacer(80.dp) }
    }
}

@Composable
private fun TypePill(label: String, selected: Boolean, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) color else MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
    }
}

@Composable
private fun Spacer(height: androidx.compose.ui.unit.Dp = 12.dp, horizontal: Boolean = false) {
    if (horizontal) {
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(end = height))
    } else {
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = height))
    }
}
