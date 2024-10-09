package com.example.flutterhub_jetpackcompose.viewmodel_repository

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flutterhub_jetpackcompose.models.LessonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//The ViewModel acts as the bridge between the Repository and the UI. It fetches the data and holds the UI state.
@HiltViewModel
class LessonViewModel @Inject constructor(
    private val repository: LessonRepository
) : ViewModel() {

    val lessons = mutableStateListOf<LessonModel>()

    init {
        loadLessons()
    }

    private fun loadLessons() {
        viewModelScope.launch {
            lessons.clear()
            lessons.addAll(repository.getLessons())
        }
    }

    fun addNewLesson(name: String, desc: String, img: Uri) {
        viewModelScope.launch {
            val imgUrl = repository.uploadImageToFirebase(img)
            if(imgUrl !=null ){
                val lesson = LessonModel(
                    id = "",
                    name = name,
                    description =  desc,
                    imgUrl = imgUrl
                )

                repository.addLesson(lesson)
                loadLessons() //refresh the list
            }
        }
    }


    fun updateLesson(lesson: LessonModel) {
        viewModelScope.launch {
            repository.updateLesson(lesson)

            loadLessons() //refresh list
        }
    }

    fun deleteLesson(lessonID: String) {
        viewModelScope.launch {
            repository.deleteLesson(lessonID)
        }
    }
}