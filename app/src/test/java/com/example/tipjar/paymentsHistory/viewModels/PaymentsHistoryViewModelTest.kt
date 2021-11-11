package com.example.tipjar.paymentsHistory.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.repositories.TipHistoryRepository
import com.example.tipjar.paymentsHistory.entities.OrderBy
import com.example.tipjar.paymentsHistory.entities.OrderType
import com.example.tipjar.paymentsHistory.entities.TipHistoryItem
import com.example.tipjar.utils.TestCoroutineRule
import com.example.tipjar.utils.providers.TestCoroutineContextProvider
import com.jraska.livedata.TestObserver
import com.jraska.livedata.test
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PaymentsHistoryViewModelTest {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK private lateinit var tipHistoryRepository: TipHistoryRepository

    private lateinit var viewModel: PaymentsHistoryViewModel
    private lateinit var testCoroutineContextProvider: TestCoroutineContextProvider

    private lateinit var testGetPaymentsHistoryEvent: TestObserver<TaskStatus<List<TipHistoryItem>>>
    private lateinit var testDeletePaymentEvent: TestObserver<TaskStatus<Int>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        testCoroutineContextProvider = TestCoroutineContextProvider(testCoroutineRule)
        viewModel = PaymentsHistoryViewModel(
            testCoroutineContextProvider,
            tipHistoryRepository
        ).apply {
            testGetPaymentsHistoryEvent = getPaymentsHistoryEvent.test()
            testDeletePaymentEvent = deletePaymentEvent.test()
        }
    }

    @Test
    fun `getPaymentsHistory()`() = testCoroutineRule.runBlockingTest {
        val testData = OrderType.Descending(OrderBy.DATE)
        coEvery { tipHistoryRepository.findAll("payment_date", "desc") } returns listOf()
        viewModel.getPaymentsHistory(testData)
        coVerify {
            tipHistoryRepository.findAll("payment_date", "desc")
        }
        testGetPaymentsHistoryEvent.assertValue(TaskStatus.success(listOf()))
    }

    @Test
    fun `deletePayment()`() = testCoroutineRule.runBlockingTest {
        val testData = OrderType.Descending(OrderBy.DATE)
        coEvery { tipHistoryRepository.delete(listOf()) } just Runs
        coEvery { tipHistoryRepository.findAll("payment_date", "desc") } returns listOf()
        viewModel.deletePayment(listOf(), testData)
        coVerifyOrder {
            tipHistoryRepository.delete(listOf())
            tipHistoryRepository.findAll("payment_date", "desc")
        }
        testGetPaymentsHistoryEvent.assertValue(TaskStatus.success(listOf()))
        testDeletePaymentEvent.assertValue(TaskStatus.success(0))
    }
}