package com.example.flutterhub_jetpackcompose.screen.lessons.admin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.LessonModel
import com.example.flutterhub_jetpackcompose.data.models.LessonSubtopic
import com.example.flutterhub_jetpackcompose.screen.components.card.LessonCard
import com.example.flutterhub_jetpackcompose.screen.components.card.SubLessonCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubLessonListScreen(
    navController: NavController,
    viewModel: AppViewModel,
    lessonID: String,
    context: Context
) {
    LaunchedEffect(lessonID) {
        viewModel.loadSubLessons(lessonID)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Table of contents") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (Hawk.get<String?>("role") == "admin") {
                FloatingActionButton(
                    onClick = { navController.navigate("addLesson/$lessonID") }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Lesson")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isLoading.value) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(viewModel.subLessons) { subLesson ->
                        SubLessonCard(
                            navController = navController,
                            lessonID = lessonID,
                            subTopic = subLesson,
                            viewModel = viewModel,
                            context = context
                        )
                    }
                }
            }
        }
    }
}
