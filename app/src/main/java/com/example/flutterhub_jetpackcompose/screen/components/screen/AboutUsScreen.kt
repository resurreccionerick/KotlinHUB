package com.example.flutterhub_jetpackcompose.screen.components.screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.screen.components.card.ImageCardWithRole
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    navController: NavController,
    viewModel: AppViewModel,
    context: Context,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Meet the team") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(
                        8.dp
                    ),
                ) {
                    ImageCardWithRole(
                        label = "Nathaniel Domingo",
                        role = "Backend Developer",
                        imageRes = R.drawable.developer,
                        imgHeight = 250,
                        onClick = {

                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ImageCardWithRole(
                        label = "Hanan Edres",
                        role = "Software Developer",
                        imageRes = R.drawable.developer,
                        imgHeight = 250,
                        onClick = {

                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ImageCardWithRole(
                        label = "Carl Louie Felipe",
                        role = "Frontend Developer",
                        imageRes = R.drawable.developer,
                        imgHeight = 250,
                        onClick = {

                        },
                        modifier = Modifier.weight(1f)
                    )

                }
            }
        })
}