package com.expensetracker.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.model.TransactionWithCategory
import com.expensetracker.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class SearchFilters(
    val query: String = "",
    val type: TransactionType? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

class SearchViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _filters = MutableStateFlow(SearchFilters())
    val filters: StateFlow<SearchFilters> = _filters

    val results: StateFlow<List<TransactionWithCategory>> = _filters
        .flatMapLatest { f ->
            if (f.query.isBlank() && f.type == null && f.startDate == null && f.endDate == null) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                repository.search(
                    query = f.query,
                    typeFilter = f.type,
                    startDay = f.startDate?.toEpochDay(),
                    endDay = f.endDate?.toEpochDay()
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(query: String) {
        _filters.value = _filters.value.copy(query = query)
    }

    fun setType(type: TransactionType?) {
        _filters.value = _filters.value.copy(type = type)
    }

    fun setDateRange(start: LocalDate?, end: LocalDate?) {
        _filters.value = _filters.value.copy(startDate = start, endDate = end)
    }

    fun clear() {
        _filters.value = SearchFilters()
    }
}
