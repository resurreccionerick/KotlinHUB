package com.example.flutterhub_jetpackcompose.screen.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flutterhub_jetpackcompose.data.models.QuizScoreModel
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.screen.assessment.admin.AddAssessmentScreen
import com.example.flutterhub_jetpackcompose.screen.assessment.admin.EditAssessmentScreen
import com.example.flutterhub_jetpackcompose.screen.assessment.admin.TrackAssessmentScreen
import com.example.flutterhub_jetpackcompose.screen.assessment.user.AssessmentHomeScreen
import com.example.flutterhub_jetpackcompose.screen.components.screen.AboutUsScreen
import com.example.flutterhub_jetpackcompose.screen.components.screen.AssessmentDetailsScreen
import com.example.flutterhub_jetpackcompose.screen.components.screen.LeaderboardScreen
import com.example.flutterhub_jetpackcompose.screen.components.screen.LessonDetailsScreen
import com.example.flutterhub_jetpackcompose.screen.components.screen.OverallLeaderboardScreen
import com.example.flutterhub_jetpackcompose.screen.components.ProfileComponent
import com.example.flutterhub_jetpackcompose.screen.components.WebView
import com.example.flutterhub_jetpackcompose.screen.home.AdminHomeScreen
import com.example.flutterhub_jetpackcompose.screen.home.SettingsScreen
import com.example.flutterhub_jetpackcompose.screen.home.UserHomeScreen
import com.example.flutterhub_jetpackcompose.screen.lessons.admin.AddLessonScreen
import com.example.flutterhub_jetpackcompose.screen.lessons.admin.EditLessonScreen
import com.example.flutterhub_jetpackcompose.screen.lessons.user.BasicHomeScreen
import com.example.flutterhub_jetpackcompose.screen.lessons.user.IntermediateHomeScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.ForgotPassScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.LoginScreen
import com.example.flutterhub_jetpackcompose.screen.login_register.SignupScreen
import com.example.flutterhub_jetpackcompose.screen.quiz.admin.AdminAddQuizScreen
import com.example.flutterhub_jetpackcompose.screen.quiz.admin.AdminEditQuizScreen
import com.example.flutterhub_jetpackcompose.screen.quiz.admin.BasicQuizHomeScreen
import com.example.flutterhub_jetpackcompose.screen.quiz.admin.IntermediateQuizHomeScreen
import com.example.flutterhub_jetpackcompose.screen.quiz.admin.QuizDifficultyScreen
import com.example.flutterhub_jetpackcompose.screen.user.UserQuizScreen
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun NavGraph(navController: NavHostController, viewModel: AppViewModel, context: Context) {


    val user: UserModel? = Hawk.get("user_details")

    val startDestination = if (user != null && user.id.isNotEmpty()) {
        if (Hawk.get<String?>("role").equals(
                "admin"
            )
        ) {
            "adminHome"
        } else {
            "userHome"
        }

    } else {
        "login"
    }

    NavHost(
        navController, startDestination = startDestination
    ) {

        // ---------------------------------------------------- LOGIN/REGISTER ---------------------------------------------------- //
        composable("login") {
            LoginScreen(navController, viewModel, context)
        }

        composable("signup") {
            SignupScreen(navController, viewModel, context)
        }

        composable("forgotPassword") {
            ForgotPassScreen(navController, viewModel, context)
        }


        // ---------------------------------------------------- HOME ---------------------------------------------------- //

        composable("adminHome") {
            AdminHomeScreen(navController, viewModel, context)
        }

        composable("userHome") {
            UserHomeScreen(navController, viewModel, context)
        }

        composable("userSettings") {
            SettingsScreen(navController, viewModel, context)
        }

        composable("adminBasic") {
            BasicHomeScreen(navController, viewModel, context)
        }


        composable("adminIntermediate") {
            IntermediateHomeScreen(navController, viewModel, context)
        }

        composable("profile") {
            ProfileComponent(navController, viewModel, context)
        }

        composable("aboutUs") {
            AboutUsScreen(navController, viewModel, context)
        }


        // ---------------------------------------------------- QUIZ ---------------------------------------------------- //
        composable("difficultyQuiz") {
            QuizDifficultyScreen(navController, viewModel, context)
        }


        composable("basicQuiz") {
            BasicQuizHomeScreen(navController, viewModel, context)
        }

        composable("intermediateQuiz") {
            IntermediateQuizHomeScreen(navController, viewModel, context)
        }

        composable("adminAddQuiz") {
            AdminAddQuizScreen(navController, viewModel, context)
        }


        composable("takeQuiz") {
            val scoreModel = QuizScoreModel()

            UserQuizScreen(navController, viewModel, scoreModel, context)
        }

        composable(
            "adminEditQuiz/{quizId}",
            arguments = listOf(navArgument("quizId") { type = NavType.StringType })
        ) { backStackEntry ->
            val quizID = backStackEntry.arguments?.getString("quizId") ?: ""
            val quiz = viewModel.getQuizByID(quizID)
            AdminEditQuizScreen(navController, viewModel, quiz, context)
        }

        // ---------------------------------------------------- LESSON ---------------------------------------------------- //

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

        // ---------------------------------------------------- WEB VIEW ---------------------------------------------------- //
        composable("webView") {
            //TheWebView("https://pl.kotl.in/Eaa0qDeSD")
            WebView(navController, context)
        }


        // ---------------------------------------------------- SCORE ---------------------------------------------------- //
        composable("scoreQuiz") {
            LeaderboardScreen(navController, viewModel, context)
        }

        composable("overallLeaderboards") {
            OverallLeaderboardScreen(navController, viewModel, context)
        }


        // ---------------------------------------------------- ASSESSMENT ---------------------------------------------------- //
        composable("assessmentHome") {
            AssessmentHomeScreen(navController, viewModel, context)
        }

        composable("AddAssessment") {
            AddAssessmentScreen(navController, viewModel, context)
        }

        composable(
            "editAssessment/{assessmentId}",
            arguments = listOf(navArgument("assessmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("assessmentId") ?: ""
            // Fetch the lesson by ID (if needed)
            val assessment = viewModel.getAssessmentById(id)

            Log.d("ASSESSMENT EDIT ID:", assessment.id)
            EditAssessmentScreen(navController, viewModel, assessment, context)
        }

        composable(
            "assessmentView/{assessmentId}",
            arguments = listOf(navArgument("assessmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("assessmentId") ?: ""
            // Fetch the lesson by ID (if needed)
            val assessment = viewModel.getAssessmentById(id)

            AssessmentDetailsScreen(
                navController,
                user!!.id,
                user!!.name,
                viewModel,
                context,
                assessment,
            )
        }

        composable(
            "assessmentTrack/{assessmentId}",
            arguments = listOf(navArgument("assessmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("assessmentId") ?: ""
            val assessment = viewModel.getAssessmentById(id)
            TrackAssessmentScreen(navController, viewModel, context, assessment.id)
        }
    }
}




