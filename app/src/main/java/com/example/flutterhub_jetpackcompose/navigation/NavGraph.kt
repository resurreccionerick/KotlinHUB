package com.example.flutterhub_jetpackcompose.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flutterhub_jetpackcompose.screen.admin.AddLessonScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.ForgotPassScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.LoginScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.SignupScreen
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

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
            AddLessonScreen(navController, viewModel, context)
        }

        composable("addLesson") {
            AddLessonScreen(navController, viewModel, context)
        }


    }
}

