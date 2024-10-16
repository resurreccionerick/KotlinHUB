package com.example.flutterhub_jetpackcompose.viewmodel_repository


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.example.flutterhub_jetpackcompose.models.QuizModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//The ViewModel acts as the bridge between the Repository and the UI. It fetches the data and holds the UI state.
@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: LessonRepository
) : ViewModel() {

    val quizzes = mutableStateListOf<QuizModel>()
    val lessons = mutableStateListOf<LessonModel>()

    fun refreshLessonDifficulty() {
        repository.refreshDifficulty()
        loadLessons()
    }


    fun refreshQuizDifficulty(){
        repository.refreshDifficulty()
        loadQuizzes()
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
        link: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val lesson = LessonModel(
                id = "", name = name,
                description = desc, link = link
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

    fun userLogout(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.userLogout(onSuccess = {
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


    // ---------------------------------------------------- QUIZZES ---------------------------------------------------- //

    fun loadQuizzes() {
        viewModelScope.launch {
            quizzes.clear()
            quizzes.addAll(repository.getQuizzes())

        }
    }

    fun addQuiz(
        question: String,
        choices: List<String>,
        selectedAns: String,
        onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val quiz = QuizModel(
                question = question,
                choices = choices,
                selectedAns = selectedAns
            );
            repository.addQuiz(
                quiz,
                onSuccess = {
                    onSuccess()
                    loadQuizzes() // refresh
                },
                onFailure = { errorMsg ->
                    onFailure(errorMsg)
                })
        }
    }


    fun deleteQuiz(id: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.deleteQuiz(id, onSuccess = {
                onSuccess()
                loadQuizzes() // refresh
            }, onFailure = { errorMsg ->
                onFailure(errorMsg)
            })
        }
    }

    fun getQuizByID(quizId: String): QuizModel {
        // This function returns the quiz by its ID.
        return quizzes.find { it.id == quizId } ?: QuizModel()
    }


    fun updateQuiz(quiz: QuizModel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.updateQuiz(quiz, onSuccess = {
                onSuccess()
                loadQuizzes() // refresh
            }, onFailure = { errorMsg ->
                onFailure(errorMsg)
            })
        }
    }
}