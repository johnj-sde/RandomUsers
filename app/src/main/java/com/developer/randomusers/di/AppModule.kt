package com.developer.randomusers.di

import com.developer.randomusers.network.API_URL
import com.developer.randomusers.network.RandomUserAPIClient
import com.developer.randomusers.ui.screens.MainViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val json = Json { ignoreUnknownKeys = true }

val appModules = module {
    single {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // .addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RandomUserAPIClient::class.java)
    }

    viewModel<MainViewModel> {
        MainViewModel(get())
    }
}