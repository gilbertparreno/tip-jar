package com.example.tipjar.paymentsHistory.fragments

import android.content.Context
import android.os.Bundle
import com.evernote.android.state.State
import com.example.tipjar.R
import com.example.tipjar.TipJarApplication
import com.example.tipjar.core.base.BaseFragmentLifeCycle
import com.example.tipjar.core.bundlers.OrderTypeBundler
import com.example.tipjar.core.extensions.addFragmentVertically
import com.example.tipjar.core.extensions.showErrorSnackbar
import com.example.tipjar.core.helpers.ToastHelper
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.paymentDetails.fragments.PaymentDetailsFragment
import com.example.tipjar.paymentsHistory.entities.OrderBy.DATE
import com.example.tipjar.paymentsHistory.entities.OrderType
import com.example.tipjar.paymentsHistory.entities.OrderType.Descending
import com.example.tipjar.paymentsHistory.viewModels.PaymentsHistoryViewModel
import com.example.tipjar.paymentsHistory.views.PaymentsHistoryView
import com.example.tipjar.paymentsHistory.views.PaymentsHistoryViewDelegate

class PaymentsHistoryFragment :
    BaseFragmentLifeCycle<PaymentsHistoryViewModel, PaymentsHistoryView>(),
    PaymentsHistoryViewDelegate, PaymentFilterBottomSheetDialogFragmentDelegate {

    @State(OrderTypeBundler::class) var filter: OrderType? = null
        set(value) {
            field = value
            viewModel.getPaymentsHistory(value!!)
        }
        get() {
            if (field == null) {
                return Descending(DATE)
            }
            return field
        }

    override fun inject() {
        TipJarApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        context: Context,
        savedInstanceState: Bundle?
    ) = PaymentsHistoryView(context).also {
        it.delegate = this
    }

    override fun onViewCreated(contentView: PaymentsHistoryView, savedInstanceState: Bundle?) {
        viewModel.getPaymentsHistory(filter!!)
    }

    override fun observerChanges() {
        viewModel.getPaymentsHistoryEvent.observe(this) {
            when (it) {
                is TaskStatus.SuccessWithResult -> {
                    contentView.setListItems(it.result)
                }
                is TaskStatus.Failure -> {
                    contentView.showErrorSnackbar(getString(R.string.generic_error))
                }
            }
        }
        viewModel.deletePaymentEvent.observe(this) {
            when (it) {
                is TaskStatus.SuccessWithResult -> ToastHelper.showSuccessToast(
                    contentView.context,
                    if (it.result > 1) {
                        R.string.delete_payments_success_message
                    } else {
                        R.string.delete_payment_success_message
                    }
                )
                is TaskStatus.Failure -> {
                    contentView.showErrorSnackbar(getString(R.string.generic_error))
                }
            }
        }
    }

    // PaymentsHistoryViewDelegate

    override fun onViewBackPressed() {
        super.onBackPressed()
    }

    override fun onItemSelected(tipHistory: TipHistory) {
        parentFragmentManager.beginTransaction().addFragmentVertically(
            containerId = R.id.mainFragmentContainer,
            fragmentClass = PaymentDetailsFragment::class.java,
            addToBackStack = true,
            bundle = Bundle().also { it.putInt("tip_history_id", tipHistory.id) }
        ).commit()
    }

    override fun onItemDelete(vararg ids: Int) {
        viewModel.deletePayment(ids.toList(), filter!!)
    }

    override fun onShowFilter() {
        val bottomSheet = PaymentFilterBottomSheetDialogFragment().also {
            it.delegate = this
        }
        bottomSheet.show(
            parentFragmentManager,
            PaymentFilterBottomSheetDialogFragment::class.simpleName?.lowercase()
        )
    }

    // PaymentFilterBottomSheetDialogFragmentDelegate

    override fun onUpdateFilter(orderType: OrderType) {
        filter = orderType
    }

    override fun getActiveFilter() = filter!!
}