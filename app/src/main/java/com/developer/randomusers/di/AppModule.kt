package com.developer.randomusers.di

import androidx.room.Room
import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.network.API_URL
import com.developer.randomusers.network.RandomUserAPIClient
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.ui.viewmodel.UserViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
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

    single {
        Room.databaseBuilder(
            context = androidContext(),
            AppDatabase::class.java, "database-name"
        ).build()
    }

    single {
        UserRepository(restClient = get(), database = get())
    }

    viewModel<UserViewModel> {
        UserViewModel(get())
    }
}