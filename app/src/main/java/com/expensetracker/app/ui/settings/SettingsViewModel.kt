package com.expensetracker.app.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.repository.TransactionRepository
import com.expensetracker.app.datastore.AppSettings
import com.expensetracker.app.datastore.AppThemeMode
import com.expensetracker.app.datastore.SettingsDataStore
import com.expensetracker.app.util.CsvBackup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class BackupEvent {
    data object ExportSuccess : BackupEvent()
    data class ImportSuccess(val count: Int) : BackupEvent()
    data class Error(val message: String) : BackupEvent()
}

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsDataStore.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    private val _event = MutableStateFlow<BackupEvent?>(null)
    val event: StateFlow<BackupEvent?> = _event

    fun setThemeMode(mode: AppThemeMode) = viewModelScope.launch { settingsDataStore.setThemeMode(mode) }
    fun setCurrencySymbol(symbol: String) = viewModelScope.launch { settingsDataStore.setCurrencySymbol(symbol) }
    fun setSoundEnabled(enabled: Boolean) = viewModelScope.launch { settingsDataStore.setSoundEnabled(enabled) }
    fun setVibrationEnabled(enabled: Boolean) = viewModelScope.launch { settingsDataStore.setVibrationEnabled(enabled) }

    fun exportCsv(context: Context, uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val all = transactionRepository.getAllForExport()
                CsvBackup.export(context, uri, all)
            }.onSuccess {
                _event.value = BackupEvent.ExportSuccess
            }.onFailure {
                _event.value = BackupEvent.Error(it.message ?: "ส่งออกไม่สำเร็จ")
            }
        }
    }

    fun importCsv(context: Context, uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val imported = CsvBackup.import(context, uri)
                // Append rather than overwrite so users don't lose data by mistake.
                imported.forEach { transactionRepository.add(it) }
                imported.size
            }.onSuccess { count ->
                _event.value = BackupEvent.ImportSuccess(count)
            }.onFailure {
                _event.value = BackupEvent.Error(it.message ?: "นำเข้าไม่สำเร็จ")
            }
        }
    }

    fun clearEvent() {
        _event.value = null
    }
}
