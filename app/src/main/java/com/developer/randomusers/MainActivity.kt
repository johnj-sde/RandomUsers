package com.developer.randomusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.developer.randomusers.ui.screens.NavHostContainer
import com.developer.randomusers.ui.theme.RandomUsersTheme
import com.developer.randomusers.ui.viewmodel.UserViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val userViewModel by inject<UserViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUsersTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    Column(
                        modifier = Modifier.padding(padding)
                    ) {
                        NavHostContainer(userViewModel)
                    }
                }

            }
        }
    }
}
