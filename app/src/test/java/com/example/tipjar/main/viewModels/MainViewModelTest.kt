package com.example.tipjar.main.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tipjar.core.taskStatus.TaskStatus
import com.example.tipjar.database.entities.TipHistory
import com.example.tipjar.database.repositories.TipHistoryRepository
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
import org.mockito.ArgumentMatchers.any

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK private lateinit var tipHistoryRepository: TipHistoryRepository

    private lateinit var viewModel: MainViewModel
    private lateinit var testCoroutineContextProvider: TestCoroutineContextProvider

    private lateinit var testSaveTipEvent: TestObserver<TaskStatus<Any>>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        testCoroutineContextProvider = TestCoroutineContextProvider(testCoroutineRule)
        viewModel = MainViewModel(
            testCoroutineContextProvider,
            tipHistoryRepository
        ).apply {
            testSaveTipEvent = saveTipEvent.test()
        }
    }

    @Test
    fun `saveTip()`() = testCoroutineRule.runBlockingTest {
        val testData: TipHistory = mockk()
        coEvery { tipHistoryRepository.insert(testData) } just Runs
        viewModel.saveTip(testData)
        coVerify {
            tipHistoryRepository.insert(testData)
        }
        testSaveTipEvent.assertValue(TaskStatus.success(any()))
    }
}