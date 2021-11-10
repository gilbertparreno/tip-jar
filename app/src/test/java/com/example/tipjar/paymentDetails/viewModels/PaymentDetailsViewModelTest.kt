package com.example.tipjar.paymentDetails.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.database.repositories.TipHistoryRepository
import com.example.tipjar.utils.TestCoroutineRule
import com.example.tipjar.utils.providers.TestCoroutineContextProvider
import com.jraska.livedata.TestObserver
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PaymentDetailsViewModelTest {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK private lateinit var tipHistoryRepository: TipHistoryRepository

    private lateinit var viewModel: PaymentDetailsViewModel
    private lateinit var testCoroutineContextProvider: TestCoroutineContextProvider

    private lateinit var testPaymentEvent: TestObserver<TaskStatus<TipHistory>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        testCoroutineContextProvider = TestCoroutineContextProvider(testCoroutineRule)
        viewModel = PaymentDetailsViewModel(
            testCoroutineContextProvider,
            tipHistoryRepository
        ).apply {
            testPaymentEvent = paymentEvent.test()
        }
    }

    @Test
    fun `getPaymentById()`() = testCoroutineRule.runBlockingTest {
        val testData: TipHistory = mockk()
        coEvery { tipHistoryRepository.find(0) } returns testData
        viewModel.getPaymentById(0)
        coVerify {
            tipHistoryRepository.find(0)
        }
        testPaymentEvent.assertValue(TaskStatus.success(testData))
    }
}