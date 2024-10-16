package com.example.flutterhub_jetpackcompose.screen.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.utils.ImageCard
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavController, viewModel: LessonViewModel, context: Context) {

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Home Screen") },
                actions = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert, contentDescription = "More",
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text("Logout") }, onClick = {
                                viewModel.userLogout(onSuccess = {
                                    Hawk.deleteAll()
                                    navController.navigate("login"){
                                        popUpTo(0) // Clears the entire back stack
                                        launchSingleTop = true // Avoids multiple instances of the same destination
                                    }
                                }, onFailure = { msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                })
                                expanded = false
                            })
                        }
                    }


                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        // Replace with your image id
                        painterResource(id = R.drawable.bg),
                        contentScale = ContentScale.FillBounds
                    )

            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // First Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ImageCard(
                            label = "Basic",
                            imageRes = R.drawable.basic,
                            onClick = {
                                Hawk.put("difficulty", "basic")
                                viewModel.refreshDifficulty()
                                navController.navigate("adminBasic")
                            },
                            modifier = Modifier.weight(1f)
                        )
                        ImageCard(
                            label = "Intermediate",
                            imageRes = R.drawable.muscle,
                            onClick = {
                                Hawk.put("difficulty", "intermediate")
                                viewModel.refreshDifficulty()
                                navController.navigate("adminIntermediate")
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Second Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ImageCard(
                            label = "Quiz",
                            imageRes = R.drawable.quiz,
                            onClick = {
                                viewModel.loadQuizzes()
                                navController.navigate("adminQuiz")
                            },
                            modifier = Modifier.weight(1f)
                        )
                        ImageCard(
                            label = "Code Runner",
                            imageRes = R.drawable.developer,
                            onClick = { navController.navigate("adminAssessment") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

        }
    )
}
