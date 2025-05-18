package com.example.flutterhub_jetpackcompose.screen.components.alert_dialog

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel

@Composable
fun LessonAlertDialog(context: Context, onDismiss: () -> Unit, viewModel: AppViewModel) {
    var lessonTitle by remember { mutableStateOf("") }
    var lessonOrder by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add Lesson")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = lessonTitle,
                    onValueChange = { lessonTitle = it },
                    label = { Text("Enter title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lessonOrder,
                    onValueChange = { lessonOrder = it },
                    label = { Text("Enter order number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    lessonTitle = ""
                    lessonOrder = ""
                    onDismiss()
                }
            ) {
                Text("Cancel", color = Color.Red)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val order = try {
                        lessonOrder.toInt()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(
                            context,
                            "Please enter a valid number for order",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TextButton
                    }

                    viewModel.addNewLesson(
                        name = lessonTitle,
                        order = order,
                        onSuccess = {
                            Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show()
                            lessonTitle = ""
                            lessonOrder = ""
                            onDismiss()
                        },
                        onFailure = { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            ) {
                Text("Confirm")
            }
        }
    )
}