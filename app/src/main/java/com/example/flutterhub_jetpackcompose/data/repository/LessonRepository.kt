package com.example.flutterhub_jetpackcompose.data.repository

import android.util.Log
import com.example.flutterhub_jetpackcompose.data.models.LessonModel
import com.example.flutterhub_jetpackcompose.data.models.QuizModel
import com.example.flutterhub_jetpackcompose.data.models.QuizScoreModel
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//The repository handles FireStore operations. Here’s an example for adding lessons to Firestore.
class LessonRepository @Inject constructor() {

    private var difficulty: String = Hawk.get("difficulty", "null")
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    fun refreshDifficulty() {
        difficulty = Hawk.get("difficulty", "default")
        Log.d("Hawk LessonRepository", "Difficulty refreshed to: $difficulty")
    }


    private fun getDifficulty(): String {
        return difficulty
    }


    // ---------------------------------------------------- USER PART ---------------------------------------------------- //
    suspend fun userRegister(
        name: String, email: String, pass: String, onSuccess: () -> Unit, //callback if success
        onFailure: (String) -> Unit
    ) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()

            // Get current user after registration
            val user = authResult.user

            // Check if user is not null, then update their profile with name
            user?.let {
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()

                // Update the user's profile with name
                it.updateProfile(profileUpdates).await()
                Log.d("userRegister", "User profile updated with name: $name")

                onSuccess()
            } ?: run {
                onFailure("User registration failed. Please try again.")
            }
        } catch (e: Exception) {
            onFailure(e.message.toString())
            Log.e("userRegister ERROR: ", e.message.toString())
        }
    }

    fun userLogin(
        email: String, pass: String, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        try {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    saveProfileDetails()
                } else {
                    onFailure("No account found")
                }
            }.addOnFailureListener { fail ->
                onFailure("Error: " + fail.message.toString())
            }
        } catch (e: Exception) {
            Log.e("userLogin ERROR: ", e.message.toString())
            onFailure(e.message.toString())
        }
    }

    fun userLogout(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            auth.signOut()
            auth.addAuthStateListener { firebaseAuth ->
                if (firebaseAuth.currentUser == null) {
                    onSuccess()
                }
            }

        } catch (e: Exception) {
            onFailure(e.message.toString())
            Log.e("userLogout ERROR: ", e.message.toString())
        }
    }

    private fun saveProfileDetails() { //save to hawk
        val firebaseUser = auth.currentUser

        if (firebaseUser != null) {
            val user = UserModel(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: ""
            )

            Hawk.put("user_details", user)

            if (firebaseUser.email.equals("esr@gmail.com") || firebaseUser.email.equals("hanansworks@gmail.com")) {
                Hawk.put("role", "admin")
            } else {
                Hawk.put("role", "user")
            }
        }
    }


    fun forgotPass(
        email: String, onSuccess: () -> Unit,  // Callback when the operation succeeds
        onFailure: (String) -> Unit // Callback when the operation fails
    ) {
        try {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess() // Call success callback
                }
            }.addOnFailureListener { fail ->
                onFailure("Error: " + fail.message.toString())
            }
        } catch (e: Exception) {
            onFailure(e.message.toString()) // Call failure callback on exception
            Log.e("forgotPass ERROR: ", e.message.toString())
        }
    }


    // ---------------------------------------------------- LESSON ---------------------------------------------------- //

    suspend fun addLesson(
        lesson: LessonModel, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        try {
            val difficulty = getDifficulty()

            val lessonDocRef =
                firestore.collection(difficulty) // Generate a document reference with an auto-ID
                    .document()

            val lessonWithID =
                lesson.copy(id = lessonDocRef.id)  // Add the generated ID to the lesson model

            lessonDocRef.set(lessonWithID) // Upload the lesson with the auto-generated ID
                .await()

            onSuccess()
        } catch (e: Exception) {
            onFailure(e.message.toString())
            Log.e("ADD LESSON ERROR: ", e.message.toString())
        }
    }


    // Function to fetch all lessons from Firestore
    suspend fun getLessons(): List<LessonModel> {
        return try {
            val difficulty = getDifficulty()
            val snapshot = firestore.collection(difficulty).get().await()


            snapshot.toObjects(LessonModel::class.java)
        } catch (e: Exception) {
            Log.e("GET LESSON ERROR: ", e.message.toString())
            emptyList()
        }
    }

    //update
    fun updateLesson(
        lesson: LessonModel, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        try {
            val difficulty = getDifficulty()

            firestore.collection(difficulty).document(lesson.id).set(lesson).addOnCompleteListener {
                onSuccess()
            }.addOnFailureListener { error ->
                onFailure(error.message.toString())
            }
        } catch (e: Exception) {
            onFailure(e.message.toString())
            Log.e("UPDATE LESSON ERROR: ", e.message.toString())
        }
    }

    //DELETE
    fun deleteLesson(id: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            val difficulty = getDifficulty()

            firestore.collection(difficulty).document(id).delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Failed to delete")
                }
            }.addOnFailureListener { error ->
                onFailure(error.message.toString())
            }
        } catch (e: Exception) {
            Log.e("DELETE LESSON ERROR: ", e.message.toString())
        }
    }


    // ---------------------------------------------------- QUIZZES ---------------------------------------------------- //


    suspend fun addQuiz(
        quizzes: QuizModel, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        try {
            val difficulty = getDifficulty()
            val quizDocRef =
                firestore.collection("quizzes_$difficulty") // Generate a document reference with an auto-ID
                    .document()

            val quizWithID =
                quizzes.copy(id = quizDocRef.id) // Add the generated ID to the quiz model

            quizDocRef.set(quizWithID).await()  // Upload the lesson with the auto-generated ID

            onSuccess()

        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }

    suspend fun getQuizzes(
    ): List<QuizModel> {
        return try {
            val difficulty = getDifficulty()

            val snapshot = firestore.collection("quizzes_$difficulty").get().await()
            snapshot.toObjects(QuizModel::class.java) // Get the quiz list

        } catch (e: Exception) {
            Log.e("getQuizzes ERROR: ", e.message.toString())
            emptyList()
        }
    }

    fun deleteQuiz(id: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            val difficulty = getDifficulty()

            firestore.collection("quizzes_$difficulty").document(id).delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()

                    } else {
                        onFailure("Failed to delete this quiz")
                    }
                }
        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }

    fun updateQuiz(quiz: QuizModel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            val difficulty = getDifficulty()

            firestore.collection("quizzes_$difficulty").document(quiz.id).set(quiz)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure("Failed to update quiz")
                    }
                }
        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }

    suspend fun saveQuiz(
        scoreModel: QuizScoreModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val difficulty = getDifficulty()
            val saveRef = firestore.collection("quiz_scores_$difficulty").document()

            val scoreWithId = scoreModel.copy(id = saveRef.id)

            saveRef.set(scoreWithId).await()

            onSuccess()

        } catch (e: Exception) {
            Log.e("getQuizzes ERROR: ", e.message.toString())
            onFailure(e.message.toString())
        }
    }


}