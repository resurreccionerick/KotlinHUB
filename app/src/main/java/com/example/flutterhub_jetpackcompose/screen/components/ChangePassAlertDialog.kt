package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun ChangePassAlertDialog(viewModel: AppViewModel, context: Context, onDismiss: () -> Unit) {
    val userModel = Hawk.get<UserModel>("user_details")

    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Example Icon")
        },
        title = {
            Text(text = "Instructions:")
        },
        text = {
            Text(
                text = "1. Email will be sent: Firebase sends a password reset link to your user's email address (Check Spam).\n" +
                        "2. Open Email: The user checks their inbox for the password reset email.\n" +
                        "3.Click the Link: The user clicks on the link in the email to reset their password.\n" +
                        "4. Enter New Password: The user is redirected to a page or screen where they can enter a new password.\n" +
                        "5. Password Updated: After submitting the new password, Firebase updates the password, and the user can log in with the new password."
            )
        },

        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.forgotPass(userModel.email, onSuccess = {
                        Toast.makeText(
                            context,
                            "Reset link will be sent to your Email",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }, onFailure = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    })
                }
            ) {
                Text("Confirm")
            }
        },
    )
}