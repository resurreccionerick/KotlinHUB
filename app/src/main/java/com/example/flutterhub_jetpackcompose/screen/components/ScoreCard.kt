package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.QuizScoreModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel


@Composable
fun ScoreCard(
    navController: NavController,
    score: QuizScoreModel,
    viewModel: AppViewModel,
    context: Context
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                Text(
                    text = score.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = score.score + " Points",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}