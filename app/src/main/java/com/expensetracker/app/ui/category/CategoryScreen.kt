package com.expensetracker.app.ui.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.ui.components.CategoryIconBadge
import com.expensetracker.app.ui.icons.CategoryIcons
import com.expensetracker.app.ui.theme.ExpenseRed
import com.expensetracker.app.ui.theme.IncomeGreen

private val availableIcons = CategoryIcons.ALL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }

    LaunchedEffect(state.deleteBlockedMessage) {
        // Message surfaces via a simple inline banner below; auto handled in UI section
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("หมวดหมู่") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "ย้อนกลับ")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(modifier = Modifier.padding(16.dp)) {
                TypeTab(
                    label = "รายรับ",
                    selected = state.selectedType == TransactionType.INCOME,
                    color = IncomeGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setSelectedType(TransactionType.INCOME) }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                TypeTab(
                    label = "รายจ่าย",
                    selected = state.selectedType == TransactionType.EXPENSE,
                    color = ExpenseRed,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setSelectedType(TransactionType.EXPENSE) }
                )
            }

            state.deleteBlockedMessage?.let { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = ExpenseRed.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(msg, color = ExpenseRed, style = MaterialTheme.typography.labelSmall)
                        TextButton(onClick = { viewModel.clearMessage() }) { Text("ปิด") }
                    }
                }
            }

            val list = if (state.selectedType == TransactionType.INCOME) state.incomeCategories else state.expenseCategories

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(list, key = { it.id }) { category ->
                    CategoryRow(
                        category = category,
                        onEdit = { editingCategory = category },
                        onDelete = { viewModel.deleteCategory(category) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ เพิ่มหมวดหมู่")
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CategoryEditDialog(
            initial = null,
            defaultType = state.selectedType,
            existingColors = list.map { it.color },
            onDismiss = { showAddDialog = false },
            onConfirm = { name, icon, color, type ->
                viewModel.addCategory(name, icon, color, type)
                showAddDialog = false
            }
        )
    }

    editingCategory?.let { category ->
        val sameTypeList = if (category.type == TransactionType.INCOME) state.incomeCategories else state.expenseCategories
        CategoryEditDialog(
            initial = category,
            defaultType = category.type,
            existingColors = sameTypeList.map { it.color },
            onDismiss = { editingCategory = null },
            onConfirm = { name, icon, color, type ->
                viewModel.updateCategory(category.copy(name = name, icon = icon, color = color, type = type))
                editingCategory = null
            }
        )
    }
}

@Composable
private fun TypeTab(label: String, selected: Boolean, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) color else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        )
    }
}

@Composable
private fun CategoryRow(
    category: CategoryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CategoryIconBadge(icon = category.icon, color = Color(category.color))
            Spacer(modifier = Modifier.padding(6.dp))
            Text(category.name, style = MaterialTheme.typography.bodyLarge)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "แก้ไข", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "ลบ", tint = ExpenseRed.copy(alpha = 0.7f))
            }
        }
    }
}

/** สุ่มสีจาก palette โดยเลี่ยงสีที่หมวดหมู่ประเภทเดียวกันใช้อยู่แล้ว ถ้าใช้ครบทุกสีแล้วค่อยสุ่มซ้ำ */
private fun randomUnusedColor(existingColors: List<Long>): Long {
    val palette = com.expensetracker.app.ui.theme.CategoryColorHex
    val unused = palette.filter { it !in existingColors }
    return (unused.ifEmpty { palette }).random()
}

@Composable
private fun CategoryEditDialog(
    initial: CategoryEntity?,
    defaultType: TransactionType,
    existingColors: List<Long> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (name: String, icon: String, color: Long, type: TransactionType) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var selectedIcon by remember { mutableStateOf(initial?.icon ?: availableIcons.first()) }
    var selectedColor by remember {
        mutableStateOf(initial?.color ?: randomUnusedColor(existingColors))
    }
    var type by remember { mutableStateOf(initial?.type ?: defaultType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "เพิ่มหมวดหมู่" else "แก้ไขหมวดหมู่") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("ชื่อหมวดหมู่") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("ไอคอน", style = MaterialTheme.typography.labelSmall)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.height(340.dp)
                ) {
                    items(availableIcons) { icon ->
                        val selected = icon == selectedIcon
                        Image(
                            painter = painterResource(id = CategoryIcons.resolve(icon)),
                            contentDescription = icon,
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable { selectedIcon = icon }
                                .then(
                                    if (selected) Modifier.background(
                                        Color(selectedColor).copy(alpha = 0.2f),
                                        CircleShape
                                    ) else Modifier
                                )
                                .padding(6.dp)
                                .size(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("สี", style = MaterialTheme.typography.labelSmall)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.height(80.dp)
                ) {
                    items(com.expensetracker.app.ui.theme.CategoryColorHex) { colorLong ->
                        val selected = colorLong == selectedColor
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(if (selected) 30.dp else 24.dp)
                                .background(Color(colorLong), CircleShape)
                                .clickable { selectedColor = colorLong }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) onConfirm(name, selectedIcon, selectedColor, type)
            }) { Text("บันทึก") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("ยกเลิก") }
        }
    )
}
