package com.example.flutterhub_jetpackcompose.screen.admin

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.utils.ImageCard
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavController, viewModel: LessonViewModel, context: Context) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Home Screen") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
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
                            navController.navigate("adminBasic") },
                        modifier = Modifier.weight(1f)
                    )
                    ImageCard(
                        label = "Intermediate",
                        imageRes = R.drawable.muscle,
                        onClick = {
                            Hawk.put("difficulty", "intermediate")
                            viewModel.refreshDifficulty()
                            navController.navigate("adminIntermediate") },
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
                            navController.navigate("adminQuiz") },
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
    )
}
