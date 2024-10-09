package com.example.flutterhub_jetpackcompose.screen.admin//package com.example.flutterhub_jetpackcompose.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import com.example.flutterhub_jetpackcompose.models.LessonModel
//import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel
//
//
//@Composable
//fun LessonScreen(viewModel: LessonViewModel, navController: NavController)  {
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(onClick = { navController.navigate("addLesson") }) {
//                Icon(Icons.Default.Add, contentDescription = "Add Lesson")
//            }
//        }
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Lessons", style = MaterialTheme.typography.h4)
//
//            LazyColumn {
//                items(viewModel.lessons) { lesson ->
//                    LessonCard(lesson, viewModel)
//                }
//            }
//        }
//    }
//}