package com.example.flutterhub_jetpackcompose.screen.assessment.user

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.flutterhub_jetpackcompose.screen.components.AssessmentCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
    context: Context
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Assessment") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        },


        floatingActionButton = {
            if (Hawk.get<Boolean?>("role").equals("admin")) {
                FloatingActionButton(onClick = {
                    navController.navigate("AddAssessment")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Assessment")
                }
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn() {
                items(viewModel.assessment) { assessment ->
                    AssessmentCard(navController, assessment, viewModel, context)
                }
            }
        }

    }
}