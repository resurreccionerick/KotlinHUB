package com.example.flutterhub_jetpackcompose.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flutterhub_jetpackcompose.screen.admin.AddLessonScreen
import com.example.flutterhub_jetpackcompose.screen.admin.AdminHomeScreen
import com.example.flutterhub_jetpackcompose.screen.admin.BasicHomeScreen

import com.example.flutterhub_jetpackcompose.screen.admin.EditLessonScreen
import com.example.flutterhub_jetpackcompose.screen.admin.IntermediateHomeScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.ForgotPassScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.LoginScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.SignupScreen
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun NavGraph(navController: NavHostController, viewModel: LessonViewModel, context: Context) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, viewModel, context)
        }

        composable("signup") {
            SignupScreen(navController, viewModel, context)
        }

        composable("forgotPassword") {
            ForgotPassScreen(navController, viewModel, context)
        }

        composable("adminHome") {
            AdminHomeScreen(navController, viewModel, context)
        }

        composable("adminBasic") {
            BasicHomeScreen(navController, viewModel, context)
        }

        composable("adminIntermediate") {
            IntermediateHomeScreen(navController, viewModel, context)
        }

        composable("addLesson") {
            AddLessonScreen(navController, viewModel, context)
        }

        composable(
            "editLesson/{lessonId}",
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            // Fetch the lesson by ID (if needed)
            val lesson = viewModel.getLessonById(lessonId)
            EditLessonScreen(navController, viewModel, lesson, context)
        }


    }
}

