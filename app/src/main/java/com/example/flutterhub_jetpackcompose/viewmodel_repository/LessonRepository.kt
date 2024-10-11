package com.example.flutterhub_jetpackcompose.viewmodel_repository

import android.net.Uri
import android.util.Log
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//The repository handles FireStore operations. Here’s an example for adding lessons to Firestore.
class LessonRepository @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun addLesson(lesson: LessonModel) {
        try {
            val lessonDocRef =
                firestore.collection("lessons") // Generate a document reference with an auto-ID
                    .document(lesson.id)

            val lessonWithID =
                lesson.copy(id = lessonDocRef.id)  // Add the generated ID to the lesson model

            lessonDocRef.set(lessonWithID) // Upload the lesson with the auto-generated ID
                .await()
        } catch (e: Exception) {
            Log.e("ADD LESSON ERROR: ", e.message.toString())
        }
    }


    // Function to fetch all lessons from Firestore
    suspend fun getLessons(): List<LessonModel> {
        return try {
            firestore.collection("lessons")
                .get()
                .await()
                .toObjects(LessonModel::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    //update
    suspend fun updateLesson(lesson: LessonModel) {
        try {
            firestore.collection("lessons")
                .document(lesson.id)
                .set(lesson)
                .await()
        } catch (e: Exception) {
            Log.e("UPDATE LESSON ERROR: ", e.message.toString())
        }
    }

    //DELETE
    suspend fun deleteLesson(id: String) {
        try {
            firestore.collection("lessons")
                .document(id)
                .delete()
        } catch (e: Exception) {
            Log.e("DELETE LESSON ERROR: ", e.message.toString())
        }
    }

    suspend fun uploadImageToFirebase(img: Uri): String? {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/${img.lastPathSegment}")
        return try {
            storageRef.putFile(img).await()
            storageRef.downloadUrl.await()
                .toString() // Return the download URL of the uploaded image
        } catch (e: Exception) {
            null
        }
    }

    // ---------------------------------------------------- USER PART ---------------------------------------------------- //
    suspend fun userRegister(
        name: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit, //callback if success
        onFailure: (String) -> Unit
    ) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()

            // Get current user after registration
            val user = authResult.user

            // Check if user is not null, then update their profile with name
            user?.let {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

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

    suspend fun userLogin(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                        Log.e("USER LOGIN SUCCESS", "USER LOGIN SUCCESS")
                    } else {
                        onFailure("No account found")
                    }
                }.addOnFailureListener { fail ->
                    onFailure("Error: " + fail.message.toString())
                }
        } catch (e: Exception) {
            Log.e("userLogin ERROR: ", e.message.toString())
        }
    }


    suspend fun forgotPass(
        email: String,
        onSuccess: () -> Unit,  // Callback when the operation succeeds
        onFailure: (String) -> Unit // Callback when the operation fails
    ) {
        try {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
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

}