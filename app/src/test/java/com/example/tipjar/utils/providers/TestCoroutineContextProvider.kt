package com.example.tipjar.utils.providers

import com.example.tipjar.core.providers.CoroutineContextProvider
import com.example.tipjar.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestCoroutineContextProvider(
    testCoroutineRule: TestCoroutineRule
) : CoroutineContextProvider() {
    override var Main: CoroutineContext = testCoroutineRule.testCoroutineDispatcher
    override var IO: CoroutineContext = testCoroutineRule.testCoroutineDispatcher
}