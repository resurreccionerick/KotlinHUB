package com.example.flutterhub_jetpackcompose.screen.admin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.example.flutterhub_jetpackcompose.screen.LessonCard
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun IntermediateHomeScreen(
    navController: NavController,
    viewModel: LessonViewModel,
    context: Context
) {


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Hawk.put("difficulty", "intermediate")
                navController.navigate("addLesson")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add lesson")
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
            Text("Lesson: ")
            LazyColumn {
                items(viewModel.lessons) { lesson ->
                    LessonCard(navController, lesson, viewModel, context)
                }
            }
        }
    }
}

