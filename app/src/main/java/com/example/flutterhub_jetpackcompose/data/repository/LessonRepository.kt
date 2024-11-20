package com.example.flutterhub_jetpackcompose.data.repository

import android.util.Log
import com.example.flutterhub_jetpackcompose.data.models.AssessmentLink
import com.example.flutterhub_jetpackcompose.data.models.AssessmentModel
import com.example.flutterhub_jetpackcompose.data.models.LessonModel
import com.example.flutterhub_jetpackcompose.data.models.QuizModel
import com.example.flutterhub_jetpackcompose.data.models.QuizScoreModel
import com.example.flutterhub_jetpackcompose.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//The repository handles FireStore operations. Here’s an example for adding lessons to Firestore.
class LessonRepository @Inject constructor() {

    private var difficulty: String = Hawk.get("difficulty", "null")
    private val firestore = FirebaseFirestore.getInstance()
    val realtimeDB = FirebaseDatabase.getInstance().reference
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
        name: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit, // callback if success
        onFailure: (String) -> Unit
    ) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()

            // Get current user after registration
            val user = authResult.user

            if (user != null) {
                val theUser = UserModel(
                    id = user.uid,
                    name = name,
                    email = email,
                    intermediate_Score = "0",
                    basic_Score = "0"
                )


                // Save the user data to the "users" collection with the UID as the document ID
                firestore.collection("users")
                    .document(user.uid)
                    .set(theUser)
                    .await() // Await the set operation to ensure it completes

                // Update the user's profile with name
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                user.updateProfile(profileUpdates).await()

                // Call onSuccess if everything is successful
                onSuccess()
            } else {
                onFailure("User registration failed. Please try again.")
            }
        } catch (e: Exception) {
            onFailure(e.message.toString())
            Log.e("userRegister ERROR: ", e.message.toString())
        }
    }


    suspend fun userLogin(
        email: String, pass: String, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        try {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Launch a coroutine to call the suspend function
                    GlobalScope.launch {
                        saveProfileDetails()
                    }
                    onSuccess()
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

    suspend fun saveProfileDetails() { // Save to Hawk
        val firebaseUser = auth.currentUser

        if (firebaseUser != null) {
            val userSnapshot =
                firestore.collection("users").document(firebaseUser.uid).get().await()
            userSnapshot.toObject(UserModel::class.java)

            // Safely extract scores as Strings
            val basicScore = userSnapshot.get("basic_Score") as? String ?: "0"
            val intermediateScore = userSnapshot.get("intermediate_Score") as? String ?: "0"

            // Create the UserModel object with scores
            val user = UserModel(
                id = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                basic_Score = basicScore.toString(),
                intermediate_Score = intermediateScore.toString()
            )

            // Save to Hawk
            Hawk.put("user_details", user)

            // Check and save role based on email
            if (firebaseUser.email == "esr@gmail.com" || firebaseUser.email == "hanansworks@gmail.com") {
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
            val difficulty = Hawk.get("difficulty", "")
            val saveRef = firestore.collection("quiz_scores_$difficulty").document()
            val scoreWithId = scoreModel.copy(id = saveRef.id)

            val userRef = firestore.collection("users").document(auth.currentUser!!.uid)

            // Prepare the fields to update based on the difficulty level


            val updates = if (difficulty.equals("basic")) {
                Log.d("DIFFICULTY WAS: ", difficulty)
                mapOf("basic_Score" to scoreModel.score)
            } else {
                Log.d("DIFFICULTY WAS : ", "NDI" + difficulty)
                mapOf("intermediate_Score" to scoreModel.score)
            }


            // Save the score and update specific fields in the user document
            saveRef.set(scoreWithId).await()
            userRef.update(updates).await()

            // Call onSuccess if both operations succeed
            onSuccess()
        } catch (e: Exception) {
            Log.e("saveQuiz ERROR: ", e.message.toString())
            onFailure(e.message.toString())
        }
    }


    // ---------------------------------------------------- SCORES ---------------------------------------------------- //


    suspend fun getScores(): List<QuizScoreModel> {
        return try {
            val difficulty = getDifficulty()
            val scoreSnapshots = firestore.collection("quiz_scores_$difficulty").get().await()
            val scoreList = scoreSnapshots.toObjects(QuizScoreModel::class.java)

            scoreList.sortedByDescending { it.score }
            //basicScoreSnapshots.toObjects(QuizScoreModel::class.java)
        } catch (e: Exception) {
            Log.e("GET getBasicScores ERROR: ", e.message.toString())
            emptyList()
        }
    }

    suspend fun getCombinedScores(): List<QuizScoreModel> { // for leaderboards of users
        return try {
            // Fetch all user documents from the "users" collection
            val userSnapshots = firestore.collection("users").get().await()

            // Convert snapshots to a list of UserModel
            val userList = userSnapshots.toObjects(UserModel::class.java)

            // Map the user data into QuizScoreModel instances with combined scores
            val combinedScores = userList.map { user ->
                val combinedScore =
                    (user.basic_Score.toIntOrNull() ?: 0) + (user.intermediate_Score.toIntOrNull()
                        ?: 0)
                QuizScoreModel(
                    id = user.id,
                    name = user.name,
                    score = combinedScore.toString() // Use a String or Int as needed
                )
            }.sortedByDescending { it.score.toIntOrNull() ?: 0 }

            combinedScores
        } catch (e: Exception) {
            Log.e("GET getCombinedScores ERROR: ", e.message.toString())
            emptyList()
        }
    }


    // ---------------------------------------------------- ASSESSMENT ---------------------------------------------------- //

    suspend fun addAssessment(
        assessmentModel: AssessmentModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            realtimeDB.child("assessment").child(assessmentModel.id).setValue(assessmentModel)
                .addOnCompleteListener { complete ->
                    if (complete.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure("Something went wrong")
                    }
                }.addOnFailureListener { msg ->
                    onFailure(msg.message.toString())
                }.await()
        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }

    fun updateAssessment(
        assessmentModel: AssessmentModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            realtimeDB.child("assessment").child(assessmentModel.id).setValue(assessmentModel)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure("Something went wrong")
                    }
                }
        } catch (e: Exception) {
            onFailure(e.message.toString())
        }
    }

    suspend fun getAssessment(userId: String): List<AssessmentModel> {
        return try {
            // Get data from Firebase Realtime Database
            val snapshot = realtimeDB.child("assessment").get().await()

            val assessmentList = mutableListOf<AssessmentModel>()

            for (child in snapshot.children) {
                val assessment = child.getValue(AssessmentModel::class.java)
                if (assessment != null) {
                    // Check if the user has checked the assessment
                    val userLink =
                        assessment.links?.get(userId) // Get the link for the specific user

                    if (userLink != null) {
                        // Log if the user has checked the assessment
                        Log.d("Assessment Checked", "User: $userId, Assessment: ${assessment.title}, Link: ${userLink.name}, CHECKED: ${userLink.checked}")
                    }

                    // Add the assessment to the list
                    assessmentList.add(assessment)
                }
            }

            assessmentList
        } catch (e: Exception) {
            Log.e("getAssessment ERROR: ", e.message.toString())
            emptyList()
        }
    }



//    suspend fun getAssessment(): List<AssessmentModel> {
//        return try {
//            // Get data from Firebase Realtime Database
//            val snapshot = realtimeDB.child("assessment").get().await()
//
//            val assessmentList = mutableListOf<AssessmentModel>()
//
//            // Loop through all the children of the "assessment" node
//            for (child in snapshot.children) {
//                val assessment = child.getValue(AssessmentModel::class.java)
//                if (assessment != null) {
//                    // Convert links Map to List if needed
//                    val linksList = assessment.links.values.toList()
//                    val updatedAssessment = assessment.copy(links = linksList.associateBy { it.id })
//                    assessmentList.add(updatedAssessment)
//                }
//            }
//
//            assessmentList
//        } catch (e: Exception) {
//            Log.e("getAssessment ERROR: ", e.message.toString())
//            emptyList()
//        }
//    }

    fun deleteAssessment(id: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            realtimeDB.child("assessment").child(id).removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure("Something went wrong.")
                    }
                }.addOnFailureListener { msg ->
                    onFailure(msg.message.toString())
                }
        } catch (e: Exception) {
            Log.e("DELETE ASSESSMENT ERROR: ", e.message.toString())
        }
    }

    fun saveAssessmentLink(
        assessmentModel: AssessmentModel,
        userId: String,
        userName: String,
        link: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val linkData = hashMapOf(
            "id" to userId,
            "name" to userName,
            "checked" to false,
            "link" to link
        )

        realtimeDB.child("assessment").child(assessmentModel.id).child("links").child(userId)
            .setValue(linkData)
            .addOnCompleteListener { db ->
                if (db.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Something went wrong on saving")
                }
            }.addOnFailureListener { msg ->
                onFailure(msg.message.toString())
            }
    }


    fun updateAssessmentLink(
        assessmentId: String,
        linkId: String,
        authID: String,
        checked: Boolean,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val linkData = mapOf(
            "checked" to checked
        )

        Log.d(
            "UPDATE ASSESSMENT LINK:",
            "ASSESSMENT ID: " + assessmentId + " LINK ID: ${linkId}" + " AUTH ID: ${authID}, " + " CHECKED $checked"
        )

        realtimeDB.child("assessment").child(assessmentId).child("links").child(linkId)
            .updateChildren(linkData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Something went wrong.")
                }
            }.addOnFailureListener { msg ->
                Log.e("saveAssessmentLink ERROR: ", msg.message.toString())
                onFailure(msg.message.toString())
            }
    }

    fun checkIfUserChecked(
        assessmentId: String,
        userName: String,
        onResult: (Boolean) -> Unit,
        getLink: (String) -> Unit
    ) {
        // Access the specific child node in the Realtime Database
        realtimeDB.child("assessment").child(assessmentId).child("links").child(userName).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Check if the 'checked' field is true/false
                    val isChecked = snapshot.child("checked").getValue(Boolean::class.java) ?: false
                    // Get the link associated with the user, if needed
                    val link = snapshot.child("link").getValue(String::class.java) ?: ""

                    Log.d("checkIfUserChecked", "Check status for $userName: $isChecked")

                    // Return the results through the callbacks
                    onResult(isChecked)
                    getLink(link)
                } else {
                    Log.d("checkIfUserChecked", "Data for $userName not found.")
                    onResult(false) // Default to false if no data found
                }
            }
            .addOnFailureListener { e ->
                Log.e("checkIfUserChecked ERROR", "Failed to fetch data: ${e.message}")
                onResult(false) // Default to false on failure
            }
    }


    suspend fun getLinksByID(assessmentId: String): List<AssessmentLink> {
        return try {
            // Get data from Firebase Realtime Database
            val snapshot =
                realtimeDB.child("assessment").child(assessmentId).child("links").get().await()

            val links = mutableListOf<AssessmentLink>()

            // Loop through all the children of the "links" node
            for (child in snapshot.children) {
                // Directly deserialize each child into an AssessmentLink object
                val assessmentLink = child.getValue(AssessmentLink::class.java)
                if (assessmentLink != null) {
                    links.add(assessmentLink)
                } else {
                    Log.w("getLinksByID", "Skipped invalid link node: ${child.key}")
                }
            }

            links
        } catch (e: Exception) {
            Log.e("getLinksByID ERROR", ": ${e.message}")
            emptyList()  // Return an empty list in case of error
        }
    }

//    suspend fun getLinksByID(assessmentId: String): List<AssessmentLink> {
//        return try {
//            // Get data from Firebase Realtime Database
//            val snapshot =
//                realtimeDB.child("assessment").child(assessmentId).child("links").get().await()
//
//            val links = mutableListOf<AssessmentLink>()
//
//            // Loop through all the children of the "assessment" node (e.g., F1OAjyaoxqQSSVALAfN5bi2BDdt2)
//            for (child in snapshot.children) {
//                val linkSnapshot = child.child("link")  // Navigate to the "link" node
//                val assessmentLink = linkSnapshot.getValue(AssessmentLink::class.java)
//
//                if (assessmentLink != null) {
//                    links.add(assessmentLink)
//                }
//            }
//
//            links
//        } catch (e: Exception) {
//            Log.e("getLinksByID ERROR", ":  ${e.message}")
//            emptyList()  // Return an empty list in case of error
//        }
//    }

}