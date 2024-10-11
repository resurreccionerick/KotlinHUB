package com.example.flutterhub_jetpackcompose.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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

