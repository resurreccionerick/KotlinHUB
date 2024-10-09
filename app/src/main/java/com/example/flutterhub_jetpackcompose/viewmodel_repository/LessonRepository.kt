package com.example.flutterhub_jetpackcompose.viewmodel_repository

import android.net.Uri
import android.util.Log
import com.example.flutterhub_jetpackcompose.models.LessonModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//The repository handles FireStore operations. Here’s an example for adding lessons to Firestore.
class LessonRepository @Inject constructor() {

    private val firestore = FirebaseFirestore.getInstance()

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
}