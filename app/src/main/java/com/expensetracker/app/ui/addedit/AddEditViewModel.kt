package com.expensetracker.app.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.local.entity.TransactionEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.repository.CategoryRepository
import com.expensetracker.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

data class AddEditUiState(
    val isEditMode: Boolean = false,
    val type: TransactionType = TransactionType.EXPENSE,
    val amountText: String = "",
    val selectedCategory: CategoryEntity? = null,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val note: String = "",
    val categories: List<CategoryEntity> = emptyList(),
    val recentCategories: List<CategoryEntity> = emptyList(),
    val isSaved: Boolean = false,
    val error: String? = null
)

class AddEditViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val editingId: Long?
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState(isEditMode = editingId != null))
    val uiState: StateFlow<AddEditUiState> = _uiState

    init {
        observeCategories()
        editingId?.let { loadForEdit(it) }
    }

    private fun observeCategories() {
        categoryRepository.getByType(_uiState.value.type)
            .onEach { list ->
                _uiState.value = _uiState.value.copy(
                    categories = list,
                    recentCategories = list.take(5)
                )
            }
            .launchIn(viewModelScope)
    }

    private fun loadForEdit(id: Long) {
        viewModelScope.launch {
            val existing = transactionRepository.getById(id) ?: return@launch
            _uiState.value = _uiState.value.copy(
                type = existing.transaction.type,
                amountText = existing.transaction.amount.toString(),
                selectedCategory = existing.category,
                date = LocalDate.ofEpochDay(existing.transaction.dateEpochDay),
                time = LocalTime.of(
                    existing.transaction.timeMinutes / 60,
                    existing.transaction.timeMinutes % 60
                ),
                note = existing.transaction.note
            )
            reloadCategoriesForType(existing.transaction.type)
        }
    }

    private fun reloadCategoriesForType(type: TransactionType) {
        categoryRepository.getByType(type)
            .onEach { list ->
                _uiState.value = _uiState.value.copy(categories = list, recentCategories = list.take(5))
            }
            .launchIn(viewModelScope)
    }

    fun setType(type: TransactionType) {
        _uiState.value = _uiState.value.copy(type = type, selectedCategory = null)
        reloadCategoriesForType(type)
    }

    fun setAmountText(value: String) {
        // only allow digits and one decimal point
        val filtered = value.filterIndexed { index, c ->
            c.isDigit() || (c == '.' && !value.substring(0, index).contains('.'))
        }
        _uiState.value = _uiState.value.copy(amountText = filtered)
    }

    fun setCategory(category: CategoryEntity) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun setTime(time: LocalTime) {
        _uiState.value = _uiState.value.copy(time = time)
    }

    fun setNote(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }

    fun save() {
        val state = _uiState.value
        val amount = state.amountText.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            _uiState.value = state.copy(error = "กรุณาระบุจำนวนเงินให้ถูกต้อง")
            return
        }
        if (state.selectedCategory == null) {
            _uiState.value = state.copy(error = "กรุณาเลือกหมวดหมู่")
            return
        }

        viewModelScope.launch {
            val entity = TransactionEntity(
                id = editingId ?: 0,
                type = state.type,
                amount = amount,
                categoryId = state.selectedCategory.id,
                dateEpochDay = state.date.toEpochDay(),
                timeMinutes = state.time.hour * 60 + state.time.minute,
                note = state.note
            )
            if (editingId != null) {
                transactionRepository.update(entity)
            } else {
                transactionRepository.add(entity)
            }
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
