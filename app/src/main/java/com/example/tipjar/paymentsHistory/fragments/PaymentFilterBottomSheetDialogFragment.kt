package com.example.tipjar.paymentsHistory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.tipjar.paymentsHistory.entities.OrderType
import com.example.tipjar.paymentsHistory.views.PaymentFilterBottomSheetDialogView
import com.example.tipjar.paymentsHistory.views.PaymentFilterBottomSheetDialogViewDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface PaymentFilterBottomSheetDialogFragmentDelegate {
    fun onUpdateFilter(orderType: OrderType)
    fun getActiveFilter(): OrderType
}

class PaymentFilterBottomSheetDialogFragment : BottomSheetDialogFragment(),
    PaymentFilterBottomSheetDialogViewDelegate {

    var delegate: PaymentFilterBottomSheetDialogFragmentDelegate? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PaymentFilterBottomSheetDialogView(inflater.context).also { view ->
        view.delegate = this
        delegate?.getActiveFilter()?.let {
            view.setUpView(it)
        }
    }

    // PaymentFilterBottomSheetDialogViewDelegate

    override fun onUpdateFilter(orderType: OrderType) {
        delegate?.onUpdateFilter(orderType)
        dismiss()
    }
}