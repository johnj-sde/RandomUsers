package com.developer.randomusers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.developer.randomusers.R
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.ui.viewmodel.UserViewModel

@Composable
fun UserDetailScreen(
    viewModel: UserViewModel,
    position: Int
) {
    val users = viewModel.usersState.collectAsStateWithLifecycle()
    if (users.value.users.isNotEmpty() && position>=0) {
        val user = users.value.users[position]
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ){
            Box (
                modifier = Modifier.fillMaxWidth().weight(0.5f).padding(10.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (user.picture?.large != null) {
                    AsyncImage(
                        model = user.picture.large,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.anonymous_avatar),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().weight(0.3f).padding(bottom=10.dp)
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

/*                if (user.registered != null && user.registered.date !=null) {
                    Text(
                        user.registered.date,
                        textAlign = TextAlign.Center
                    )
                }*/

                /*
                val displayLocation = user.location?.displayLocation()
                if (displayLocation!=null) {
                    Text(
                        text = displayLocation,
                        textAlign = TextAlign.Center
                    )
                }*/
            }
        }
    }

}
