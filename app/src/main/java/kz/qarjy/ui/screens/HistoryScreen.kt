package kz.qarjy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.qarjy.data.Category
import kz.qarjy.ui.QarjyUiState
import kz.qarjy.ui.components.CategoryFilterChip
import kz.qarjy.ui.components.EmptyState
import kz.qarjy.ui.components.ExpenseItem
import kz.qarjy.ui.components.LoadingBox
import kz.qarjy.ui.components.SearchField

@Composable
fun HistoryScreen(
    state: QarjyUiState,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (Category?) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        LoadingBox(modifier)
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchField(
            query = state.query,
            onQueryChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                CategoryFilterChip("All", state.selectedCategory == null) { onCategorySelected(null) }
            }
            items(Category.entries) { category ->
                CategoryFilterChip(
                    label = "${category.emoji} ${category.title}",
                    selected = state.selectedCategory == category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        val visible = state.visibleExpenses
        if (visible.isEmpty()) {
            EmptyState("No expenses found")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visible, key = { it.id }) { expense ->
                    ExpenseItem(expense = expense, onDelete = { onDelete(expense.id) })
                }
            }
        }
    }
}
