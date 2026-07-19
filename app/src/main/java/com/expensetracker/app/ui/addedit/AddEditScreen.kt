package com.expensetracker.app.ui.addedit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.ui.components.CategoryIconBadge
import com.expensetracker.app.ui.theme.ExpenseRed
import com.expensetracker.app.ui.theme.IncomeGreen
import com.expensetracker.app.util.formatDateThaiFull
import com.expensetracker.app.util.formatTime

@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditMode) "แก้ไขรายการ" else "เพิ่มรายการ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.save() }) {
                        Icon(Icons.Filled.Check, contentDescription = "บันทึก")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Type toggle
            Row(modifier = Modifier.fillMaxWidth()) {
                TypeToggleButton(
                    label = "รายรับ",
                    selected = state.type == TransactionType.INCOME,
                    color = IncomeGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setType(TransactionType.INCOME) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                TypeToggleButton(
                    label = "รายจ่าย",
                    selected = state.type == TransactionType.EXPENSE,
                    color = ExpenseRed,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setType(TransactionType.EXPENSE) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("จำนวนเงิน", style = MaterialTheme.typography.labelSmall)
            OutlinedTextField(
                value = state.amountText,
                onValueChange = viewModel::setAmountText,
                placeholder = { Text("0.00") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoRow(
                label = "หมวดหมู่",
                value = state.selectedCategory?.let { "${it.icon} ${it.name}" } ?: "เลือกหมวดหมู่"
            )
            InfoRow(label = "วันที่", value = formatDateThaiFull(state.date), onClick = {
                DatePickerDialog(
                    context,
                    { _, y, m, d -> viewModel.setDate(java.time.LocalDate.of(y, m + 1, d)) },
                    state.date.year, state.date.monthValue - 1, state.date.dayOfMonth
                ).show()
            })
            InfoRow(label = "เวลา", value = formatTime(state.time.hour * 60 + state.time.minute), onClick = {
                TimePickerDialog(
                    context,
                    { _, h, min -> viewModel.setTime(java.time.LocalTime.of(h, min)) },
                    state.time.hour, state.time.minute, true
                ).show()
            })

            Spacer(modifier = Modifier.height(4.dp))
            Text("หมายเหตุ", style = MaterialTheme.typography.labelSmall)
            OutlinedTextField(
                value = state.note,
                onValueChange = viewModel::setNote,
                placeholder = { Text("เพิ่มหมายเหตุ (ไม่บังคับ)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text("หมวดหมู่ที่ใช้บ่อย", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.categories) { category ->
                    QuickCategoryChip(
                        category = category,
                        selected = state.selectedCategory?.id == category.id,
                        onClick = { viewModel.setCategory(category) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TypeToggleButton(
    label: String,
    selected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = label,
            color = if (selected) color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun QuickCategoryChip(
    category: CategoryEntity,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        CategoryIconBadge(
            icon = category.icon,
            color = if (selected) Color(category.color) else Color(category.color).copy(alpha = 0.5f),
            size = 52.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(category.name, style = MaterialTheme.typography.labelSmall)
    }
}
