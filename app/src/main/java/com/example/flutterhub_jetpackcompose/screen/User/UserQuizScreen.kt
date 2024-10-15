package com.example.flutterhub_jetpackcompose.screen.User

import android.content.Context
import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text

import com.example.flutterhub_jetpackcompose.models.QuizModel
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel


@Composable
fun UserQuizScreen(

    navController: NavController, viewModel: LessonViewModel, context: Context
) {
    var quizzes by remember { mutableStateOf<List<QuizModel>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var answered by remember { mutableStateOf(false) } // Track if answer is submitted

    // Load quizzes when the screen opens
    LaunchedEffect(Unit) {
        quizzes = viewModel.quizzes
    }

    if (quizzes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        return
    }

    if (showResult) { //if done
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Quiz Completed!", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Your Score:$score / ${quizzes.size}", fontSize = 25.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back to Home")
            }

        }
        return
    }


    //get current quiz question
    val currentQuiz = quizzes[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //question number
        Text("$currentIndex+1 / ${quizzes.size}")

        Spacer(modifier = Modifier.height(16.dp))

        //question
        Text(text = currentQuiz.question, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        //display answers
        currentQuiz.choices.forEach { choices ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnswer == choices,
                    onClick = { selectedAnswer = choices }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = choices, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!answered) {
                    if (selectedAnswer.isNotEmpty()) {
                        val isCorrect = selectedAnswer.equals(currentQuiz.selectedAns)

                        if (isCorrect) {
                            score++
                        }

                        Toast.makeText(
                            context,
                            if (isCorrect) "Correct!" else "Wrong!",
                            Toast.LENGTH_SHORT
                        ).show()

                        answered = true //mark as answered
                    } else {
                        Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // Move to the next question or show result
                    if (currentIndex < quizzes.size + 1) {
                        currentIndex++
                        answered = false //reset for next question
                        selectedAnswer = "" //clear for selected ans
                    } else {
                        showResult = true //show final result
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color((0xFF2196F3)))
        ) {
            Text(
                text = if (answered) "Next" else "Submit",
                color = Color.White
            )
        }
    }
}
