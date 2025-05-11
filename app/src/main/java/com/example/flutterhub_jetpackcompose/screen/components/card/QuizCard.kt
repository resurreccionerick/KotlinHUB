package com.example.flutterhub_jetpackcompose.screen.components.card

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.flutterhub_jetpackcompose.data.models.QuizModel
import com.example.flutterhub_jetpackcompose.ui.theme.DeleteRedDark
import com.example.flutterhub_jetpackcompose.ui.theme.DeleteRedLight
import com.example.flutterhub_jetpackcompose.ui.theme.EditGreenDark
import com.example.flutterhub_jetpackcompose.ui.theme.EditGreenLight
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun QuizCard(
    navController: NavController,
    quiz: QuizModel,
    viewModel: AppViewModel,
    context: Context
) {
    val isDarkMode = isSystemInDarkTheme()

    // Conditionally apply colors
    val editColor = if (isDarkMode) EditGreenDark else EditGreenLight
    val deleteColor = if (isDarkMode) DeleteRedDark else DeleteRedLight

    var openDeleteDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(15.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Card padding
        elevation = CardDefaults.cardElevation(10.dp) // Card elevation (shadow effect)
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
                    text = quiz.question,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

            if (Hawk.get<Boolean?>("role").equals("admin")) {
                // Edit Button
                Button(
                    onClick = {
                        navController.navigate("adminEditQuiz/${quiz.id}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = editColor),
                    modifier = Modifier.padding(end = 8.dp) // Spacing between buttons
                ) {
                    Text("Edit", color = Color.White)
                }

                // Delete Button
                Button(
                    onClick = { openDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }

                if (openDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { openDeleteDialog = false },
                        title = { Text("Confirm Deletion") },
                        text = { Text("Are you sure you want to delete this quiz?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deleteQuiz(quiz.id,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Successfully deleted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onFailure = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }
                                )
                                openDeleteDialog = false  // Close dialog after confirmation
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { openDeleteDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }

            }
        }
    }
}

