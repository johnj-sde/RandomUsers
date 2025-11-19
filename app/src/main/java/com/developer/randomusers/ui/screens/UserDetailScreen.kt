package com.developer.randomusers.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.developer.randomusers.R
import com.developer.randomusers.model.User
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.ui.screens.previewutils.previewUser1
import com.developer.randomusers.ui.viewmodel.UserViewModel

@Composable
fun UserDetailScreenScaffold(
    viewModel: UserViewModel,
    position: Int,
    handleBackPressed: () -> Unit){
    val usersState by viewModel.usersState.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {
        viewModel.setNavigationResultToBackPressed()
        handleBackPressed()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        if (usersState.users.isNotEmpty() && position>=0) {
            val user = usersState.users[position]
            UserDetailScreen(user, paddingValues)
        }
    }
}

@Composable
private fun UserDetailScreen(
    user: User,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailScreenPreview() {
    UserDetailScreen(user = previewUser1, PaddingValues())
}
