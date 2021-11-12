package com.example.tipjar.paymentsHistory.entities

sealed class OrderType(open val orderBy: OrderBy) {
    data class Ascending(override val orderBy: OrderBy) : OrderType(orderBy)
    data class Descending(override val orderBy: OrderBy) : OrderType(orderBy)
}