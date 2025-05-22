package com.developer.randomusers.ui.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class UserViewModel(
    val restClient: RandomUserAPIClient
): ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    val lazyListState = LazyListState()

    init {

        viewModelScope.launch {
            infiniteScrollFlow()
        }

    }

    private suspend fun infiniteScrollFlow() {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                lastVisibleItemIndex.let { index ->
                    if (users.value.size - index <= 20) {
                        fetchUsers()
                    }
                }
            }
    }

    fun fetchUsers() {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { restClient.fetchUsers(40) }
            }
            val newUsersList = if (result.isSuccess) {
                result.getOrNull()?.users ?: emptyList()
            } else {
                emptyList()
            }
            _users.update { currUsersList ->
                val mutableList = currUsersList.toMutableList()
                mutableList.addAll(newUsersList)
                mutableList.toList()
            }
        }

    }

}