package com.androidrider.quizmint.Model

data class QuestionModel(

    val subjectName: String = "",
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = -1

)
