package com.example.flutterhub_jetpackcompose.screen.components.card

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.AssessmentModel
import com.example.flutterhub_jetpackcompose.ui.theme.DeleteRedDark
import com.example.flutterhub_jetpackcompose.ui.theme.DeleteRedLight
import com.example.flutterhub_jetpackcompose.ui.theme.EditGreenDark
import com.example.flutterhub_jetpackcompose.ui.theme.EditGreenLight
import com.example.flutterhub_jetpackcompose.ui.theme.TrackBlueDark
import com.example.flutterhub_jetpackcompose.ui.theme.TrackBlueLight
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun AssessmentCard(
    userId: String,
    navController: NavController,
    assessmentModel: AssessmentModel,
    viewModel: AppViewModel,
    context: Context
) {
    val isDarkMode = isSystemInDarkTheme()

    // Conditionally apply colors
    val editColor = if (isDarkMode) EditGreenDark else EditGreenLight
    val deleteColor = if (isDarkMode) DeleteRedDark else DeleteRedLight
    val trackColor = if (isDarkMode) TrackBlueDark else TrackBlueLight

    // Get the specific link for the current user from the assessment
    val userLink = assessmentModel.links?.get(userId)

    // Check if the userLink exists and if it's checked
    val isChecked = userLink?.checked ?: null // Default to false if no link exists for the user
    val theComment = userLink?.comment ?: null

    Card(
        onClick = {
            navController.navigate("assessmentView/${assessmentModel.id}")
        },
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Card padding
        elevation = CardDefaults.cardElevation(4.dp) // Card elevation (shadow effect)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title at the top
                Text(
                    text = "Title: ${assessmentModel.title}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )

                // IconButton and Text in a Row
                if (!Hawk.get<Boolean?>("role").equals("admin")) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            IconButton(onClick = {
                                // Define action here if needed
                            }) {
//                                val imageVector = when (isChecked) {
//                                    "true" -> {
//                                        Icons.Default.Check
//                                    }
//
//                                    "false" -> {
//                                        Icons.Default.NotInterested
//                                    }
//
//                                    else -> {
//                                        Icons.Default.Pending
//                                    }
//                                }
                                val imageVector = if (isChecked != null) {
                                    Icons.Default.Check
                                } else {
                                    Icons.Default.Pending

                                }

                                Icon(
                                    imageVector = imageVector,
                                    contentDescription = null,
                                    tint = Color(0xFF039be5)
                                )

                            }

//                            Text(
//                                text = when (isChecked.toString()) {
//                                    "true" -> "Status: Done!"
//                                    "false" -> "Status: Not Approved!"
//                                    else -> "Status: Pending"
//                                },
//                                style = MaterialTheme.typography.bodySmall
//                            )
                            Text(
                                text = if (isChecked != null) "Status: Done!" else "Status: Not done",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        if (isChecked == "false" && !theComment.isNullOrEmpty()) {
                            IconButton(onClick = {
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Comment,
                                    contentDescription = null,
                                    tint = Color.Red
                                )

                            }
                            Text(
                                maxLines = 2,
                                text = "Comment: $theComment",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                if (Hawk.get<Boolean?>("role").equals("admin")) {
                    // Buttons at the bottom
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (Hawk.get<Boolean?>("role").equals("admin")) {
                            // Track button
//                            Button(
//                                onClick = {
//                                    navController.navigate("assessmentTrack/${assessmentModel.id}")
//                                },
//                                colors = ButtonDefaults.buttonColors(containerColor = trackColor),
//                                modifier = Modifier.padding(8.dp)
//                            ) {
//                                Text("Track", color = Color.White)
//                            }

                            // Edit button
                            Button(
                                onClick = {
                                    navController.navigate("editAssessment/${assessmentModel.id}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = editColor),
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Edit", color = Color.White)
                            }

                            // Delete button
                            Button(
                                onClick = {
                                    viewModel.deleteAssessment(
                                        userId,
                                        assessmentModel.id,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "Successfully deleted",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onFailure = { error ->
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = deleteColor),
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Delete", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}