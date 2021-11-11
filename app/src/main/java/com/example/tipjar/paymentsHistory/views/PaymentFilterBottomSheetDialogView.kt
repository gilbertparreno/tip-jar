package com.example.tipjar.paymentsHistory.views

import android.content.Context
import android.widget.FrameLayout
import com.example.tipjar.R
import com.example.tipjar.core.extensions.setDebounceClickListener
import com.example.tipjar.paymentsHistory.entities.OrderBy.DATE
import com.example.tipjar.paymentsHistory.entities.OrderBy.PAYMENT_AMOUNT
import com.example.tipjar.paymentsHistory.entities.OrderType
import com.example.tipjar.paymentsHistory.entities.OrderType.Ascending
import com.example.tipjar.paymentsHistory.entities.OrderType.Descending
import kotlinx.android.synthetic.main.view_payment_filter_bottom_sheet.view.*

interface PaymentFilterBottomSheetDialogViewDelegate {
    fun onUpdateFilter(orderType: OrderType)
}

class PaymentFilterBottomSheetDialogView(context: Context) : FrameLayout(context) {

    var delegate: PaymentFilterBottomSheetDialogViewDelegate? = null

    init {
        inflate(context, R.layout.view_payment_filter_bottom_sheet, this)
        updateFilter.setDebounceClickListener {
            delegate?.onUpdateFilter(
                when (sortBy.checkedRadioButtonId) {
                    R.id.dateAscending -> Ascending(DATE)
                    R.id.paymentAscending -> Ascending(PAYMENT_AMOUNT)
                    R.id.paymentDescending -> Descending(PAYMENT_AMOUNT)
                    else -> Descending(DATE)
                }
            )
        }
    }

    fun setUpView(orderType: OrderType) {
        val radioButton = when (orderType) {
            Ascending(DATE) -> dateAscending
            Ascending(PAYMENT_AMOUNT) -> paymentAscending
            Descending(PAYMENT_AMOUNT) -> paymentDescending
            else -> dateDescending
        }
        radioButton.isChecked = true
    }
}