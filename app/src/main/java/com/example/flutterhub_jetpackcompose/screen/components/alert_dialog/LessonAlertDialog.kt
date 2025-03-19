package com.example.flutterhub_jetpackcompose.screen.components.alert_dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel

@Composable
fun LessonAlertDialog(context: Context, onDismiss: () -> Unit, viewModel: AppViewModel) {
    val openDialog = remember { mutableStateOf(false) }
    var lessonTitle by remember { mutableStateOf("") }

    AlertDialog(
        title = {
            Text(text = "Add Lesson:")
        },

        text = { // 📝 Add TextField here
            OutlinedTextField(
                value = lessonTitle,
                onValueChange = { lessonTitle = it },
                label = { Text("Enter title") }
            )
        },


        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                    onDismiss()
                }
            ) {
                Text("Cancel", style = TextStyle(Color.Red))
            }
        },

        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.addNewLesson(lessonTitle, onSuccess = {

                        Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT)
                            .show()

                        openDialog.value = false
                        onDismiss()
                    }, onFailure = { errorMsg ->
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    })
                }
            ) {
                Text("Confirm")
            }
        },
    )
}