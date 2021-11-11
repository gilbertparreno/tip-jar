package com.example.tipjar.core.bundlers

import android.os.Bundle
import com.evernote.android.state.Bundler
import com.example.tipjar.paymentsHistory.entities.OrderBy
import com.example.tipjar.paymentsHistory.entities.OrderType
import com.example.tipjar.paymentsHistory.entities.OrderType.Ascending
import com.example.tipjar.paymentsHistory.entities.OrderType.Descending

class OrderTypeBundler : Bundler<OrderType> {

    override fun put(key: String, value: OrderType, bundle: Bundle) {
        bundle.putString(
            "order_type",
            if (value is Descending) "desc" else "asc"
        )
        bundle.putString("order_by", value.orderBy.value)
    }

    override fun get(key: String, bundle: Bundle): OrderType {
        val orderType = bundle.getString("order_type")
        val orderBy = OrderBy.getByValue(bundle.getString("order_by")!!)
        return if (orderType == "asc") {
            Ascending(orderBy)
        } else {
            Descending(orderBy)
        }
    }
}