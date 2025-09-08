package com.developer.randomusers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> CoroutineScope.observeFlow(
    flow: Flow<T>,
    testDispatcher: CoroutineDispatcher
) {
    val job = launch(testDispatcher) {
        flow.collect {  }
    }
    job.cancel()
}