package com.example.flutterhub_jetpackcompose.data.models

data class AssessmentModel(
    val assessmentId: String = "",
    val id: String = "",
    val title: String = "",
    val instructions: String = "",
    val links: List<AssessmentLink> = emptyList()
)


data class AssessmentLink(
    val id: String = "",
    val name: String = "",
    val link: String = "",
    var checked: Boolean = false
)