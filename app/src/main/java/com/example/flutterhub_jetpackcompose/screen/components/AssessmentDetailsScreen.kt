package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.AssessmentModel
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentDetailsScreen(
    navController: NavController,
    userID: String,
    userName: String,
    viewModel: AppViewModel,
    context: Context,
    assessmentModel: AssessmentModel,
) {
    var link by rememberSaveable { mutableStateOf("") }
    var isApproved by remember { mutableStateOf(false) }
    var getLink by remember { mutableStateOf("") }

    // Fetch user check status on screen load
    LaunchedEffect(Unit) {
        viewModel.checkUserStatus(assessmentModel.id, userID, onResult = { data ->
            isApproved = data
        }, getLink = { res ->
            getLink = res
        })
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(assessmentModel.title) }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
                )
            }
        })
    }, content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // LazyColumn for scrollable content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                        Text(
                            text = assessmentModel.instructions,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    if (!isApproved) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    openTheLink(
                                        context, "https://pl.kotl.in/Eaa0qDeSD"
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.Code, contentDescription = "Code Runner"
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Let's do this!",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.onTertiaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    openTheLink(
                                        context, getLink
                                    )
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.Code, contentDescription = "Code Runner"
                                    )
                                },
                                text = { Text(text = "See your work") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.onTertiaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                contentColor = Color.White
                            )
                        }
                    }
                }
            }


            if (!isApproved) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Aligns this at the bottom
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(0.5f))
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = link,
                        onValueChange = { link = it },
                        label = { Text("Paste your answer link here") }
                    )

                    Button(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp),
                        onClick = {
                            if (isKotlinLangUrl(link)) {
                                viewModel.saveAssessmentLink(
                                    assessmentModel,
                                    userID,
                                    userName,
                                    link,
                                    onSuccess = {
                                        Toast.makeText(
                                            context, "Successfully Submitted", Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    },
                                    onFailure = { msg ->
                                        Toast.makeText(
                                            context, msg, Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            } else {
                                Toast.makeText(context, "Invalid Link", Toast.LENGTH_SHORT).show()
                            }
                        },
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    })
}

fun openTheLink(context: Context, s: String) {
    val url = s
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)

//    if (intent.resolveActivity(context.packageManager) != null) {
    context.startActivity(intent)  // Launch browser with the URL
//    } else {
//        Toast.makeText(
//            context, "No browser found to open this link", Toast.LENGTH_SHORT
//        ).show()
//    }
}

fun isKotlinLangUrl(url: String): Boolean {
    // Check if the URL is valid and starts with the specified domain
    return Patterns.WEB_URL.matcher(url).matches() && url.contains("kotl", ignoreCase = true)
}