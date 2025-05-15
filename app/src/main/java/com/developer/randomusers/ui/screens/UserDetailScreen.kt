package com.developer.randomusers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developer.randomusers.R
import com.developer.randomusers.model.User
import com.developer.randomusers.model.displayLocation
import com.developer.randomusers.model.getFullName

@Composable
fun UserDetailScreen(
    users: List<User>,
    position: Int
) {
    if (users.isNotEmpty() && position>=0) {
        val user = users[position]
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Column (
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.anonymous_avatar),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.5f)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(bottom=10.dp)
            ){
                Text(
                    text = user.getFullName(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                if (user.email != null) {
                    Text(
                        user.email,
                        textAlign = TextAlign.Center
                    )
                }

                if (user.phone != null) {
                    Text(
                        user.phone,
                        textAlign = TextAlign.Center
                    )
                }

                if (user.gender != null) {
                    Text(
                        user.gender,
                        textAlign = TextAlign.Center
                    )
                }

                if (user.registered != null && user.registered.date !=null) {
                    Text(
                        user.registered.date,
                        textAlign = TextAlign.Center
                    )
                }

                val displayLocation = user.location?.displayLocation()
                if (displayLocation!=null) {
                    Text(
                        text = displayLocation,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}
