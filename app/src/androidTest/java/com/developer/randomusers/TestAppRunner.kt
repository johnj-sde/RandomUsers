package com.developer.randomusers

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

@SuppressWarnings("unused")
class TestAppRunner : AndroidJUnitRunner() {

    override fun newApplication(
        loader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(loader, TestApp::class.java.name, context)
    }
}