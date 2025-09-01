package com.developer.randomusers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.Dispatcher
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class CoroutineTestExtension: BeforeAllCallback, AfterAllCallback {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}