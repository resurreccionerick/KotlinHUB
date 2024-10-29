package com.example.flutterhub_jetpackcompose.screen.assessment.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.AssessmentModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssessmentScreen(
    navController: NavController, viewModel: AppViewModel, context: Context
) {
    var title by rememberSaveable { mutableStateOf("") }
    var instruction by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Assessment") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }, modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = instruction,
                onValueChange = { instruction = it },
                label = { Text("Input the instructions here") },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), maxLines = 20
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val assessment = AssessmentModel(
                    id = "", title = title,
                    instructions = instruction
                )

                if (title.isNotEmpty() || instruction.isNotEmpty()) {
                    viewModel.addAssessment(assessment, onSuccess = {
                        Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }, onFailure = { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    })
                } else {
                    Toast.makeText(context, "Please Enter All Fields!", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.align(Alignment.End)) { Text("Add") }
        }

    }

}