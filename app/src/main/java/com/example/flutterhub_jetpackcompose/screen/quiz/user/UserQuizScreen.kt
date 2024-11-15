package com.example.flutterhub_jetpackcompose.screen.user

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.QuizModel
import com.example.flutterhub_jetpackcompose.data.models.QuizScoreModel
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.screen.components.AnswerDescription
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk


@Composable
fun UserQuizScreen(
    navController: NavController,
    viewModel: AppViewModel,
    quizScoreModel: QuizScoreModel,
    context: Context
) {
    var quizzes by remember { mutableStateOf<List<QuizModel>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var answered by remember { mutableStateOf(false) } // Track if answer is submitted
    var showDesc by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

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
            Toast.makeText(context, "No quiz available", Toast.LENGTH_SHORT)
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
            Button(onClick = {

                val userModel = Hawk.get<UserModel>("user_details")

                val scoreModel =
                    quizScoreModel.copy(
                        id = userModel.id,
                        score = score.toString(),
                        name = userModel.name
                    )

                viewModel.refreshLessonDifficulty()
                viewModel.saveQuizScore(scoreModel,
                    onSuccess = {
                        Toast.makeText(context, "Score saved", Toast.LENGTH_SHORT).show();
                    }, onFailure = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    })
                navController.popBackStack()

            }) {
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
                        isCorrect = selectedAnswer == currentQuiz.selectedAns
                        showDesc = isCorrect

                        if (isCorrect) {
                            score++
                        }

                        // Show toast feedback
//                        if (isCorrect) {
//                            Toast.makeText(
//                                context,
//                                "Correct! ${currentQuiz.selectedAns}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            Toast.makeText(
//                                context,
//                                "Wrong! ${currentQuiz.selectedAns}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }

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
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (answered) {
                    Color(0xFF1A1A1A)
                } else {
                    Color(0xFF2196F3)
                }
            )
        )
        {
            Text(
                text = if (answered) "Next" else "Submit",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            if (answered) {
                if(isCorrect){
                    AnswerDescription(isCorrect,currentQuiz.correctDesc)
                }else{
                    AnswerDescription(isCorrect,currentQuiz.wrongDesc)
                }
            }
        }
    }
}
