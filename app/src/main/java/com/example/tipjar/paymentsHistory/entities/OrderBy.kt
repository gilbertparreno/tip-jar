package com.example.tipjar.paymentsHistory.entities

enum class OrderBy(val value: String) {
    DATE("payment_date"), PAYMENT_AMOUNT("payment"), ;

    companion object {
        fun getByValue(value: String): OrderBy {
            return values().first { it.value == value }
        }
    }
}