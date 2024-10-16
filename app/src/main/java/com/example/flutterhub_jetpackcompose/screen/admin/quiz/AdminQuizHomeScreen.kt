package com.example.flutterhub_jetpackcompose.screen.admin.quiz

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EditOff
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flutterhub_jetpackcompose.utils.LessonCard
import com.example.flutterhub_jetpackcompose.utils.QuizCard
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminQuizHomeScreen(
    navController: NavHostController,
    viewModel: LessonViewModel,
    context: Context
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Quizzes") },
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
            FloatingActionButton(onClick = {
                navController.navigate("adminAddQuiz")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Quiz")
            }

//            FloatingActionButton(onClick = {
//                navController.navigate("takeQuiz")
//            }) {
//                Icon(Icons.Default.EditOff, contentDescription = "Add Quiz")
//            }
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
