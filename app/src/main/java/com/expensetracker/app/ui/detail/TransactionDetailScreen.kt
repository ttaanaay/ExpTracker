package com.expensetracker.app.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.ui.components.CategoryIconBadge
import com.expensetracker.app.ui.theme.ExpenseRed
import com.expensetracker.app.ui.theme.IncomeGreen
import com.expensetracker.app.util.formatAmountWithSign
import com.expensetracker.app.util.formatDateThaiFull
import com.expensetracker.app.util.formatTime
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onDeleted: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onDeleted()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("รายละเอียดรายการ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                },
                actions = {
                    state.item?.let { item ->
                        IconButton(onClick = { onEdit(item.transaction.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "แก้ไข")
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "ลบ", tint = ExpenseRed)
                        }
                    }
                }
            )
        }
    ) { padding ->
        state.item?.let { item ->
            val isIncome = item.transaction.type == TransactionType.INCOME
            Column(modifier = Modifier.padding(padding).padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CategoryIconBadge(
                        icon = item.category?.icon ?: "📦",
                        color = item.category?.color?.let { Color(it) } ?: Color.Gray,
                        size = 56.dp
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Column {
                        Text(item.category?.name ?: "ไม่มีหมวดหมู่", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = formatDateThaiFull(java.time.LocalDate.ofEpochDay(item.transaction.dateEpochDay)) +
                                " · " + formatTime(item.transaction.timeMinutes),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = formatAmountWithSign(item.transaction.amount, isIncome) + " บาท",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isIncome) IncomeGreen else ExpenseRed
                )
                Spacer(modifier = Modifier.height(24.dp))
                if (item.transaction.note.isNotBlank()) {
                    Text("หมายเหตุ", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.transaction.note, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("ลบรายการ") },
            text = { Text("ต้องการลบรายการนี้ใช่หรือไม่?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.delete()
                    showDeleteConfirm = false
                }) { Text("ลบ") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("ยกเลิก") }
            }
        )
    }
}
