package com.example.flutterhub_jetpackcompose.screen.login_register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LessonViewModel, context: Context) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("LOGIN")

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
                        Toast.makeText(context, "LOGIN SUCCESS", Toast.LENGTH_SHORT).show()
                        navController.navigate("adminHome") //callback
                    },
                    onFailure = { errorMsg ->
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