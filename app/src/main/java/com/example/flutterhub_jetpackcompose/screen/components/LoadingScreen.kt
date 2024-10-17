package com.example.flutterhub_jetpackcompose.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(isLoading: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Your main content goes here
        MainContent()

        // Show the progress bar when loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)), // semi-transparent background
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator() // Add your progress indicator
            }
        }
    }
}

@Composable
fun MainContent() {
    // This is your main content that will be displayed
    // Replace this with your actual content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
    }
}
