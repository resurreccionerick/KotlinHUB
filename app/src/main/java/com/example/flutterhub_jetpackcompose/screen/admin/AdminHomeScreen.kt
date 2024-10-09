package com.example.flutterhub_jetpackcompose.screen.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

@Composable
fun AdminHomeScreen(navController: NavController, viewModel: LessonViewModel) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addLesson")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add lesson")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lesson: ")

            LazyColumn {
                items(viewModel.lessons) { lesson ->
                    LessonCard(lesson, viewModel)
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: LessonModel, viewModel: LessonViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Title: " + lesson.name)
        Text("Description:  " + lesson.description)

        Row {
            Button(onClick = { //todo navigate to edit lesson
            }) { Text("Edit") }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { viewModel.deleteLesson(lesson.id) }) {
                Text("Delete")
            }
        }
    }
}


