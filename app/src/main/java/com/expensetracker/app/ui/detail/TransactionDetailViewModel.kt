package com.expensetracker.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.model.TransactionWithCategory
import com.expensetracker.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TransactionDetailUiState(
    val item: TransactionWithCategory? = null,
    val isDeleted: Boolean = false
)

class TransactionDetailViewModel(
    private val repository: TransactionRepository,
    private val transactionId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionDetailUiState())
    val uiState: StateFlow<TransactionDetailUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(item = repository.getById(transactionId))
        }
    }

    fun delete() {
        viewModelScope.launch {
            _uiState.value.item?.let { repository.delete(it.transaction) }
            _uiState.value = _uiState.value.copy(isDeleted = true)
        }
    }
}
