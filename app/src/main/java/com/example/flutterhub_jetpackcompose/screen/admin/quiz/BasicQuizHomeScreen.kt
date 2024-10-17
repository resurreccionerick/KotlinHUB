package com.example.flutterhub_jetpackcompose.screen.admin.quiz

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flutterhub_jetpackcompose.screen.components.QuizCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicQuizHomeScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
    context: Context
) {

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
            TopAppBar(
                title = { Text(text = "Basic Quizzes") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },


        floatingActionButton = {
            if (!Hawk.get<Boolean?>("role").equals("admin")
            ) {
                FloatingActionButton(onClick = {
                    navController.navigate("takeQuiz")
                }) {
                    Icon(Icons.Default.Quiz, contentDescription = "Add Quiz")
                }
            } else {
                FloatingActionButton(onClick = {
                    navController.navigate("adminAddQuiz")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Quiz")
                }
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
            LazyColumn {
                items(viewModel.quizzes) { quiz ->
                    QuizCard(navController, quiz, viewModel, context)
                }
            }
        }
    }
}
