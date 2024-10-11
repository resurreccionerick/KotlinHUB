package com.example.flutterhub_jetpackcompose.screen.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLessonScreen(
    navController: NavController,
    viewModel: LessonViewModel,
    lessonModel: LessonModel,
    context: Context
) {
    var title by remember { mutableStateOf(lessonModel.name) }
    var description by remember { mutableStateOf(lessonModel.description) }


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
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Content") },
                    minLines = 10,
                    maxLines = 15,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        val lesson = lessonModel.copy(name = title, description = description)
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


