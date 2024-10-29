package com.example.flutterhub_jetpackcompose.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flutterhub_jetpackcompose.data.models.AssessmentModel
import com.example.flutterhub_jetpackcompose.data.models.LessonModel
import com.example.flutterhub_jetpackcompose.data.models.QuizModel
import com.example.flutterhub_jetpackcompose.data.models.QuizScoreModel
import com.example.flutterhub_jetpackcompose.data.repository.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//The ViewModel acts as the bridge between the Repository and the UI. It fetches the data and holds the UI state.
@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: LessonRepository
) : ViewModel() {
    var selectedAssessment by mutableStateOf<AssessmentModel?>(null)
    val assessment = mutableStateListOf<AssessmentModel>()
    val quizzes = mutableStateListOf<QuizModel>()
    val lessons = mutableStateListOf<LessonModel>()
    val scores = mutableStateListOf<QuizScoreModel>()
    private var isLoading = false

    fun refreshLessonDifficulty() {
        repository.refreshDifficulty()
        loadLessons()
    }


    fun refreshQuizDifficulty() {
        repository.refreshDifficulty()
        loadQuizzes()
    }

    fun refreshLeaderboardsDifficulty() {
        repository.refreshDifficulty()
        loadLeaderboards()
    }


    private fun loadLessons() {
        if (isLoading) return

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
        if (isLoading) return

        viewModelScope.launch {
            val newQuizzes = repository.getQuizzes()
            quizzes.clear()
            quizzes.addAll(newQuizzes)
        }
    }


    fun saveQuizScore(
        scoreModel: QuizScoreModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.saveQuiz(scoreModel,
                onSuccess = {
                    onSuccess()
                }, onFailure = { errorMsg ->
                    onFailure(errorMsg)
                })
        }
    }


    fun addQuiz(
        question: String,
        choices: List<String>,
        selectedAns: String,
        correctDesc: String,
        wrongDesc: String,
        onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val quiz = QuizModel(
                question = question,
                choices = choices,
                selectedAns = selectedAns,
                correctDesc = correctDesc,
                wrongDesc = wrongDesc
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

    // ---------------------------------------------------- SCORES ---------------------------------------------------- //
    private fun loadLeaderboards() {
        if (isLoading) return

        viewModelScope.launch {
            scores.clear()
            scores.addAll(repository.getScores())
        }
    }


    // ---------------------------------------------------- ASSESSMENT ---------------------------------------------------- //
    fun addAssessment(
        assessmentModel: AssessmentModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.addAssessment(assessmentModel, onSuccess = {
                onSuccess()
            }, onFailure = { msg ->
                onFailure(msg)
            })
        }
    }

    fun loadAssessment() {
        if (isLoading) return

        viewModelScope.launch {
            val _assessment = repository.getAssessment()

            assessment.clear()
            assessment.addAll(_assessment)
        }
    }

    fun getAssessmentById(assessmentId: String): AssessmentModel {
        // This function returns the lesson by its ID.
        return assessment.find { it.id == assessmentId } ?: AssessmentModel()
    }

    fun updateAssessment(
        assessment: AssessmentModel,
        onSuccess: () -> Boolean,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.updateAssessment(assessment, onSuccess = {
                onSuccess()
                loadAssessment()
            }, onFailure = { msg ->
                onFailure(msg)
            })
        }
    }

    fun deleteAssessment(id: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            repository.deleteAssessment(id, onSuccess = {
                onSuccess()
                loadAssessment()
            }, onFailure = { msg ->
                onFailure(msg)
            })
        }
    }

    // ---------------------------------------------------- SAVE ASSESSMENT LINK ---------------------------------------------------- //
    fun saveAssessmentLink(
        assessmentModel: AssessmentModel,
        authID: String,
        link: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.saveAssessmentLink(assessmentModel.id, authID, link,
                onSuccess = {
                    onSuccess()
                }, onFailure = { msg ->
                    onFailure(msg)
                })
        }
    }

    fun updateAssessmentLink(
        assessmentId: String,
        authName: String,
        checked: Boolean,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.updateAssessmentLink(assessmentId, authName, checked,
                onSuccess = {
                    onSuccess()
                }, onFailure = { msg ->
                    onFailure(msg)
                })
        }
    }


}