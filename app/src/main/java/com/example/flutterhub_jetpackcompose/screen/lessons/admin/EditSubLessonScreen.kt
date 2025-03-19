package com.example.flutterhub_jetpackcompose.screen.lessons.admin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.LessonModel
import com.example.flutterhub_jetpackcompose.data.models.LessonSubtopic
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSubLessonScreen(
    navController: NavController,
    viewModel: AppViewModel,
    lessonId: String,
    subLessonModel: LessonSubtopic,
    context: Context
) {

    var title by rememberSaveable { mutableStateOf(subLessonModel.name) }
    var description by rememberSaveable { mutableStateOf(subLessonModel.description) }
    var link by rememberSaveable { mutableStateOf(subLessonModel.link) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit $title") },
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

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = link,
                    maxLines = 1,
                    onValueChange = { link = it },
                    label = { Text("YouTube video link") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = {
                    if (title.isNotEmpty()) {
                        val subTopic = subLessonModel.copy(
                            name = title,
                            description = description,
                            link = link
                        )

                        viewModel.updateSubLesson(
                            lessonId,
                            subTopic,
                            onSuccess = {
                                Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT)
                                    .show()

                                navController.navigate("adminHome")
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


