package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileComponent(navController: NavController, viewModel: AppViewModel, context: Context) {
    val openDialog = remember { mutableStateOf(false) }
    val userModel = Hawk.get<UserModel>("user_details")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Profile") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        },


        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(text = "Name: ${userModel.name}", modifier = Modifier.padding(12.dp))
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(text = "Email: ${userModel.email}", modifier = Modifier.padding(12.dp))
                    }

                    Card(
                        onClick = {
                            openDialog.value = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(text = "Reset Password", modifier = Modifier.padding(12.dp))
                    }
                }


//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 32.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
//                    elevation = CardDefaults.cardElevation(4.dp),
//                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary)
//                ) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
//                        elevation = CardDefaults.cardElevation(4.dp)
//                    ) {
//                        Text(
//                            text = "Delete Account", modifier = Modifier.padding(12.dp),
//                            color = Color.Red
//                        )
//                    }
//                }

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
                    Text("Logout", color = Color.White, modifier = Modifier.padding(8.dp))
                }

                if (openDialog.value) {
                    ChangePassAlertDialog(viewModel,
                        context = context, onDismiss = { openDialog.value = false }
                    )
                }
            }
        })
}
