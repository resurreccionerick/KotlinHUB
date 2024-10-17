package com.example.flutterhub_jetpackcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.flutterhub_jetpackcompose.navigation.NavGraph
import com.example.flutterhub_jetpackcompose.utils.BottomNavigationBar

import com.example.flutterhub_jetpackcompose.viewmodel_repository.AppViewModel
import com.orhanobut.hawk.Hawk

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            if(Hawk.get<Boolean?>("role").equals("admin")){
                NavGraph(navController, viewModel, this@MainActivity)
                Toast.makeText(this, "admin", Toast.LENGTH_SHORT).show()
            }else{
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) } // Add Bottom Navigation Bar
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        NavGraph(navController, viewModel, this@MainActivity)
                    }
                }
            }


        }
    }
}

