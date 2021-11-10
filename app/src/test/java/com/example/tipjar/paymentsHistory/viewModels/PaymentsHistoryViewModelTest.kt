package com.example.tipjar.paymentsHistory.viewModels

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

    private lateinit var testPaymentsHistoryEvent: TestObserver<TaskStatus<List<TipHistory>>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        testCoroutineContextProvider = TestCoroutineContextProvider(testCoroutineRule)
        viewModel = PaymentsHistoryViewModel(
            testCoroutineContextProvider,
            tipHistoryRepository
        ).apply {
            testPaymentsHistoryEvent = paymentsHistoryEvent.test()
        }
    }

    @Test
    fun `getPaymentsHistory()`() = testCoroutineRule.runBlockingTest {
        coEvery { tipHistoryRepository.findAllSortByDate() } returns listOf()
        viewModel.getPaymentsHistory()
        coVerify {
            tipHistoryRepository.findAllSortByDate()
        }
        testPaymentsHistoryEvent.assertValue(TaskStatus.success(listOf()))
    }
}