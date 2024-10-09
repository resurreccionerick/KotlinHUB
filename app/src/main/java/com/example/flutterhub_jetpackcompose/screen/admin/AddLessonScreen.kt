package com.example.flutterhub_jetpackcompose.screen.admin

import android.content.Context
import android.graphics.Paint.Align
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.example.flutterhub_jetpackcompose.viewmodel_repository.LessonViewModel

@Composable
fun AddLessonScreen(navController: NavController, viewModel: LessonViewModel, context: Context) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imgUrl by remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imgUrl = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Add Lesson")

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Lesson title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            launcher.launch("image/*")
        }, modifier = Modifier.align(Alignment.End)) { Text("Pick Image") }

        //show the picked img if available
        imgUrl?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(imgUrl),
                contentDescription = "Selected img",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Crop
            )
        }


        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            if (title.isNotEmpty() && description.isNotEmpty() && imgUrl != null) {
                imgUrl?.let { uri ->

                    viewModel.addNewLesson(title, description, imgUrl!!)

                    // Navigate back to Home screen after adding the lesson
                    navController.popBackStack()
                }
            } else {
                Toast.makeText(context, "Please enter all fields", Toast.LENGTH_LONG).show()
            }

        }) { Text("Add Lesson") }
    }
}