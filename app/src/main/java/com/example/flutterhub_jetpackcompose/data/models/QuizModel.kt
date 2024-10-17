package com.example.flutterhub_jetpackcompose.data.models

data class QuizModel(
    val id: String = "",
    val question: String = "",
   // val difficulty: String = "",
    val choices: List<String> = emptyList(),
    val selectedAns: String = ""  //correct ans
)