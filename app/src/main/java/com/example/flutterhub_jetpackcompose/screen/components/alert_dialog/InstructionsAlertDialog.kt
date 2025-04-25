package com.example.flutterhub_jetpackcompose.screen.components.alert_dialog

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.screen.components.WatchInstructions
import com.example.flutterhub_jetpackcompose.screen.components.screen.openTheLink

@Composable
fun InstructionsAlertDialog(navController: NavController, context: Context, onDismiss: () -> Unit) {
    val openWatchDialog = remember { mutableStateOf(false) }

    AlertDialog(
        icon = {
            Icon(Icons.Default.Info, contentDescription = "Example Icon")
        },
        title = {
            Text(text = "Instructions:")
        },
        text = {
            Text(
                text = "1. Follow and complete the coding task or assessment as instructed. \n" +
                        "2. Once you have finished, click the link icon \uD83D\uDD17 to generate or copy the link to your work. \n" +
                        "3. Copy the link.\n" +
                        "4. Paste the copied link into the input text box at the bottom part of the assessment form/page."
            )
        },
//        dismissButton = {
//            TextButton(
//                onClick = {
//                    openWatchDialog.value = true
//                }
//            ) {
//                Text("Watch Tutorial")
//            }
//        },

        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openTheLink(
                        navController,
                        "https://play.kotlinlang.org/"
                    )
                }
            ) {
                Text("Confirm")
            }

            if (openWatchDialog.value) {
                WatchInstructions(
                    navController = navController,
                    context = context,
                    onDismiss = { openWatchDialog.value = false }
                )
            }
        },
    )
}