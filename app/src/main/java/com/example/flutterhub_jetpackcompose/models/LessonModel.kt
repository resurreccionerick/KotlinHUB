package com.example.flutterhub_jetpackcompose.models

data class LessonModel(
    val id: String,
    val name: String,
    val description: String,
    val imgUrl: String,
    val categories: List<Category> = emptyList()
)

data class Category(
    val id: String,
    val name: String,
    val content: List<Content> = emptyList()
)

data class Content(
    val id: String,
    val name: String,
    val videoUrl: String
)