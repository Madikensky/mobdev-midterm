package kz.qarjy.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kz.qarjy.data.Category
import kz.qarjy.ui.components.CategoryFilterChip

@Composable
fun AddExpenseScreen(
    onSave: (amount: Int, category: Category, note: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amountText by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf(Category.FOOD) }
    var note by rememberSaveable { mutableStateOf("") }

    val amount = amountText.toIntOrNull() ?: 0
    val canSave = amount > 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Add expense", style = MaterialTheme.typography.titleLarge)
        }

        OutlinedTextField(
            value = amountText,
            onValueChange = { new ->
                if (new.length <= 9 && new.all { it.isDigit() }) amountText = new
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Amount") },
            suffix = { Text("₸") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { if (!canSave) Text("Enter an amount greater than 0") }
        )

        Text("Category", style = MaterialTheme.typography.labelLarge)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Category.entries) { option ->
                CategoryFilterChip(
                    label = "${option.emoji} ${option.title}",
                    selected = option == category,
                    onClick = { category = option }
                )
            }
        }

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Note (optional)") },
            singleLine = true
        )

        Button(
            onClick = { onSave(amount, category, note) },
            enabled = canSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
