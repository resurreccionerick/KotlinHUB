package com.example.flutterhub_jetpackcompose.screen.user.quiz

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun SettingsScreen(navController: NavController, viewModel: AppViewModel, context: Context) {

    Scaffold(content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    viewModel.userLogout(onSuccess = {
                        Hawk.deleteAll()
                        navController.navigate("login")
                    }, onFailure = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    })
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) { Text("Logout") }
        }
    }


    )
}