package com.expensetracker.app.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.local.dao.CategorySum
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.repository.CategoryRepository
import com.expensetracker.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.YearMonth

data class CategorySlice(
    val category: CategoryEntity?,
    val amount: Double,
    val percent: Float
)

data class StatisticsUiState(
    val month: YearMonth = YearMonth.now(),
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val expenseSlices: List<CategorySlice> = emptyList(),
    val incomeSlices: List<CategorySlice> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
)

class StatisticsViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _month = MutableStateFlow(YearMonth.now())
    private val _selectedType = MutableStateFlow(TransactionType.EXPENSE)

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState

    init {
        combine(_month, categoryRepository.getAll()) { month, categories -> month to categories }
            .flatMapLatest { (month, categories) -> buildFlow(month, categories) }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    private fun buildFlow(month: YearMonth, categories: List<CategoryEntity>) =
        combine(
            transactionRepository.sumByCategoryBetween(
                TransactionType.EXPENSE, month.atDay(1).toEpochDay(), month.atEndOfMonth().toEpochDay()
            ),
            transactionRepository.sumByCategoryBetween(
                TransactionType.INCOME, month.atDay(1).toEpochDay(), month.atEndOfMonth().toEpochDay()
            )
        ) { expenseSums, incomeSums ->
            val catMap = categories.associateBy { it.id }
            val expenseSlices = toSlices(expenseSums, catMap)
            val incomeSlices = toSlices(incomeSums, catMap)
            StatisticsUiState(
                month = month,
                selectedType = _selectedType.value,
                expenseSlices = expenseSlices,
                incomeSlices = incomeSlices,
                totalExpense = expenseSums.sumOf { it.total },
                totalIncome = incomeSums.sumOf { it.total }
            )
        }

    private fun toSlices(sums: List<CategorySum>, catMap: Map<Long, CategoryEntity>): List<CategorySlice> {
        val total = sums.sumOf { it.total }
        if (total <= 0.0) return emptyList()
        return sums
            .filter { it.total > 0 }
            .sortedByDescending { it.total }
            .map { sum ->
                CategorySlice(
                    category = sum.categoryId?.let { catMap[it] },
                    amount = sum.total,
                    percent = (sum.total / total).toFloat()
                )
            }
    }

    fun changeMonth(month: YearMonth) {
        _month.value = month
    }

    fun setSelectedType(type: TransactionType) {
        _selectedType.value = type
        _uiState.value = _uiState.value.copy(selectedType = type)
    }
}
