package com.developer.randomusers.ui.navigation


sealed class NavResult {
    object BackPressed : NavResult()
    object Idle : NavResult()
}