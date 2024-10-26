package com.example.flutterhub_jetpackcompose.screen.lessons.user

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.flutterhub_jetpackcompose.R
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.screen.components.LessonCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicHomeScreen(navController: NavController, viewModel: AppViewModel, context: Context) {

    Scaffold(
        containerColor = colorResource(id = R.color.metal_black),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.metal_black2),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                title = { Text(text = "Basic Lesson") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },

        floatingActionButton = {
            if(Hawk.get<Boolean?>("role").equals("admin")){
                FloatingActionButton(onClick = {
                    navController.navigate("addLesson")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add lesson")
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

            LazyColumn {
                items(viewModel.lessons) { lesson ->
                    LessonCard(navController, lesson, viewModel, context)
                }
            }
        }
    }
}

