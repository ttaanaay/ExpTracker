package com.expensetracker.app.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.model.TransactionWithCategory
import com.expensetracker.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val month: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val dailyNet: Map<LocalDate, Double> = emptyMap(),
    val selectedDayTransactions: List<TransactionWithCategory> = emptyList(),
    val selectedDayTotal: Double = 0.0
)

class CalendarViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _month = MutableStateFlow(YearMonth.now())
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        combine(_month, _selectedDate) { month, date -> month to date }
            .flatMapLatest { (month, date) -> monthFlow(month, date) }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    private fun monthFlow(month: YearMonth, selectedDate: LocalDate) : kotlinx.coroutines.flow.Flow<CalendarUiState> {
        val start = month.atDay(1).toEpochDay()
        val end = month.atEndOfMonth().toEpochDay()

        return combine(
            repository.getBetweenDates(start, end),
            repository.getForDay(selectedDate.toEpochDay())
        ) { monthItems, dayItems ->
            val net = monthItems.groupBy { LocalDate.ofEpochDay(it.transaction.dateEpochDay) }
                .mapValues { (_, items) ->
                    items.sumOf { if (it.transaction.type == TransactionType.INCOME) it.transaction.amount else -it.transaction.amount }
                }
            CalendarUiState(
                month = month,
                selectedDate = selectedDate,
                dailyNet = net,
                selectedDayTransactions = dayItems,
                selectedDayTotal = dayItems.sumOf {
                    if (it.transaction.type == TransactionType.INCOME) it.transaction.amount else -it.transaction.amount
                }
            )
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun changeMonth(month: YearMonth) {
        _month.value = month
    }
}
