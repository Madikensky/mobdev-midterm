package kz.qarjy.data

import java.time.LocalDate

enum class Category(val title: String, val emoji: String) {
    FOOD("Food", "🍔"),
    TRANSPORT("Transport", "🚕"),
    SHOPPING("Shopping", "🛍️"),
    FUN("Fun", "🎬"),
    BILLS("Bills", "💡"),
    OTHER("Other", "📦"),
}

data class Expense(
    val id: Int,
    val amount: Int,
    val category: Category,
    val note: String,
    val date: LocalDate
)
