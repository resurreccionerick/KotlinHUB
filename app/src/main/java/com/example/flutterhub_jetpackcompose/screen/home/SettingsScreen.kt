package com.example.flutterhub_jetpackcompose.screen.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk


@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AppViewModel,
    context: Context,
) {

    val userModel = Hawk.get<UserModel>("user_details")
    val darkModeState = viewModel.darkModeState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-15).dp), // Shifts the image upwards by 5dp,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Card box for the banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.kotlin_settings),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {

            Column(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
            ) {
                // Switch for toggling theme
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (darkModeState) "Light Mode" else "Dark Mode",
                        modifier = Modifier.padding(12.dp)
                    )
                    Switch(
                        checked = darkModeState,
                        onCheckedChange = { isChecked ->
                            // Toggle the theme state in ViewModel
                            viewModel.toggleDarkMode(isChecked)
                        }
                    )
                }
            }
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(
//                ) {
//                    // Switch for toggling theme
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            if (darkModeState) "Light Mode" else "Dark Mode",
//                            modifier = Modifier.padding(12.dp)
//                        )
//                        Switch(
//                            checked = darkModeState,
//                            onCheckedChange = { isChecked ->
//                                // Toggle the theme state in ViewModel
//                                viewModel.toggleDarkMode(isChecked)
//                            }
//                        )
//                    }
//                }
//            }


            Card(
                onClick = {
                    navController.navigate("profile")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(text = "Profile", modifier = Modifier.padding(12.dp))
            }

            Card(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(text = "About Us", modifier = Modifier.padding(12.dp))
            }
        }


    }
}
