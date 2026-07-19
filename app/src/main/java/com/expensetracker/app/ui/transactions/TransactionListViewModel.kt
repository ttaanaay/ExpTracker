package com.expensetracker.app.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.local.entity.TransactionEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.model.TransactionWithCategory
import com.expensetracker.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionListViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _filter = MutableStateFlow<TransactionType?>(null)
    val filter: StateFlow<TransactionType?> = _filter

    val transactions: StateFlow<List<TransactionWithCategory>> = _filter
        .flatMapLatest { type ->
            if (type == null) repository.getAll()
            else repository.search("", type, null, null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(type: TransactionType?) {
        _filter.value = type
    }

    fun delete(item: TransactionWithCategory) {
        viewModelScope.launch { repository.delete(item.transaction) }
    }

    fun undoDelete(entity: TransactionEntity) {
        viewModelScope.launch { repository.add(entity.copy(id = 0)) }
    }
}
