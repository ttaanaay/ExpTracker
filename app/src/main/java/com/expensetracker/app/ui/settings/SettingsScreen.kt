package com.expensetracker.app.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expensetracker.app.datastore.AppThemeMode
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onManageCategories: () -> Unit) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current
    val event by viewModel.event.collectAsState()

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri -> uri?.let { viewModel.exportCsv(context, it) } }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri -> uri?.let { viewModel.importCsv(context, it) } }

    Scaffold(topBar = { TopAppBar(title = { Text("ตั้งค่า") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                SectionTitle("ธีม")
                ThemeOption("สว่าง (Light)", settings.themeMode == AppThemeMode.LIGHT) {
                    viewModel.setThemeMode(AppThemeMode.LIGHT)
                }
                ThemeOption("มืด (Dark)", settings.themeMode == AppThemeMode.DARK) {
                    viewModel.setThemeMode(AppThemeMode.DARK)
                }
                ThemeOption("ตามระบบ (System)", settings.themeMode == AppThemeMode.SYSTEM) {
                    viewModel.setThemeMode(AppThemeMode.SYSTEM)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                SectionTitle("สกุลเงิน")
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("สกุลเงินปัจจุบัน")
                    Text(settings.currencySymbol, fontWeight = FontWeight.Medium)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                SectionTitle("การแจ้งเตือน")
                ToggleRow(
                    label = "การสั่น",
                    checked = settings.vibrationEnabled,
                    onCheckedChange = viewModel::setVibrationEnabled
                )
                ToggleRow(
                    label = "เสียง",
                    checked = settings.soundEnabled,
                    onCheckedChange = viewModel::setSoundEnabled
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                SectionTitle("หมวดหมู่")
                SettingsActionRow(label = "จัดการหมวดหมู่", onClick = onManageCategories)
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                SectionTitle("ข้อมูล")
                SettingsActionRow(
                    label = "สำรองข้อมูล (Export CSV)",
                    onClick = { exportLauncher.launch("expense_tracker_backup.csv") }
                )
                SettingsActionRow(
                    label = "กู้คืนข้อมูล (Import CSV)",
                    onClick = { importLauncher.launch(arrayOf("text/csv", "text/comma-separated-values", "*/*")) }
                )
            }

            event?.let { evt ->
                item {
                    val message = when (evt) {
                        is BackupEvent.ExportSuccess -> "สำรองข้อมูลสำเร็จ"
                        is BackupEvent.ImportSuccess -> "นำเข้าข้อมูลสำเร็จ (${evt.count} รายการ)"
                        is BackupEvent.Error -> "เกิดข้อผิดพลาด: ${evt.message}"
                    }
                    Text(
                        text = message,
                        color = if (evt is BackupEvent.Error) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ThemeOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsActionRow(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .then(Modifier.androidClickable(onClick)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
    }
}

private fun Modifier.androidClickable(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))
