package com.example.flutterhub_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flutterhub_jetpackcompose.screen.components.BottomNavigationBar
import com.example.flutterhub_jetpackcompose.screen.navigation.NavGraph
import com.example.flutterhub_jetpackcompose.ui.theme.KotlinHubTheme
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            KotlinHubTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()   // Observe the current route
                val currentRoute = navBackStackEntry?.destination?.route

                if (Hawk.get<String?>("role") == null) {
                    NavGraph(navController, viewModel, this@MainActivity)
                } else {
                        if (currentRoute == "userHome" || currentRoute == "overallLeaderboards" || currentRoute == "userSettings") {
                            Scaffold(bottomBar = {
                                BottomNavigationBar(navController)
                            }
                            )
                            { padding ->
                                Box(modifier = Modifier.padding(padding)) {
                                    NavGraph(navController, viewModel, this@MainActivity)
                                }
                            }
                        } else {
                            NavGraph(navController, viewModel, this@MainActivity)
                        }
                    }
                }
            }
        }
    }


