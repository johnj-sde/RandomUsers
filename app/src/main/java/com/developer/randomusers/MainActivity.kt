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

•Scenario A: Adding a "Favorite" toggle.•Challenge: You click a heart on a user in the list. It must update the Room database and reflect immediately in the User Details screen.•What they are testing: Do you understand Unidirectional Data Flow (UDF)? Do you update the list efficiently or reload the whole thing?
•Scenario B: Filtering the list.•Challenge: Add a TextField at the top to filter users by country or age.•What they are testing: Use of derivedStateOf in Compose or combine in Kotlin Flows to filter data in the ViewModel.
•Scenario C: Image Loading & Performance.•Challenge: Handling large lists of images (like hotel rooms or user avatars).•What they are testing: Coil/Glide integration and memory management.

Want to try a mock "Live Code" task? Try this: Modify your UserList screen so that when a user is "deleted," a Snackbar appears with an "Undo" button. (This tests Room, ViewModel state, and Compose UI coordination).
 */