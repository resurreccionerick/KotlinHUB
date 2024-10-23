package com.example.flutterhub_jetpackcompose.screen.admin.quiz

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import com.example.flutterhub_jetpackcompose.data.models.QuizModel
import com.example.flutterhub_jetpackcompose.screen.components.AnswerDropDown
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditQuizScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
    quizModel: QuizModel,
    context: Context
) {
    var question by rememberSaveable { mutableStateOf(quizModel.question) }
    var correctDesc by rememberSaveable { mutableStateOf(quizModel.correctDesc) }
    var wrongDesc by rememberSaveable { mutableStateOf(quizModel.wrongDesc) }
    // var difficulty by rememberSaveable { mutableStateOf(quizModel.difficulty) }
    var selectedAns by rememberSaveable { mutableStateOf(quizModel.selectedAns) }
    var choices = remember {
        mutableStateListOf(
            quizModel.choices[0],
            quizModel.choices[1],
            quizModel.choices[2],
            quizModel.choices[3]
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Quiz") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown to select difficulty
                // DifficultyDropDown(selectedDifficulty = difficulty) { difficulty = it }

                Spacer(modifier = Modifier.height(8.dp))

                //radiobutton 4 choices
                Text("Choices:")
                choices.forEachIndexed { index, choice ->
                    OutlinedTextField(
                        value = choice,
                        onValueChange = { choices[index] = it },
                        label = { Text("Choices ${index + 1}") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))


                // Dropdown to select the correct answer
                AnswerDropDown(selectedAns = quizModel.selectedAns,
                    choices = choices.filter { it.isNotBlank() },
                    onAnswerSelected = { selectedAns = it })

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = correctDesc,
                    onValueChange = { correctDesc = it },
                    label = { Text("Correct Answer Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = wrongDesc,
                    onValueChange = { wrongDesc = it },
                    label = { Text("Wrong Answer Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (correctDesc.isNotEmpty() && wrongDesc.isNotEmpty() && question.isNotEmpty() && choices.all { it.isNotBlank() } &&
                        selectedAns != null
                    ) {
                        val quiz = quizModel.copy(
                            question = question,
                            //difficulty = difficulty,
                            choices = choices,
                            selectedAns = selectedAns,
                            correctDesc = correctDesc,
                            wrongDesc = wrongDesc,
                        )
                        viewModel.updateQuiz(quiz,
                            onSuccess = {
                                Toast.makeText(context, "Quiz Updated!", Toast.LENGTH_LONG).show()
                                navController.popBackStack()
                            },
                            onFailure = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            })

                    } else {
                        Toast.makeText(context, "Please enter all fields!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, modifier = Modifier.align(Alignment.End)) {
                    Text("Update Quiz")
                }


            }
        }
    )
}


