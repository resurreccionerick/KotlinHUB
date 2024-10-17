package com.example.flutterhub_jetpackcompose.screen.user

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

import com.example.flutterhub_jetpackcompose.data.models.QuizModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel


@Composable
fun UserQuizScreen(
    navController: NavController,
    viewModel: AppViewModel,
    context: Context
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (showResult) {
        // Show the quiz result
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Quiz Completed!", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Your Score: $score / ${quizzes.size}", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back to Home")
            }
        }
        return
    }

    // Get the current quiz question
    val currentQuiz = quizzes[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show question number
        Text(
            text = "Question ${currentIndex + 1} / ${quizzes.size}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show the question
        Text(text = currentQuiz.question, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Display answer choices
        currentQuiz.choices.forEach { choice ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedAnswer = choice }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedAnswer == choice,
                    onClick = {
                        if (!answered) {
                            selectedAnswer = choice
                        }
                    },
                    enabled = !answered // Disable after answering

                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = choice,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable(enabled = !answered) {
                        if (!answered) selectedAnswer = choice
                    })
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        // Single button for both actions
        Button(
            onClick = {
                if (!answered) {
                    // Check if answer is selected
                    if (selectedAnswer.isNotEmpty()) {
                        val isCorrect = selectedAnswer == currentQuiz.selectedAns
                        if (isCorrect) score++

                        // Show toast feedback
                        Toast.makeText(
                            context,
                            if (isCorrect) "Correct! ${currentQuiz.selectedAns}" else "Wrong! ${currentQuiz.selectedAns}",
                            Toast.LENGTH_SHORT
                        ).show()

                        answered = true // Mark as answered
                    } else {
                        Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // Move to the next question or show result
                    if (currentIndex < quizzes.size - 1) {
                        currentIndex++
                        answered = false // Reset for next question
                        selectedAnswer = "" // Clear selected answer
                    } else {
                        showResult = true // Show final result
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor =
            if(answered){
                Color(0xFF1A1A1A)
            }else{
                Color(0xFF2196F3)
            }


            )
        ) {
            Text(
                text = if (answered) "Next" else "Submit",
                color = Color.White
            )
        }
    }
}
