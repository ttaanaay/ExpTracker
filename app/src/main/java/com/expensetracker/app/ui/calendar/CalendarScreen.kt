package com.expensetracker.app.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.components.TransactionRow
import com.expensetracker.app.ui.theme.ExpenseRed
import com.expensetracker.app.ui.theme.IncomeGreen
import com.expensetracker.app.ui.theme.TealPrimary
import com.expensetracker.app.util.formatAmountWithSign
import com.expensetracker.app.util.formatMonthYearThai
import com.expensetracker.app.util.thaiWeekdayAbbrev
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onBack: () -> Unit,
    onTransactionClick: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ปฏิทิน") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        }
    ) { padding ->
    Column(modifier = Modifier.fillMaxWidth().padding(padding).padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.changeMonth(state.month.minusMonths(1)) }) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = "เดือนก่อนหน้า")
            }
            Text(
                text = formatMonthYearThai(state.month.atDay(1)),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { viewModel.changeMonth(state.month.plusMonths(1)) }) {
                Icon(Icons.Filled.ChevronRight, contentDescription = "เดือนถัดไป")
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf(1, 2, 3, 4, 5, 6, 7).forEach { d ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        thaiWeekdayAbbrev(d),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }

        val firstOfMonth = state.month.atDay(1)
        val leadingBlanks = (firstOfMonth.dayOfWeek.value) % 7 // Mon=1..Sun=7 -> want Sun-first offset; approximate
        val daysInMonth = state.month.lengthOfMonth()
        val cells: List<LocalDate?> = List(leadingBlanks) { null } + (1..daysInMonth).map { state.month.atDay(it) }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(cells) { date ->
                if (date == null) {
                    Box(modifier = Modifier.aspectRatio(1f))
                } else {
                    val net = state.dailyNet[date]
                    val isSelected = date == state.selectedDate
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clickable { viewModel.selectDate(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) TealPrimary else androidx.compose.ui.graphics.Color.Transparent,
                                        CircleShape
                                    )
                                    .padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    color = if (isSelected) androidx.compose.ui.graphics.Color.White
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (net != null && net != 0.0) {
                                Text(
                                    text = if (net > 0) "+" else "-",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (net > 0) IncomeGreen else ExpenseRed
                                )
                            }
                        }
                    }
                }
            }
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 12.dp))
        Text(
            text = "รายการวันที่ ${state.selectedDate.dayOfMonth} ${com.expensetracker.app.util.thaiMonthAbbrev(state.selectedDate.monthValue)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.selectedDayTransactions, key = { it.transaction.id }) { item ->
                TransactionRow(item = item, onClick = { onTransactionClick(item.transaction.id) })
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("รวม", fontWeight = FontWeight.Bold)
                    Text(
                        text = formatAmountWithSign(state.selectedDayTotal, state.selectedDayTotal >= 0),
                        fontWeight = FontWeight.Bold,
                        color = if (state.selectedDayTotal >= 0) IncomeGreen else ExpenseRed
                    )
                }
            }
        }
    }
    }
}
