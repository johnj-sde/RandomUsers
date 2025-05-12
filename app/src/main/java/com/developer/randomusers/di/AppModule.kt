package com.developer.randomusers.di

import com.developer.randomusers.network.API_URL
import com.developer.randomusers.network.RandomUserAPIClient
import com.developer.randomusers.ui.screens.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
    single {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RandomUserAPIClient::class.java)
    }

    viewModel<MainViewModel> {
        MainViewModel(get())
    }


}