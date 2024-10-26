package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(navController: NavController, viewModel: AppViewModel, context: Context) {

    if (viewModel.quizzes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent overlay
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

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
                title = { Text("Leaderboard") }, navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        },

        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            {
                LazyColumn {
                    items(viewModel.scores) { score ->
                        ScoreCard(navController, score, viewModel, context)
                    }
                }
            }
        }
    )
}



