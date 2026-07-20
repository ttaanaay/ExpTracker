package com.expensetracker.app.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.data.local.entity.CategoryEntity
import com.expensetracker.app.data.model.TransactionType
import com.expensetracker.app.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class CategoryUiState(
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val expenseCategories: List<CategoryEntity> = emptyList(),
    val incomeCategories: List<CategoryEntity> = emptyList(),
    val deleteBlockedMessage: String? = null
)

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState

    init {
        combine(
            repository.getByType(TransactionType.EXPENSE),
            repository.getByType(TransactionType.INCOME)
        ) { expense, income ->
            _uiState.value.copy(expenseCategories = expense, incomeCategories = income)
        }.onEach { _uiState.value = it }.launchIn(viewModelScope)
    }

    fun setSelectedType(type: TransactionType) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun addCategory(name: String, icon: String, color: Long, type: TransactionType) {
        viewModelScope.launch {
            repository.add(CategoryEntity(name = name, type = type, icon = icon, color = color))
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch { repository.update(category) }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            val deleted = repository.delete(category)
            if (!deleted) {
                _uiState.value = _uiState.value.copy(
                    deleteBlockedMessage = "ไม่สามารถลบได้ เนื่องจากมีรายการที่ใช้หมวดหมู่นี้อยู่"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(deleteBlockedMessage = null)
    }
}
