package com.example.flutterhub_jetpackcompose.screen.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

@Composable
fun AdminHomeScreen(navController: NavController, viewModel: LessonViewModel, context: Context) {

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
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lesson: ")

            LazyColumn {
                items(viewModel.lessons) { lesson ->
                    LessonCard(navController, lesson, viewModel, context)
                }
            }
        }
    }
}

@Composable
fun LessonCard(
    navController: NavController,
    lesson: LessonModel,
    viewModel: LessonViewModel,
    context: Context
) {
    Card(
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Card padding
        elevation = CardDefaults.cardElevation(4.dp) // Card elevation (shadow effect)
    ) {
        // Box to align content inside the card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Padding inside the card
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, // Center vertically
                horizontalArrangement = Arrangement.SpaceBetween // Spread text and buttons
            ) {
                Text(
                    text = "Title: ${lesson.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            // Edit Button
            Button(
                onClick = {
                    navController.navigate("editLesson/${lesson.id}")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.padding(end = 8.dp) // Spacing between buttons
            ) {
                Text("Edit")
            }

            // Delete Button
            Button(
                onClick = {
                    viewModel.deleteLesson(lesson.id,
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Successfully deleted",
                                Toast.LENGTH_SHORT
                            ).show()

                        },
                        onFailure = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        })
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete")
            }
        }
    }
}

