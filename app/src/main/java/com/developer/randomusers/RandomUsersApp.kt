package com.developer.randomusers

import android.app.Application
import com.developer.randomusers.di.appModules
import org.koin.core.context.startKoin

class RandomUsersApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(appModules)
        }
    }

}