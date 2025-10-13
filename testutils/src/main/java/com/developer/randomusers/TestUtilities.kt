package com.developer.randomusers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

fun <T> CoroutineScope.observeFlow(
    flow: Flow<T>,
    testDispatcher: CoroutineDispatcher,
    dropInitialValue: Boolean = true,
    action: () -> Unit
): List<T> {
    val collection = mutableListOf<T>()
    val job = launch(testDispatcher) {
        flow.toCollection(collection)
    }
    action()
    job.cancel()
    return if (dropInitialValue) collection.drop(1) else collection
}