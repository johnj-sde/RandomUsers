package com.developer.randomusers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import com.developer.randomusers.ui.navigation.NavRoot
import com.developer.randomusers.ui.theme.RandomUsersTheme

class MainActivity : ComponentActivity() {

    private var intentUri = mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intentUri.value = intent.data
        enableEdgeToEdge()
        setContent {
            RandomUsersTheme {
                NavRoot(deeplink = intentUri.value) {
                    intentUri.value = null
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentUri.value = intent.data
    }

}
/*TODO:

•Scenario B: Filtering the list.•Challenge: Add a TextField at the top to filter users by country or age.•What they are testing: Use of derivedStateOf in Compose or combine in Kotlin Flows to filter data in the ViewModel.
•Scenario C: Image Loading & Performance.•Challenge: Handling large lists of images (like hotel rooms or user avatars).•What they are testing: Coil/Glide integration and memory management.

 */