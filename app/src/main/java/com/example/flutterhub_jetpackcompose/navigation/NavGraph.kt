package com.example.flutterhub_jetpackcompose.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flutterhub_jetpackcompose.models.Content
import com.example.flutterhub_jetpackcompose.screen.admin.AddLessonScreen
import com.example.flutterhub_jetpackcompose.screen.admin.AdminHomeScreen
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: LessonViewModel, context: Context) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            AdminHomeScreen(navController, viewModel)
        }

        composable("addLesson") {
            AddLessonScreen(navController, viewModel, context)
        }
    }
}

