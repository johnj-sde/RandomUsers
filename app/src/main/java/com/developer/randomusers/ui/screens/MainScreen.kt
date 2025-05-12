package com.developer.randomusers.ui.screens

import androidx.compose.runtime.Composable
import com.developer.randomusers.network.RandomUserAPIClient

@Composable
fun MainScreen(

) {

}


class Fetcher(
    val restClient: RandomUserAPIClient
) {
    fun fetchUsers() {
        restClient.fetchUsers(10)
    }
}