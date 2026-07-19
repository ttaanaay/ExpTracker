package com.expensetracker.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.model.TransactionWithCategory
import com.expensetracker.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.YearMonth

data class DashboardUiState(
    val month: YearMonth = YearMonth.now(),
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val balance: Double = 0.0,
    val recent: List<TransactionWithCategory> = emptyList()
)

class DashboardViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        loadMonth(YearMonth.now())
    }

    private fun loadMonth(month: YearMonth) {
        val start = month.atDay(1).toEpochDay()
        val end = month.atEndOfMonth().toEpochDay()

        combine(
            repository.sumByTypeBetween(TransactionType.INCOME, start, end),
            repository.sumByTypeBetween(TransactionType.EXPENSE, start, end),
            repository.getRecent(10)
        ) { income, expense, recent ->
            DashboardUiState(
                month = month,
                income = income,
                expense = expense,
                balance = income - expense,
                recent = recent
            )
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)
    }

    fun changeMonth(month: YearMonth) = loadMonth(month)
}
