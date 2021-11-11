package com.example.tipjar.paymentsHistory.entities

sealed class OrderType(val orderBy: OrderBy) {
    data class Ascending(val order: OrderBy) : OrderType(order)
    data class Descending(val order: OrderBy) : OrderType(order)
}