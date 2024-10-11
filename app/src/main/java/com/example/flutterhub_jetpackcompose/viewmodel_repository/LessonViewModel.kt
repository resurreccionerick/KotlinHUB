package com.example.flutterhub_jetpackcompose.viewmodel_repository

import androidx.compose.runtime.mutableStateListOf
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

    fun addNewLesson(
        name: String,
        desc: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val lesson = LessonModel(
                id = "", name = name,
                description = desc,
            )

            repository.addLesson(lesson,
                onSuccess = {
                    onSuccess()
                    loadLessons() //refresh the list
                }, onFailure = { errorMsg ->
                    onFailure(errorMsg)
                })

        }
    }


    fun updateLesson(lesson: LessonModel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.updateLesson(lesson, onSuccess, onFailure)

            loadLessons() //refresh list
        }
    }

    fun deleteLesson(lessonID: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.deleteLesson(lessonID, onSuccess, onFailure)
            loadLessons() //refresh
        }
    }


    // ---------------------------------------------------- USER PART ---------------------------------------------------- //
    fun userSignUp(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.userRegister(name, email, password, onSuccess = {
                onSuccess()
            }, onFailure = { errorMsg ->
                onFailure(errorMsg)
            });
        }
    }

    fun userLogin(
        email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {

        viewModelScope.launch {
            repository.userLogin(email, password, onSuccess = {
                onSuccess()
            }, onFailure = { errorMsg ->
                onFailure(errorMsg)
            })
        }
    }

    fun forgotPass(
        email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.forgotPass(email, onSuccess = {
                onSuccess()
            }, onFailure = { errorMsg ->
                onFailure(errorMsg)
            })
        }
    }

    fun getLessonById(lessonId: String): LessonModel {
        // This function returns the lesson by its ID.
        return lessons.find { it.id == lessonId } ?: LessonModel()
    }

}