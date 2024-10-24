package com.example.flutterhub_jetpackcompose.data.models

data class AssessmentModel(
    val id: String = "",
    val title: String = "",
    val instructions: String = "",
    val links: List<AssessmentLink> = emptyList()
)



data class AssessmentLink(
    val field: String = "",
    val link: String = ""
)