package com.example.flutterhub_jetpackcompose.screen.login_register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AppViewModel, context: Context) {

    var email by remember { mutableStateOf("esr@gmail.com") }
    var password by remember { mutableStateOf("Test123!") }
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent overlay
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("LOGIN")

        if (!isLoading) {

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = {
                navController.navigate("forgotPassword")
            }, modifier = Modifier.align(Alignment.End)) {
                Text("Forgot your password?")
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(onClick = {
                navController.navigate("signup")
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Don't have an account yet? Sign up now")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true

                    viewModel.userLogin(email, password,
                        onSuccess = {
                            isLoading = false
                            navController.navigate("adminHome") //callback
                        },
                        onFailure = { errorMsg ->
                            isLoading = false
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                        })
                } else {
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Sign In")
            }
        }
    }
}