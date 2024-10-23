package com.example.flutterhub_jetpackcompose.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AnswerDescription(status: Boolean, data: String) {
    Card(
        modifier = Modifier
            .wrapContentSize() // Card will wrap its content
            .padding(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (status) {
                Color.Green.copy(alpha = 0.5f) // Background color for "Correct"
            } else {
                Color.Red.copy(alpha = 0.5f) // Background color for "Wrong"
            }
        ),
        elevation = CardDefaults.cardElevation(4.dp) // Card elevation (shadow effect)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center) // Align all contents in the center
            ) {
                Text(
                    text = if (status) {
                        "Correct!"
                    } else {
                        "Wrong!"
                    },

                    color = if (status) {
                        Color.Black
                    } else {
                        Color.White
                    },

                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Start // Align the text to start
                )

                Spacer(modifier = Modifier.height(8.dp)) // Space between the two texts

                Text(
                    text = data,

                    color = if (status) {
                        Color.Black
                    } else {
                        Color.White
                    },

                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the data text
                )
            }
        }
    }
}
