package com.example.flutterhub_jetpackcompose.screen.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk


@Composable
fun SettingsScreen(navController: NavController, viewModel: AppViewModel, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Card box for the banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.kotlin),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            val userModel = Hawk.get<UserModel>("user_details")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(text = "Name: ${userModel.name}", modifier = Modifier.padding(16.dp))
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(text = "Email: ${userModel.email}", modifier = Modifier.padding(16.dp))
            }


        }

        // Logout button
        Button(
            onClick = {
                viewModel.userLogout(onSuccess = {
                    Hawk.deleteAll()
                    navController.navigate("login")
                }, onFailure = { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Logout", color = Color.White)
        }
    }
}
