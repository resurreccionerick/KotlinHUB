package com.example.flutterhub_jetpackcompose.screen.home

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flutterhub_jetpackcompose.R
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.example.flutterhub_jetpackcompose.screen.components.card.ImageCard
import com.example.flutterhub_jetpackcompose.screen.components.card.ImageRowCard
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(navController: NavController, viewModel: AppViewModel, context: Context) {
    var isRightDrawerOpen by remember { mutableStateOf(false) }
    val animatedValue = remember { Animatable(0f) }
    var user by remember { mutableStateOf<UserModel?>(null) }
    val darkModeState = viewModel.darkModeState.value

    LaunchedEffect(Unit) {
        viewModel.refreshProfileDetails()
        user = Hawk.get("user_details")
        animatedValue.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content (unchanged from your original)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (!Hawk.get<Boolean?>("role").equals("admin")) {
                            if (user != null) {
                                Text("Hello, " + user!!.name)
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { isRightDrawerOpen = true }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .graphicsLayer(alpha = animatedValue.value)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("Kotlin Lesson: ", fontWeight = FontWeight.Bold)

                        Row() {
                            ImageCard(
                                label = "Basic",
                                imageRes = R.drawable.easy_colored,
                                imgHeight = 120,
                                onClick = {
                                    Hawk.put("difficulty", "basic")
                                    viewModel.refreshLessonDifficulty()
                                    navController.navigate("adminBasic")
                                },
                                modifier = Modifier.weight(1f)
                            )

                            ImageCard(
                                label = "Intermediate",
                                imageRes = R.drawable.muscle_colored,
                                imgHeight = 120,
                                onClick = {
                                    Hawk.put("difficulty", "intermediate")
                                    viewModel.refreshLessonDifficulty()
                                    navController.navigate("adminIntermediate")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Test your knowledge: ", fontWeight = FontWeight.Bold)

                        Row() {
                            ImageCard(
                                label = "Quiz",
                                imageRes = R.drawable.quiz,
                                imgHeight = 120,
                                onClick = {
                                    viewModel.loadQuizzes()
                                    navController.navigate("difficultyQuiz")
                                },
                                modifier = Modifier.weight(1f)
                            )

                            ImageCard(
                                label = "Assessment",
                                imageRes = R.drawable.developer,
                                imgHeight = 120,
                                onClick = {
                                    navController.navigate("assessmentHome")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        ImageCard(
                            label = "Kotlin Sandbox",
                            imageRes = R.drawable.code_runner,
                            imgHeight = 120,
                            onClick = {
                                val encodedUrl = Uri.encode("https://play.kotlinlang.org/")
                                navController.navigate("webViewAssessment/${encodedUrl}")
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        Text("Other Topics: ", fontWeight = FontWeight.Bold)

                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .verticalScroll(rememberScrollState())
                                .padding(8.dp)
                                .weight(1f)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ImageRowCard(
                                    label = "Latest Kotlin News",
                                    imageRes = R.drawable.newspaper,
                                    onClick = {
                                        Hawk.put("title", "")
                                        Hawk.put("link", "https://blog.jetbrains.com/kotlin/")
                                        navController.navigate("webView")
                                    }
                                )

                                ImageRowCard(
                                    label = "Kotlin Forum",
                                    imageRes = R.drawable.chat,
                                    onClick = {
                                        Hawk.put("title", "")
                                        Hawk.put("link", "https://discuss.kotlinlang.org/")
                                        navController.navigate("webView")
                                    }
                                )

                                ImageRowCard(
                                    label = "Install Android Studio",
                                    imageRes = R.drawable.android,
                                    onClick = {
                                        Hawk.put("title", "")
                                        Hawk.put("link", "https://developer.android.com/studio/install")
                                        navController.navigate("webView")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        )

        // Right Side Drawer
        if (isRightDrawerOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            isRightDrawerOpen = false
                        }
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .align(Alignment.CenterEnd)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {

                // Drawer Items
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {
                        navController.navigate("profile")
                        isRightDrawerOpen = false
                    },
                    icon = { Icon(Icons.Default.Person, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Easy Lesson") },
                    selected = false,
                    onClick = {
                        Hawk.put("difficulty", "basic")
                        viewModel.refreshLessonDifficulty()
                        navController.navigate("adminBasic")
                        isRightDrawerOpen = false
                    },
                    icon = { Icon(Icons.Default.School, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Intermediate Lesson") },
                    selected = false,
                    onClick = {
                        Hawk.put("difficulty", "intermediate")
                        viewModel.refreshLessonDifficulty()
                        navController.navigate("adminIntermediate")
                        isRightDrawerOpen = false
                    },
                    icon = { Icon(Icons.Default.Code, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Code Runner") },
                    selected = false,
                    onClick = {
                        val encodedUrl = Uri.encode("https://play.kotlinlang.org/")
                        navController.navigate("webViewAssessment/${encodedUrl}")
                        isRightDrawerOpen = false
                    },
                    icon = { Icon(Icons.Default.PlayArrow, null) }
                )

                NavigationDrawerItem(
                    label = { Text("News") },
                    selected = false,
                    onClick = {
                        Hawk.put("title", "")
                        Hawk.put("link", "https://blog.jetbrains.com/kotlin/")
                        navController.navigate("webView")
                        isRightDrawerOpen = false
                    },
                    icon = { Icon(Icons.Default.Newspaper, null) }
                )

                Spacer(modifier = Modifier.weight(1f))

                Divider()

                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        navController.navigate("userSettings")
                        isRightDrawerOpen = false
                    },
                    icon = { Icon(Icons.Default.Settings, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        viewModel.userLogout(onSuccess = {
                            Hawk.deleteAll()
                            navController.navigate("login") {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }, onFailure = { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        })
                        isRightDrawerOpen = false
                    },
                    icon = { Icon(Icons.Default.ExitToApp, null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Red.copy(alpha = 0.2f),
                        unselectedContainerColor = Color.Transparent
                    )
                )

                Divider()
                Column(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                ) {
                    // Switch for toggling theme
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (darkModeState) "Light Mode" else "Dark Mode",
                            modifier = Modifier.padding(12.dp)
                        )
                        Switch(
                            checked = darkModeState,
                            onCheckedChange = { isChecked ->
                                // Toggle the theme state in ViewModel
                                viewModel.toggleDarkMode(isChecked)
                            }
                        )
                    }
                }
            }
        }
    }
}
