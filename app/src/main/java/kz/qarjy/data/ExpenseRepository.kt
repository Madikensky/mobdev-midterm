package kz.qarjy.data

import kotlinx.coroutines.delay
import java.time.LocalDate

class ExpenseRepository {
    private val items = mutableListOf<Expense>()
    private var nextId = 1

    suspend fun load(): List<Expense> {
        delay(400)
        if (items.isEmpty()) seed()
        return items.toList()
    }

    fun add(amount: Int, category: Category, note: String): List<Expense> {
        items.add(Expense(nextId++, amount, category, note.trim(), LocalDate.now()))
        return items.toList()
    }

    fun delete(id: Int): List<Expense> {
        items.removeAll { it.id == id }
        return items.toList()
    }

    private fun seed() {
        val today = LocalDate.now()
        listOf(
            Triple(12_450, Category.FOOD, "Groceries" to 0),
            Triple(1_800, Category.TRANSPORT, "Taxi to work" to 1),
            Triple(4_290, Category.FUN, "Streaming subscription" to 2),
            Triple(3_500, Category.BILLS, "Mobile plan" to 3),
            Triple(1_300, Category.FOOD, "Coffee" to 3),
            Triple(3_000, Category.FUN, "Cinema" to 5),
            Triple(18_900, Category.SHOPPING, "Sneakers" to 7),
            Triple(6_200, Category.BILLS, "Electricity" to 9),
            Triple(2_800, Category.FOOD, "Lunch with friends" to 11),
            Triple(3_700, Category.OTHER, "Pharmacy" to 14)
        ).forEach { (amount, category, noteAndAge) ->
            items.add(
                Expense(nextId++, amount, category, noteAndAge.first, today.minusDays(noteAndAge.second.toLong()))
            )
        }
    }
}
