package com.example.flutterhub_jetpackcompose.screen.admin.quiz

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddQuizScreen(
    navController: NavHostController,
    viewModel: LessonViewModel,
    context: Context
) {
    var question by rememberSaveable { mutableStateOf("") }
    var difficulty by rememberSaveable { mutableStateOf("") }
    var selectedAns by rememberSaveable { mutableStateOf("") }
    var choices = remember { mutableStateListOf("", "", "", "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Quiz") },
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
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dropdown to select difficulty
                DifficultyDropDown(selectedDifficulty = difficulty) { difficulty = it }

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
                AnswerDropDown(selectedAns) { selectedAns = it }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (question.isNotEmpty() && difficulty.isNotBlank() && choices.all { it.isNotBlank() } &&
                        selectedAns.isNotBlank()
                    ) {
                        viewModel.addQuiz(question, difficulty, choices, selectedAns,
                            onSuccess = {
                                Toast.makeText(context, "Quiz Added!", Toast.LENGTH_LONG).show()
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
                    Text("Add Quiz")
                }


            }
        }
    )
}

@Composable
fun DifficultyDropDown(selectedDifficulty: String, onDifficultySelected: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val difficulties = listOf("Basic", "Intermediate")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedDifficulty,
            onValueChange = {},
            label = { Text("Difficulty") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Difficulty")
                }
            }
        )

        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            difficulties.forEach { difficulty ->
                DropdownMenuItem(
                    text = { Text(difficulty) },
                    onClick = {
                        onDifficultySelected(difficulty)
                        expanded = false

                    }
                )
            }
        }
    }
}

@Composable
fun AnswerDropDown(
    selectedAns: String,
    onAnswerSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val answerOption = listOf("Choice 1", "Choice 2", "Choice 3", "Choice 4")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedAns,
            onValueChange = {},
            label = { Text("Correct Answer") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Answer")
                }
            }
        )

        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false })
        {
            answerOption.forEach { answer ->
                DropdownMenuItem(
                    text = { Text(answer) },
                    onClick = {
                        onAnswerSelected(answer)
                        expanded = false
                    }
                )
            }
        }
    }
}
