package com.example.flutterhub_jetpackcompose.viewmodel


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flutterhub_jetpackcompose.data.models.AssessmentLink
import com.example.flutterhub_jetpackcompose.data.models.AssessmentModel
import com.example.flutterhub_jetpackcompose.data.models.LessonModel
import com.example.flutterhub_jetpackcompose.data.models.LessonSubtopic
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

    val assessment = mutableStateListOf<AssessmentModel>()
    val links = mutableStateListOf<AssessmentLink>()
    val quizzes = mutableStateListOf<QuizModel>()
    val lessons = mutableStateListOf<LessonModel>()
    val subLessons = mutableStateListOf<LessonSubtopic>()
    val scores = mutableStateListOf<QuizScoreModel>()
    val darkMode = mutableStateOf(false)
    val darkModeState: State<Boolean> = darkMode
    val isLoading = mutableStateOf(false)



    // Toggle dark mode state
    fun toggleDarkMode(isDarkMode: Boolean) {
        darkMode.value = isDarkMode
    }

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

    fun getOverallLeaderboards() {
        loadOverallLeaderboards()
    }


    private fun loadLessons() {
        if (isLoading.value) return

        viewModelScope.launch {
            lessons.clear()
            lessons.addAll(repository.getLessons())
        }
    }

    fun loadSubLessons(lessonID: String) {
        if (isLoading.value) return  // Prevent duplicate calls

        viewModelScope.launch {
            isLoading.value = true // Start loading

            try {
                val fetchedSubLessons = repository.getSubtopics(lessonID)
                Log.e("LOAD SUBTOPICS", "Fetched ${fetchedSubLessons.size} subtopics")

                subLessons.clear()
                subLessons.addAll(fetchedSubLessons)
            } catch (e: Exception) {
                Log.e("LOAD ERROR", e.message ?: "Unknown error")
            } finally {
                isLoading.value = false
            }
        }
    }


    fun addNewLesson(
        name: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val lesson = LessonModel(
                id = "", name = name,
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

    fun addNewSubLesson(
        lessonId: String,
        name: String,
        desc: String,
        link: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val lesson = LessonSubtopic(
                id = "", name = name,
                description = desc, link = link,
            )

            repository.addSubLesson(lessonId, lesson,
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
        if (isLoading.value) return

        viewModelScope.launch {
            val newQuizzes = repository.getQuizzes()
            quizzes.clear()
            quizzes.addAll(newQuizzes)
        }
    }


    fun saveQuizScore(
        userID: String,
        scoreModel: QuizScoreModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.saveQuiz(userID, scoreModel,
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
        if (isLoading.value) return

        viewModelScope.launch {
            scores.clear()
            scores.addAll(repository.getScores())
        }
    }

    private fun loadOverallLeaderboards() {
        if (isLoading.value) return

        viewModelScope.launch {
            scores.clear()
            scores.addAll(repository.getCombinedScores())
        }
    }


    // ---------------------------------------------------- ASSESSMENT ---------------------------------------------------- //
    fun loadAssessment(userID: String) {
        if (isLoading.value) return

        viewModelScope.launch {
            val _assessment = repository.getAssessment(userId = userID)

            assessment.clear()
            assessment.addAll(_assessment)
        }
    }

    fun getAssessmentById(assessmentId: String): AssessmentModel {
        // This function returns the lesson by its ID.
        return assessment.find { it.id == assessmentId } ?: AssessmentModel()
    }


    fun addAssessment(
        userID: String,
        assessmentModel: AssessmentModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.addAssessment(assessmentModel, onSuccess = {
                onSuccess()
                loadAssessment(userID)
            }, onFailure = { msg ->
                onFailure(msg)
            })
        }
    }


    fun updateAssessment(
        userID: String,
        assessment: AssessmentModel,
        onSuccess: () -> Boolean,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.updateAssessment(assessment, onSuccess = {
                onSuccess()
                loadAssessment(userID)
            }, onFailure = { msg ->
                onFailure(msg)
            })
        }
    }

    fun deleteAssessment(
        userID: String,
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.deleteAssessment(id, onSuccess = {
                onSuccess()
                loadAssessment(userID)
            }, onFailure = { msg ->
                onFailure(msg)
            })
        }
    }

    // ---------------------------------------------------- SAVE ASSESSMENT LINK ---------------------------------------------------- //
    fun saveAssessmentLink(
        assessmentModel: AssessmentModel,
        authID: String,
        authName: String,
        link: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.saveAssessmentLink(assessmentModel, authID, authName, link,
                onSuccess = {
                    onSuccess()
                }, onFailure = { msg ->
                    onFailure(msg)
                })
        }
    }

    fun updateAssessmentLink(
        assessmentId: String,
        linkId: String,
        authID: String,
        checked: String,
        comment: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.updateAssessmentLink(assessmentId,
                linkId,
                authID,
                checked,
                comment,
                onSuccess = {
                    onSuccess()

                },
                onFailure = { msg ->
                    onFailure(msg)
                })
        }
    }

    fun checkUserStatus(
        id: String,
        userID: String,
        onResult: (String) -> Unit,
        getLink: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.checkIfUserChecked(id, userID, getLink = { link ->
                getLink(link)
            }, onResult = { res ->
                onResult(res)
            })
        }
    }

    fun loadLinks(id: String) {
        if (isLoading.value) return

        viewModelScope.launch {
            val _links = repository.getLinksByID(id)

            links.clear()
            links.addAll(_links)
        }
    }

    suspend fun refreshProfileDetails() {
        repository.saveProfileDetails()
    }
}