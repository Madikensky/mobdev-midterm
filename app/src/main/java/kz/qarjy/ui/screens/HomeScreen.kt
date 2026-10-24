package kz.qarjy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.qarjy.ui.QarjyUiState
import kz.qarjy.ui.components.CategoryBarRow
import kz.qarjy.ui.components.EmptyState
import kz.qarjy.ui.components.ExpenseItem
import kz.qarjy.ui.components.LoadingBox
import kz.qarjy.ui.components.SummaryCard

/** Stateless: draws [state], reports navigation intent through [onSeeAll]. */
@Composable
fun HomeScreen(
    state: QarjyUiState,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        LoadingBox(modifier)
        return
    }

    val maxCategoryAmount = state.byCategory.maxOfOrNull { it.second } ?: 1

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        // bottom padding keeps the last item clear of the floating action button
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SummaryCard(
                title = "Spent in the last 30 days",
                amount = state.totalLast30Days,
                subtitle = "${state.countLast30Days} expenses"
            )
        }

        item { Text("By category", style = MaterialTheme.typography.titleMedium) }

        if (state.byCategory.isEmpty()) {
            item { EmptyState("No expenses yet. Tap + to add the first one.") }
        } else {
            items(state.byCategory, key = { it.first }) { (category, amount) ->
                CategoryBarRow(
                    category = category,
                    amount = amount,
                    fraction = amount.toFloat() / maxCategoryAmount
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recent", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onSeeAll) { Text("See all") }
            }
        }

        items(state.recent, key = { it.id }) { expense ->
            ExpenseItem(expense)
        }
    }
}
