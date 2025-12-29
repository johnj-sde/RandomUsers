package com.developer.randomusers

import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.network.MqttClientManager
import com.developer.randomusers.network.MqttClientManagerInterface
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.repository.MqttEventRepository
import com.developer.randomusers.repository.MqttEventRepositoryInterface
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.repository.UserRepositoryInterface
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testAppModule = module {

    single<RandomUserAPIClientInterface> {
        InMemoryRESTClient()
    }

    single<MqttClientManagerInterface> {
        fakeEmptyMqttClientManager
    }

    single<AppDatabaseInterface> {
        FakeAppDatabase(FakeUserDao(storedUsers = listOf(fakeUserEntity)))
    }

    single<UserRepositoryInterface> {
        UserRepository(restClient = get(), database = get())
    }

    single<MqttEventRepositoryInterface> {
        MqttEventRepository(
            mqttClientManager = get()
        )
    }

    viewModel { UserViewModel(get(), get(), Dispatchers.IO) }

}
