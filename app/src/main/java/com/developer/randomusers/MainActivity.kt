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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.developer.randomusers.ui.screens.MainViewModel
import com.developer.randomusers.ui.screens.NavHostContainer
import com.developer.randomusers.ui.theme.RandomUsersTheme
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val mainViewModel by inject<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUsersTheme {
                val users = mainViewModel.users.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    Column(
                        modifier = Modifier.padding(padding)
                    ) {
                        NavHostContainer(users.value)
                    }
                }

            }
        }
    }
}
