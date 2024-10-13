package com.example.flutterhub_jetpackcompose.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flutterhub_jetpackcompose.models.UserModel
import com.example.flutterhub_jetpackcompose.screen.admin.lesson.AddLessonScreen
import com.example.flutterhub_jetpackcompose.screen.admin.assessment.AdminAssessmentScreen
import com.example.flutterhub_jetpackcompose.screen.admin.AdminHomeScreen
import com.example.flutterhub_jetpackcompose.screen.admin.quiz.AdminQuizHomeScreen
import com.example.flutterhub_jetpackcompose.screen.admin.lesson.BasicHomeScreen

import com.example.flutterhub_jetpackcompose.screen.admin.lesson.EditLessonScreen
import com.example.flutterhub_jetpackcompose.screen.admin.lesson.IntermediateHomeScreen
import com.example.flutterhub_jetpackcompose.screen.admin.quiz.AdminAddQuizScreen
import com.example.flutterhub_jetpackcompose.screen.admin.quiz.AdminEditQuizScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.ForgotPassScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.LoginScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.SignupScreen
import com.example.flutterhub_jetpackcompose.utils.LessonDetailsScreen
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun NavGraph(navController: NavHostController, viewModel: LessonViewModel, context: Context) {


    val user: UserModel? = Hawk.get("user_details")

    val startDestination = if (user != null && user.id.isNotEmpty()) {
        "adminHome"
    } else {
        "login"
    }

    NavHost(
        navController,
        startDestination = startDestination
    ) {
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

        composable("adminQuiz") {
            AdminQuizHomeScreen(navController, viewModel, context)
        }

        composable("adminAddQuiz") {
            AdminAddQuizScreen(navController, viewModel, context)
        }

        composable(
            "adminEditQuiz/{quizId}",
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) { backStackEntry ->
            val quizID = backStackEntry.arguments?.getString("quizId") ?: ""
            val quiz = viewModel.getQuizByID(quizID)
            AdminEditQuizScreen(navController, viewModel, quiz, context)
        }

        composable("adminAssessment") {
            AdminAssessmentScreen(navController, viewModel, context)
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

        composable(
            "lessonView/{lessonId}",
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            // Fetch the lesson by ID (if needed)
            val lesson = viewModel.getLessonById(lessonId)
            LessonDetailsScreen(navController, context, lesson)
        }


    }
}


