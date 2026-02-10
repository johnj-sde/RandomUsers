package com.developer.randomusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.developer.randomusers.ui.navigation.NavRoot
import com.developer.randomusers.ui.theme.RandomUsersTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUsersTheme {
                NavRoot(deeplink = intent.data)
            }
        }
    }

}
