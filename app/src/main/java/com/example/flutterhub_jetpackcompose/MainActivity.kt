package com.example.flutterhub_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.flutterhub_jetpackcompose.navigation.NavGraph
import com.example.flutterhub_jetpackcompose.viewmodel_repository.AppViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavGraph(navController, viewModel, this)
        }
    }
}

