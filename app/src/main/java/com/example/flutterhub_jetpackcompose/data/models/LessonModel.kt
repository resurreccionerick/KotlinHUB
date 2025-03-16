package com.example.flutterhub_jetpackcompose.data.models


data class LessonModel(
    val id: String = "",
    val name: String = "",
    val subtopics: List<LessonSubtopic> = emptyList()
)

data class LessonSubtopic(
    val id: String = "",
    val name: String = "",
    val link: String = "",
    val description: String = ""
)
