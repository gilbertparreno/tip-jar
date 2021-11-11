package com.example.tipjar.paymentsHistory.entities

enum class OrderBy(val value: String) {
    DATE("date"), PAYMENT_AMOUNT("payment_amount"), ;

    companion object {
        fun getByValue(value: String): OrderBy {
            return values().first { it.value == value }
        }
    }
}