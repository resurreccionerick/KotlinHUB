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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.LessonSubtopic
import com.example.flutterhub_jetpackcompose.ui.theme.DeleteRedDark
import com.example.flutterhub_jetpackcompose.ui.theme.DeleteRedLight
import com.example.flutterhub_jetpackcompose.ui.theme.EditGreenDark
import com.example.flutterhub_jetpackcompose.ui.theme.EditGreenLight
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun SubLessonCard(
    navController: NavController,
    lessonID: String,
    subTopic: LessonSubtopic?,
    viewModel: AppViewModel,
    context: Context
) {
    val isDarkMode = isSystemInDarkTheme()

    // Conditionally apply colors
    val editColor = if (isDarkMode) EditGreenDark else EditGreenLight
    val deleteColor = if (isDarkMode) DeleteRedDark else DeleteRedLight


    Card(
        onClick = {
            navController.navigate("lessonView/${subTopic?.id}")
        },
        shape = RoundedCornerShape(4.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp), // Card padding
        elevation = CardDefaults.cardElevation(2.dp) // Card elevation (shadow effect)
    ) {
        // Box to align content inside the card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the card
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, // Center vertically
                horizontalArrangement = Arrangement.SpaceBetween // Spread text and buttons
            ) {
                Text(
                    text = "Title: ${subTopic?.name}",
                    style = MaterialTheme.typography.bodyLarge, color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

            if (Hawk.get<Boolean?>("role").equals("admin")) {
                // Edit Button
                Button(
                    onClick = {
                        val subTopicId = subTopic?.id ?: ""
                        val lessonId = lessonID ?: ""

                        navController.navigate("editSubLesson/$subTopicId/$lessonId")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = editColor),
                    modifier = Modifier.padding(end = 8.dp) // Spacing between buttons
                ) {
                    Text("Edit", color = Color.White)
                }

                // Delete Button
                Button(
                    onClick = {
                        viewModel.deleteSubLesson(lessonID, subTopic!!.id,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Successfully deleted",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.navigate("adminHome")
                            },
                            onFailure = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = deleteColor)
                ) {
                    Text("Delete", color = Color.White)
                }
            }
        }
    }
}

