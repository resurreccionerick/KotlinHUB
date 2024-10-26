package com.example.flutterhub_jetpackcompose.screen.quiz.admin

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.screen.components.ImageCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDifficultyScreen(
    navController: NavController,
    viewModel: AppViewModel,
    context: Context
) {

    Scaffold(
        containerColor = colorResource(id = R.color.metal_black),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.metal_black2),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                title = { Text("Choose Difficulty") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
                {
                    ImageCard(
                        label = "Basic",
                        imageRes = R.drawable.basic, 300,
                        onClick = {
                            Hawk.put("difficulty", "basic")
                            if (viewModel.quizzes.isEmpty()) { // Call refresh only if needed
                                viewModel.refreshQuizDifficulty()
                            }
                            navController.navigate("basicQuiz")
                        }

                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ImageCard(
                        label = "Intermediate",
                        imageRes = R.drawable.muscle, 300,
                        onClick = {
                            Hawk.put("difficulty", "intermediate")
                            viewModel.refreshQuizDifficulty()
                            navController.navigate("intermediateQuiz")
                        }
                    )
                }
            }


        }
    )


}