package com.example.flutterhub_jetpackcompose.screen.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.screen.components.ImageCard
import com.example.flutterhub_jetpackcompose.screen.components.ImageRowCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(navController: NavController, viewModel: AppViewModel, context: Context) {

    LaunchedEffect(Unit) { viewModel.refreshProfileDetails() }
    val user: UserModel? = Hawk.get("user_details")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Current Score: " + ((user?.basic_Score?.toIntOrNull()
                            ?: 0) + (user?.intermediate_Score?.toIntOrNull() ?: 0))
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
//                    Text(
//                        "What would you like to learn today?",
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
//                    )

                    Row(
                        modifier = Modifier.weight(2f)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ImageRowCard(
                                label = "Basic", imageRes = R.drawable.easy_colored, onClick = {
                                    Hawk.put("difficulty", "basic")
                                    viewModel.refreshLessonDifficulty()
                                    navController.navigate("adminBasic")
                                }, modifier = Modifier.weight(1f)
                            )

                            ImageRowCard(
                                label = "Intermediate",
                                imageRes = R.drawable.muscle_colored,
                                onClick = {
                                    Hawk.put("difficulty", "intermediate")
                                    viewModel.refreshLessonDifficulty()
                                    navController.navigate("adminIntermediate")
                                }, modifier = Modifier.weight(1f)
                            )

                            ImageRowCard(
                                label = "Quiz", imageRes = R.drawable.quiz, onClick = {
                                    viewModel.loadQuizzes()
                                    navController.navigate("difficultyQuiz")
                                }, modifier = Modifier.weight(1f)
                            )
                        }

                        ImageCard(
                            label = "Code Runner",
                            imageRes = R.drawable.developer,
                            imgHeight = 450,
                            onClick = {
                                viewModel.loadAssessment()
                                navController.navigate("assessmentHome")
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Text("Other Topics: ", fontWeight = FontWeight.Bold)

                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.5f) // Constraint the height
                            .verticalScroll(rememberScrollState())
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ImageRowCard(
                                label = "Latest Kotlin News",
                                imageRes = R.drawable.newspaper,
                                onClick = {
                                    Hawk.put("title", "")
                                    Hawk.put("link", "https://blog.jetbrains.com/kotlin/")
                                    navController.navigate("webView")
                                }
                            )

                            ImageRowCard(
                                label = "Kotlin Forum",
                                imageRes = R.drawable.chat,
                                onClick = {
                                    Hawk.put("title", "")
                                    Hawk.put("link", "https://discuss.kotlinlang.org/")
                                    navController.navigate("webView")
                                }
                            )

                            ImageRowCard(
                                label = "Install Android Studio",
                                imageRes = R.drawable.android,
                                onClick = {
                                    Hawk.put("title", "")
                                    Hawk.put("link", "https://developer.android.com/studio/install")
                                    navController.navigate("webView")
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}
