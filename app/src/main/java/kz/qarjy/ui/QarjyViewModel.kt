package kz.qarjy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.qarjy.data.Category
import kz.qarjy.data.Expense
import kz.qarjy.data.ExpenseRepository
import java.time.LocalDate

data class QarjyUiState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true,
    val query: String = "",
    val selectedCategory: Category? = null
) {
    private val newestFirst: List<Expense>
        get() = expenses.sortedWith(compareByDescending<Expense> { it.date }.thenByDescending { it.id })

    val visibleExpenses: List<Expense>
        get() = newestFirst.filter { e ->
            (selectedCategory == null || e.category == selectedCategory) &&
                (e.note.contains(query.trim(), ignoreCase = true) ||
                    e.category.title.contains(query.trim(), ignoreCase = true))
        }

    private val last30Days: List<Expense>
        get() {
            val from = LocalDate.now().minusDays(29)
            return expenses.filter { !it.date.isBefore(from) }
        }

    val totalLast30Days: Int get() = last30Days.sumOf { it.amount }
    val countLast30Days: Int get() = last30Days.size

    val byCategory: List<Pair<Category, Int>>
        get() = last30Days
            .groupBy { it.category }
            .map { (category, list) -> category to list.sumOf { it.amount } }
            .sortedByDescending { it.second }

    val recent: List<Expense> get() = newestFirst.take(3)
}

class QarjyViewModel : ViewModel() {

    private val repository = ExpenseRepository()

    private val _uiState = MutableStateFlow(QarjyUiState())
    val uiState: StateFlow<QarjyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val loaded = repository.load()
            _uiState.update { it.copy(expenses = loaded, isLoading = false) }
        }
    }

    fun onQueryChange(query: String) = _uiState.update { it.copy(query = query) }

    fun onCategorySelected(category: Category?) =
        _uiState.update { it.copy(selectedCategory = category) }

    fun addExpense(amount: Int, category: Category, note: String) {
        val updated = repository.add(amount, category, note)
        _uiState.update { it.copy(expenses = updated) }
    }

    fun deleteExpense(id: Int) {
        val updated = repository.delete(id)
        _uiState.update { it.copy(expenses = updated) }
    }
}
