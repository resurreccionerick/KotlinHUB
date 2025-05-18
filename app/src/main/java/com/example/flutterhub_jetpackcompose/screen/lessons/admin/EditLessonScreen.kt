package com.example.flutterhub_jetpackcompose.screen.lessons.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.LessonModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLessonScreen(
    navController: NavController,
    viewModel: AppViewModel,
    lessonModel: LessonModel,
    context: Context
) {
    var title by remember { mutableStateOf(lessonModel.name) }
    var order by remember { mutableStateOf(lessonModel.order.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Lesson") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Lesson title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = order,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                            order = newValue
                        }
                    },
                    label = { Text("Order") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = {
                    if (title.isNotEmpty()) {
                        val lesson = lessonModel.copy(name = title, order = order.toInt())
                        viewModel.updateLesson(
                            lesson,
                            onSuccess = {
                                Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT)
                                    .show()

                                navController.popBackStack()
                            }, onFailure = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            })


                    } else {
                        Toast.makeText(context, "Please enter all fields", Toast.LENGTH_LONG).show()
                    }

                }, modifier = Modifier.align(Alignment.End)) { Text("Update") }
            }
        }
    )
}


