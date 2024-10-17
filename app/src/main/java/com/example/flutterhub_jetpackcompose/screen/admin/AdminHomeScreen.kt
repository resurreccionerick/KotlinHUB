package com.example.flutterhub_jetpackcompose.screen.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.utils.ImageCard
import com.example.flutterhub_jetpackcompose.utils.ImageRowCard
import com.example.flutterhub_jetpackcompose.viewmodel_repository.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavController, viewModel: AppViewModel, context: Context) {

    var expanded by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Admin Home Screen") }, actions = {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = "More",
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text("Logout") }, onClick = {
                        viewModel.userLogout(onSuccess = {
                            Hawk.deleteAll()
                            navController.navigate("login") {
                                popUpTo(0) // Clears the entire back stack
                                launchSingleTop =
                                    true // Avoids multiple instances of the same destination
                            }
                        }, onFailure = { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        })
                        expanded = false
                    })
                }
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ), modifier = Modifier.fillMaxWidth()
        )
    }, content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
//                .paint(
//                    // Replace with your image id
//                    painterResource(id = R.drawable.bg), contentScale = ContentScale.FillBounds
//                )

        ) {
            Column(
                modifier = Modifier
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
                        label = "Basic", imageRes = R.drawable.easy_colored, onClick = {
                            Hawk.put("difficulty", "basic")
                            viewModel.refreshLessonDifficulty()
                            navController.navigate("adminBasic")
                        }, modifier = Modifier.weight(1f)
                    )
                    ImageCard(
                        label = "Intermediate",
                        imageRes = R.drawable.muscle_colored,
                        onClick = {
                            Hawk.put("difficulty", "intermediate")
                            viewModel.refreshLessonDifficulty()
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
                        label = "Quiz", imageRes = R.drawable.quiz, onClick = {
                            viewModel.loadQuizzes()
                            navController.navigate("difficultyQuiz")
                        }, modifier = Modifier.weight(1f)
                    )
                    ImageCard(
                        label = "Code Runner",
                        imageRes = R.drawable.developer,
                        onClick = { navController.navigate("adminAssessment") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Text("Other Topics: ", color = Color.Black, fontWeight = FontWeight.Bold)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        ImageRowCard(
                            label = "Install Android Studio",
                            imageRes = R.drawable.developer,
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "  clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        )


                        ImageRowCard(
                            label = "Install Android Studio",
                            imageRes = R.drawable.developer,
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "  clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        )

                        ImageRowCard(
                            label = "Install Android Studio",
                            imageRes = R.drawable.developer,
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "  clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        )
                    }
                }
            }
        }

    })
}
