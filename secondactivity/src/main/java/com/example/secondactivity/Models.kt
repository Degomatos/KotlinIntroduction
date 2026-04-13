package com.example.secondactivity

data class Transaction(
    val id: Int,
    val description: String,
    val value: Double,
    val type: TransactionType // INCOME or EXPENSE
)

enum class TransactionType {
    INCOME, EXPENSE
}

data class Dream(
    val id: Int,
    val description: String,
    val targetValue: Double
)
