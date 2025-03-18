package com.example.flutterhub_jetpackcompose.screen.components.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.flutterhub_jetpackcompose.data.models.LessonSubtopic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailsScreen(
    navController: NavController,
    context: Context,
    lessonModel: LessonSubtopic,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lesson Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            // Use `Column` inside `LazyColumn` to make the content scrollable
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = lessonModel.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                    )
                }

                item {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(8.dp),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        // Add padding inside the Card
                        Text(
                            text = lessonModel.description,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    // Use Box to apply background color to the button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                openYoutubeVid(context, lessonModel.link)
                            },
                            icon = {
                                Icon(
                                    Icons.Filled.VideoLibrary,
                                    contentDescription = "Watch Tutorial"
                                )
                            },
                            text = { Text(text = "Watch Tutorial") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Red, shape = RoundedCornerShape(8.dp)),
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    }
                }
            }
        }
    )
}

fun openYoutubeVid(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setPackage("com.google.android.youtube") // Force open in YouTube if installed
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // Ensure new task behavior
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback if YouTube app is not available
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (webIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(webIntent)
        } else {
            Toast.makeText(context, "No application can handle this request", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
