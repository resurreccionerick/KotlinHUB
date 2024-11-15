package com.example.flutterhub_jetpackcompose.screen.quiz.admin

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flutterhub_jetpackcompose.screen.components.QuizCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntermediateQuizHomeScreen(
    navController: NavHostController, viewModel: AppViewModel, context: Context
) {

    LaunchedEffect(Unit) {
        viewModel.refreshQuizDifficulty()
        viewModel.loadQuizzes()
    }

    if (viewModel.quizzes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent overlay
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Intermediate Quizzes") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        },


        floatingActionButton = {
            Column() {
                if (!Hawk.get<Boolean?>("role").equals("admin")) {
//                FloatingActionButton(onClick = {
//                    navController.navigate("takeQuiz/${viewModel.quizzes}")
//                }) {
//                    Icon(Icons.Default.Quiz, contentDescription = "Add Quiz")
//                }
                } else {
                    FloatingActionButton(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onClick = {
                            Hawk.put("difficulty", "intermediate")
                            viewModel.refreshLeaderboardsDifficulty()
                            navController.navigate("scoreQuiz")
                        }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Leaderboard",
                            tint = Color.Blue
                        )
                    }

                    FloatingActionButton(onClick = {
                        navController.navigate("adminAddQuiz")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Quiz")
                    }
                }
            }
        }


    ) { paddingValues ->
        if (Hawk.get<String?>("role").equals("admin")) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LazyColumn {
                    items(viewModel.quizzes) { quiz ->
                        QuizCard(navController, quiz, viewModel, context)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (Hawk.get<Boolean?>("role").equals("admin")) {
                    LazyColumn {
                        items(viewModel.quizzes) { quiz ->
                            QuizCard(navController, quiz, viewModel, context)
                        }
                    }
                } else {
                    Card(
                        onClick = {
                            navController.navigate("takeQuiz")
                        },
                        shape = RoundedCornerShape(15.dp), // Rounded corners for the card
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
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
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically, // Center vertically
                                horizontalArrangement = Arrangement.SpaceBetween // Spread text and buttons
                            ) {
                                Text(
                                    text = "Take the Quiz",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

