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
import com.example.flutterhub_jetpackcompose.screen.components.AnswerDropDown
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddQuizScreen(
    navController: NavHostController, viewModel: AppViewModel, context: Context
) {
    var question by rememberSaveable { mutableStateOf("") }
    //var difficulty by rememberSaveable { mutableStateOf("") }
    var selectedAns by rememberSaveable { mutableStateOf("") }
    var correctDescription by rememberSaveable { mutableStateOf("") }
    var wrongDescription by rememberSaveable { mutableStateOf("") }
    var choices = remember { mutableStateListOf("", "", "", "") }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Add Quiz") }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back") }
        })
    }, content = { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            OutlinedTextField(value = question,
                onValueChange = { question = it },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown to select difficulty
//                DifficultyDropDown(selectedDifficulty = difficulty) { difficulty = it }

            Spacer(modifier = Modifier.height(8.dp))

            //radiobutton 4 choices
            Text("Choices:")
            choices.forEachIndexed { index, choice ->
                OutlinedTextField(value = choice,
                    onValueChange = { choices[index] = it },
                    label = { Text("Choices ${index + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown to select the correct answer
            AnswerDropDown(selectedAns = selectedAns,
                choices = choices.filter { it.isNotBlank() },
                onAnswerSelected = { selectedAns = it })

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = correctDescription,
                onValueChange = { correctDescription = it },
                label = { Text("Correct Answer Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = wrongDescription,
                onValueChange = { wrongDescription = it },
                label = { Text("Wrong Answer Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (wrongDescription.isNotEmpty() && correctDescription.isNotEmpty() && question.isNotEmpty() && choices.all { it.isNotBlank() } && selectedAns.isNotBlank() ) {
                    viewModel.addQuiz(question,
                        choices,
                        selectedAns,
                        correctDesc = correctDescription,
                        wrongDesc = wrongDescription,
                        onSuccess = {
                            Toast.makeText(context, "Quiz Added!", Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        },
                        onFailure = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        })

                } else {
                    Toast.makeText(context, "Please enter all fields!", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.align(Alignment.End)) {
                Text("Add Quiz")
            }


        }
    })
}




